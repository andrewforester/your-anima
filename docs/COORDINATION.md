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

## Открытые вопросы

- Имя пакета — выбирает сессия каркаса, фиксирует здесь.
- Ветка каркаса — фиксирует здесь.
