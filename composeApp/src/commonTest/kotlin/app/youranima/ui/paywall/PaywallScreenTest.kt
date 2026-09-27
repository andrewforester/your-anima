package app.youranima.ui.paywall

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.runComposeUiTest
import app.youranima.data.paywall.MockPaywallRepository
import app.youranima.ui.theme.AppTheme
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class PaywallScreenTest {
    private val data = MockPaywallRepository.paywallData()

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
            onNodeWithTag(PaywallScreenTags.LEGAL).assertTextContains("charged 1 349,99 UAH every 3 months", substring = true)

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
            onNodeWithTag(PaywallScreenTags.LEGAL).assertTextContains("charged 354,99 UAH every week", substring = true)
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
}
