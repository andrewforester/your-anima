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

if [ ! -x "$ANDROID_SDK/cmdline-tools/latest/bin/sdkmanager" ]; then
  mkdir -p "$ANDROID_SDK/cmdline-tools"
  tmp=$(mktemp -d)
  curl -sfL -o "$tmp/tools.zip" "https://dl.google.com/android/repository/$CMDLINE_TOOLS_ZIP"
  unzip -q "$tmp/tools.zip" -d "$tmp"
  rm -rf "$ANDROID_SDK/cmdline-tools/latest"
  mv "$tmp/cmdline-tools" "$ANDROID_SDK/cmdline-tools/latest"
  rm -rf "$tmp"
fi

yes | "$ANDROID_SDK/cmdline-tools/latest/bin/sdkmanager" --licenses >/dev/null 2>&1 || true
"$ANDROID_SDK/cmdline-tools/latest/bin/sdkmanager" --install "platform-tools" "platforms;android-$COMPILE_SDK" >/dev/null

echo "sdk.dir=$ANDROID_SDK" > local.properties
if [ -n "${CLAUDE_ENV_FILE:-}" ]; then
  echo "export ANDROID_HOME=\"$ANDROID_SDK\"" >> "$CLAUDE_ENV_FILE"
fi

# Warm up Gradle wrapper and dependency cache (container state is cached after the hook).
./gradlew --quiet help >/dev/null
