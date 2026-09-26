#!/bin/bash
# SessionStart hook for Claude Code on the web: prepares Android SDK + Gradle
# so that builds, lint and tests can run inside the cloud container.
set -euo pipefail

if [ "${CLAUDE_CODE_REMOTE:-}" != "true" ]; then
  exit 0
fi

cd "$CLAUDE_PROJECT_DIR"

ANDROID_SDK="${ANDROID_HOME:-$HOME/android-sdk}"
CMDLINE_TOOLS_ZIP="commandlinetools-linux-13114758_latest.zip"
COMPILE_SDK=$(grep -E '^android-compileSdk' gradle/libs.versions.toml | sed -E 's/.*"([0-9]+)".*/\1/')

# Google Maven / Android SDK live on dl.google.com. Without it nothing Compose-related resolves.
if ! curl -sf -m 15 -o /dev/null "https://dl.google.com/android/repository/repository2-3.xml"; then
  echo "WARNING: dl.google.com is not reachable from this environment." >&2
  echo "Gradle builds will fail. Add dl.google.com to the environment's allowed domains." >&2
  exit 0
fi

# --- Android SDK -------------------------------------------------------------
if [ ! -x "$ANDROID_SDK/cmdline-tools/latest/bin/sdkmanager" ]; then
  mkdir -p "$ANDROID_SDK/cmdline-tools"
  tmp=$(mktemp -d)
  curl -sfL -o "$tmp/tools.zip" "https://dl.google.com/android/repository/$CMDLINE_TOOLS_ZIP"
  unzip -q "$tmp/tools.zip" -d "$tmp"
  rm -rf "$ANDROID_SDK/cmdline-tools/latest"
  mv "$tmp/cmdline-tools" "$ANDROID_SDK/cmdline-tools/latest"
  rm -rf "$tmp"
fi

SDKMANAGER="$ANDROID_SDK/cmdline-tools/latest/bin/sdkmanager"
yes | "$SDKMANAGER" --licenses >/dev/null 2>&1 || true
# Since API 36 platforms are published as `android-<major>.<minor>` (e.g. `android-37.0`).
if [ ! -d "$ANDROID_SDK/platforms/android-$COMPILE_SDK" ] && [ ! -d "$ANDROID_SDK/platforms/android-$COMPILE_SDK.0" ]; then
  "$SDKMANAGER" --install "platform-tools" "platforms;android-$COMPILE_SDK" >/dev/null 2>&1 \
    || "$SDKMANAGER" --install "platform-tools" "platforms;android-$COMPILE_SDK.0" >/dev/null
fi

echo "sdk.dir=$ANDROID_SDK" > local.properties
if [ -n "${CLAUDE_ENV_FILE:-}" ]; then
  echo "export ANDROID_HOME=\"$ANDROID_SDK\"" >> "$CLAUDE_ENV_FILE"
fi

# --- Maven Central mirror ----------------------------------------------------
# repo.maven.apache.org answers 429 to download bursts from the shared egress IP.
# Route it through Google's official Maven Central mirror (container only, not the repo build).
mkdir -p "$HOME/.gradle/init.d"
cat > "$HOME/.gradle/init.d/maven-central-mirror.gradle.kts" <<'EOF'
val mirror = "https://maven-central.storage-download.googleapis.com/maven2/"

fun RepositoryHandler.useMirror() = withType(MavenArtifactRepository::class.java).configureEach {
    if (url.host == "repo.maven.apache.org" || url.host == "repo1.maven.org") setUrl(mirror)
}

beforeSettings {
    pluginManagement.repositories.useMirror()
    dependencyResolutionManagement.repositories.useMirror()
}

allprojects {
    buildscript.repositories.useMirror()
    repositories.useMirror()
}
EOF

# --- Yarn: GitHub tarballs ---------------------------------------------------
# Kotlin/Wasm tooling depends on `github:Kotlin/karma`, which yarn downloads from
# codeload.github.com. The session's GitHub proxy blocks that host but serves git reads,
# so build those tarballs with git and put them in yarn's offline mirror (checked first).
YARN_MIRROR="$HOME/.cache/yarn-offline-mirror"
mkdir -p "$YARN_MIRROR"
touch "$HOME/.yarnrc"
grep -q '^yarn-offline-mirror ' "$HOME/.yarnrc" || echo "yarn-offline-mirror \"$YARN_MIRROR\"" >> "$HOME/.yarnrc"
grep -q '^yarn-offline-mirror-pruning ' "$HOME/.yarnrc" || echo "yarn-offline-mirror-pruning false" >> "$HOME/.yarnrc"

seed_codeload_tarballs() {
  local url repo sha tmp urls
  urls=$(cat "$HOME"/.kotlin/kotlin-npm-tooling/yarn/*/yarn.lock kotlin-js-store/*/yarn.lock 2>/dev/null \
    | grep -oE 'https://codeload\.github\.com/[^/]+/[^/]+/tar\.gz/[0-9a-f]{40}' | sort -u || true)
  for url in $urls; do
    repo=$(echo "$url" | cut -d/ -f4-5)
    sha=${url##*/}
    [ -s "$YARN_MIRROR/$sha" ] && continue
    tmp=$(mktemp -d)
    git -C "$tmp" init -q
    git -C "$tmp" fetch -q --depth 1 "https://github.com/$repo" "$sha"
    git -C "$tmp" archive --format=tar.gz --prefix="${repo#*/}-$sha/" -o "$YARN_MIRROR/$sha" FETCH_HEAD
    rm -rf "$tmp"
  done
}

# --- Warm up -----------------------------------------------------------------
# Gradle wrapper, dependency cache and the Wasm npm tooling. The first tooling
# setup writes its yarn.lock and fails on the GitHub tarball; seed it and retry.
seed_codeload_tarballs
./gradlew --quiet help >/dev/null
if ! ./gradlew --quiet kotlinWasmToolingSetup >/dev/null 2>&1; then
  seed_codeload_tarballs
  ./gradlew --quiet kotlinWasmToolingSetup >/dev/null
fi
