package app.youranima.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class AppThemeTest {
    @Test
    fun textIsDisplayedInAppTheme() =
        runComposeUiTest {
            setContent {
                AppTheme {
                    Text("Andrew", style = MaterialTheme.appTypography.name)
                }
            }

            onNodeWithText("Andrew").assertIsDisplayed()
        }

    @Test
    fun materialSchemeMapsToAppTokens() =
        runComposeUiTest {
            var background = Color.Unspecified
            var onSurfaceVariant = Color.Unspecified
            var tokens: AppColors? = null
            setContent {
                AppTheme {
                    background = MaterialTheme.colorScheme.background
                    onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant
                    tokens = MaterialTheme.appColors
                }
            }

            waitForIdle()
            assertEquals(Color(0xFF0D0F2B), background)
            assertEquals(tokens?.onSurfaceMuted, onSurfaceVariant)
        }

    @Test
    fun homeFeedTokensMatchSpec() {
        val colors = DarkAppColors
        assertEquals(Color(0xFF051B6F), colors.tipGradientStart)
        assertEquals(Color(0xFF36327A), colors.tipGradientEnd)
        assertEquals(Color(0xFF252B78), colors.cardGlow)
    }

    @Test
    fun showcaseRenders() =
        runComposeUiTest {
            setContent { AppTheme { ThemeShowcase() } }

            onNodeWithText("surface").assertIsDisplayed()
        }
}
