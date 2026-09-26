# Координация параллельных сессий

Репозиторий: `andrewforester/your-anima` (каркас делается здесь, не в Mallorca).

## Зоны ответственности

| Что | Сессия |
|---|---|
| `settings.gradle.kts`, `build.gradle.kts`, `gradle/libs.versions.toml`, модули, точки входа (`MainActivity`, `MainViewController`), `App.kt` | Каркас |
| `composeApp/src/commonMain/kotlin/<package>/ui/**` — тема, токены, компоненты, экраны | Figma → Compose UI |

- UI-сессия не трогает build-файлы; сессия каркаса не создаёт файлы в `ui/`.
- Модуль: `composeApp` (стандартный шаблон JetBrains KMP).
- Зависимости, нужные UI: `compose.material3`, `compose.components.resources`, `compose.components.uiToolingPreview`.

## Точка стыка

`App.kt` вызывает `AppTheme { HomeScreen() }`. Каркас может оставить заглушки — UI-сессия заменит их.

## Порядок слияния

1. Каркас → `main`.
2. UI-ветка `claude/compose-parallel-sessions-98xomw` мержит `main` в себя, собирается, подключается в `App.kt`.

## Решения каркаса

- **Пакет:** `app.youranima`. UI-код: `composeApp/src/commonMain/kotlin/app/youranima/ui/**`
  (например, `app.youranima.ui.theme.AppTheme`, `app.youranima.ui.home.HomeScreen`).
- **Ресурсы:** `composeApp/src/commonMain/composeResources/`, класс `Res` в пакете `app.youranima.resources`.
  Каркас ресурсов не создаёт — `strings.xml`, шрифты и картинки добавляет UI-сессия.
- **Ветка каркаса:** `claude/cloud-dev-mobile-2enuva` (в неё уже влит этот файл из UI-ветки, поэтому после слияния в `main` конфликта по нему не будет).
- **Модули:** `composeApp` (KMP-библиотека: Android, iOS, Wasm, JVM для тестов, плюс web entry point), `androidApp` (тонкая Android-оболочка — AGP 9 не разрешает application-плагин в KMP-модуле), `iosApp` (Xcode, фреймворк `ComposeApp`).
- **Зависимости для UI** подключены через version catalog (аналоги устаревших аксессоров `compose.*`):
  `libs.compose.material3`, `libs.compose.components.resources`, `libs.compose.uiToolingPreview`.
- **Стык:** в `App.kt` приватные заглушки `AppTheme` и `HomeScreen`, помечены `TODO(ui)`. UI-сессия удаляет их и добавляет импорты из `app.youranima.ui.*`.
- **Проверка перед пушем:** `./gradlew ktlintCheck :composeApp:jvmTest` (ktlint 1.8.0, `@Composable` функции в PascalCase разрешены).
