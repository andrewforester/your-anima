package app.youranima.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
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
        assertEquals(Color(0xFF323996), colors.lockBadge)
        assertEquals(Color(0xFF051B6F), colors.tipGradientStart)
        assertEquals(Color(0xFF36327A), colors.tipGradientEnd)
        assertEquals(Color(0xFF26A09B), colors.yesGradientStart)
        assertEquals(Color(0xFF2478A4), colors.yesGradientEnd)
        assertEquals(Color(0xFFB24044), colors.noGradientStart)
        assertEquals(Color(0xFF6C235F), colors.noGradientEnd)
        assertEquals(Color(0xFF252B78), colors.cardGlow)

        val type = appTypography(FontFamily.Default)
        assertEquals(
            TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, lineHeight = 26.sp),
            type.sectionTitle.copy(fontFamily = null),
        )
        assertEquals(TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal, lineHeight = 23.sp), type.preview.copy(fontFamily = null))
        assertEquals(
            TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal, lineHeight = 18.sp),
            type.bodyRegular.copy(fontFamily = null),
        )
        assertEquals(TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold), type.headline.copy(fontFamily = null))
    }

    @Test
    fun showcaseRenders() =
        runComposeUiTest {
            setContent { AppTheme { ThemeShowcase() } }

            onNodeWithText("surface").assertIsDisplayed()
        }
}
