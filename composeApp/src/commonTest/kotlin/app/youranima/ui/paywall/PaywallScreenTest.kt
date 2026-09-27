package app.youranima.ui.paywall

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.test.swipeLeft
import app.youranima.data.paywall.MockPaywallRepository
import app.youranima.ui.theme.AppTheme
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class PaywallScreenTest {
    private val data = MockPaywallRepository.paywallData()

    /** The legal text's charge phrase; price and period are rendered with non-breaking spaces. */
    private fun legalCharge(
        price: String,
        period: String,
    ) = "charged ${price.replace(' ', NBSP)} every ${period.replace(' ', NBSP)}"

    private companion object {
        const val NBSP = '\u00A0'
    }

    @Test
    fun showsOfferWithDefaultPlanSelected() =
        runComposeUiTest {
            setContent { AppTheme { PaywallScreen(onClose = {}) } }

            onNodeWithTag(PaywallScreenTags.SCREEN).assertIsDisplayed()
            onNodeWithText("Anima Premium").assertIsDisplayed()
            onNodeWithTag(PaywallScreenTags.RESTORE).assertIsDisplayed()
            onNodeWithText("Daily horoscope").assertIsDisplayed()
            onNodeWithTag(PaywallScreenTags.PAGE_INDICATOR).assertIsDisplayed()
            onNodeWithTag(PaywallScreenTags.HOT_DEAL).assertIsDisplayed()
            onNodeWithText("Save 23%").assertIsDisplayed()
            onNodeWithText("Save 68%").assertIsDisplayed()

            onNodeWithTag(PaywallScreenTags.plan("quarterly")).assertIsSelected()
            onNodeWithTag(PaywallScreenTags.plan("weekly")).assertIsNotSelected()
            onNodeWithTag(PaywallScreenTags.LEGAL).assertTextContains(legalCharge("1 349,99 UAH", "3 months"), substring = true)

            onNodeWithTag(PaywallScreenTags.SUBSCRIBE).performScrollTo().assertIsDisplayed()
            onNodeWithTag(PaywallScreenTags.SUBSCRIPTION_TERMS).performScrollTo().assertIsDisplayed()
        }

    @Test
    fun tappingAPlanSelectsItAndUpdatesLegalText() =
        runComposeUiTest {
            setContent { AppTheme { PaywallScreen(onClose = {}) } }

            onNodeWithTag(PaywallScreenTags.plan("weekly")).performClick()

            onNodeWithTag(PaywallScreenTags.plan("weekly")).assertIsSelected()
            onNodeWithTag(PaywallScreenTags.plan("quarterly")).assertIsNotSelected()
            onNodeWithTag(PaywallScreenTags.LEGAL).assertTextContains(legalCharge("354,99 UAH", "week"), substring = true)
        }

    @Test
    fun closeAndSubscribeAreHoisted() =
        runComposeUiTest {
            var closes = 0
            var subscribedPlan: String? = null
            setContent {
                AppTheme {
                    PaywallScreen(
                        state = data.toUiState(),
                        onPlanSelect = {},
                        onClose = { closes++ },
                        onSubscribe = { subscribedPlan = it },
                    )
                }
            }

            onNodeWithTag(PaywallScreenTags.CLOSE).performClick()
            assertEquals(1, closes)

            onNodeWithTag(PaywallScreenTags.SUBSCRIBE).performScrollTo().performClick()
            assertEquals(data.defaultPlanId, subscribedPlan)
        }

    @Test
    fun swipingThePagerMovesThePageDots() =
        runComposeUiTest {
            setContent { AppTheme { PaywallScreen(onClose = {}) } }

            onNodeWithTag(PaywallScreenTags.PAGE_INDICATOR).assertContentDescriptionEquals("Page 1 of 4")
            onNodeWithTag(PaywallScreenTags.PAGER).performTouchInput { swipeLeft() }
            waitForIdle()

            onNodeWithText("Compatibility readings").assertIsDisplayed()
            onNodeWithTag(PaywallScreenTags.PAGE_INDICATOR).assertContentDescriptionEquals("Page 2 of 4")
        }
}
