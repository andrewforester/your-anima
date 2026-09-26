---
name: implement-screen
description: Implement a new app screen (from a Figma frame, screenshot or description) in Compose Multiplatform, following project conventions, with tests and visual verification. Use whenever the task is to add or rebuild a screen or a reusable UI component.
---

# Implement a screen

1. **Gather the design.** With a Figma link: fetch design context, variables and a screenshot of the frame. With an image: study it carefully (spacing, type scale, colors, states). Write down the component tree before coding.
2. **Tokens first.** Any new color / text style / shape / spacing goes into `composeApp/src/commonMain/kotlin/app/youranima/ui/theme/`. Reuse existing tokens where values match within ~2px / near-identical color.
3. **Assets.** Icons as vector drawables (`composeResources/drawable/*.xml`), images in `composeResources/drawable/`, strings in `composeResources/values/strings_<screen>.xml` with keys prefixed `<screen>_` (see `docs/COORDINATION.md`).
4. **Components.** Reusable pieces go to `ui/components/`. Screen-specific pieces stay in `ui/<screen>/`.
5. **Screen.** `ui/<screen>/<Name>Screen.kt`: stateless composable taking UI state + callbacks, `modifier` param, `<Name>ScreenTags`, `@Preview`. Mock data from `data/`.
6. **Test.** Add `composeApp/src/commonTest/.../<Name>ScreenTest.kt` using `runComposeUiTest` that checks key content is displayed.
7. **Verify.** `./gradlew ktlintFormat ktlintCheck :composeApp:jvmTest`. If the build works in this environment, also build the web bundle, serve `composeApp/build/dist/wasmJs/productionExecutable` with `python3 -m http.server`, screenshot it with Playwright at a phone viewport (390x844) and compare side by side with the design. Fix visible differences before pushing.
8. **Push and open a PR.** PR CI runs only lint + JVM tests; there is no branch web preview unless someone runs the CI workflow manually on the branch. Report the local screenshot from step 7 in the PR.
