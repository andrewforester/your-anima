package app.youranima

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class AppTest {
    @Test
    fun appRendersWithoutCrashing() =
        runComposeUiTest {
            setContent { App() }

            onRoot().assertExists()
        }
}
