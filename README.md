# Your Anima

Compose Multiplatform app for **Android, iOS and mobile Web** from a single Kotlin codebase, developed AI-first with Claude Code.

- Architecture, commands and conventions: [`CLAUDE.md`](CLAUDE.md)
- Web preview (GitHub Pages): `https://andrewforester.github.io/your-anima/` (branches, via manual CI run: `/preview/<branch-with-dashes>/`)
- Android APK (latest `main`): `https://github.com/andrewforester/your-anima/releases/download/main-latest/your-anima-debug.apk`

## Quick start

```bash
./gradlew :androidApp:assembleDebug            # Android
./gradlew :composeApp:wasmJsBrowserDevelopmentRun  # Web
open iosApp/iosApp.xcodeproj                   # iOS (macOS + Xcode)
```

Requirements: JDK 21, Android SDK (or `ANDROID_HOME`), Xcode for iOS.

The Gradle and Xcode setup is based on JetBrains' [KMP-App-Template](https://github.com/Kotlin/KMP-App-Template) (Apache 2.0).
