package app.youranima.ui.navigation

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.runComposeUiTest
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import app.youranima.data.home.MockHomeRepository
import app.youranima.ui.components.AppBottomBarTags
import app.youranima.ui.components.AppTab
import app.youranima.ui.home.HomeScreenTags
import app.youranima.ui.paywall.PaywallScreenTags
import app.youranima.ui.theme.AppTheme
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class PaywallNavigationTest {
    private val lockedCard =
        HomeScreenTags.categoryCard(
            MockHomeRepository
                .homeData()
                .categories
                .first { it.isLocked }
                .id,
        )

    @Test
    fun backPressClosesPaywallAndKeepsHomeScrollPosition() =
        runComposeUiTest {
            lateinit var navController: NavHostController
            setContent {
                navController = rememberNavController()
                AppTheme { AppShell(navController = navController) }
            }

            // Scroll past the categories so the locked card is fully on screen (a tap doesn't move it).
            onNodeWithTag(HomeScreenTags.TIP).performScrollTo()
            val cardTop = onNodeWithTag(lockedCard).getUnclippedBoundsInRoot().top
            onNodeWithTag(lockedCard).performClick()

            onNodeWithTag(PaywallScreenTags.SCREEN).assertIsDisplayed()
            onNodeWithTag(AppBottomBarTags.BAR).assertDoesNotExist()
            onNodeWithTag(HomeScreenTags.SCREEN).assertDoesNotExist()
            assertEquals(AppRoutes.PAYWALL, navController.currentDestination?.route)

            runOnIdle { navController.popBackStack() }

            onNodeWithTag(PaywallScreenTags.SCREEN).assertDoesNotExist()
            onNodeWithTag(AppBottomBarTags.navItem(AppTab.Today)).assertIsSelected()
            assertEquals(cardTop, onNodeWithTag(lockedCard).getUnclippedBoundsInRoot().top)
        }

    @Test
    fun closeReturnsToTheSameTabWithoutEmptyingTheBackStack() =
        runComposeUiTest {
            lateinit var navController: NavHostController
            setContent {
                navController = rememberNavController()
                AppTheme { AppShell(navController = navController) }
            }

            onNodeWithTag(lockedCard).performScrollTo().performClick()
            onNodeWithTag(PaywallScreenTags.CLOSE).performClick()

            onNodeWithTag(HomeScreenTags.SCREEN).assertIsDisplayed()
            onNodeWithTag(AppBottomBarTags.BAR).assertIsDisplayed()
            assertEquals(AppRoutes.MAIN, navController.currentDestination?.route)
        }

    @Test
    fun closeGoesThroughTheHookOnceWhenOneIsGiven() =
        runComposeUiTest {
            lateinit var navController: NavHostController
            var closeCalls = 0
            setContent {
                navController = rememberNavController()
                AppTheme { AppShell(navController = navController, onCloseScreen = { closeCalls++ }) }
            }

            onNodeWithTag(lockedCard).performScrollTo().performClick()
            onNodeWithTag(PaywallScreenTags.CLOSE).performClick()
            onNodeWithTag(PaywallScreenTags.CLOSE).performClick()

            // The hook (web: history.back()) owns the pop; X itself leaves the back stack alone.
            assertEquals(1, closeCalls)
            assertEquals(AppRoutes.PAYWALL, navController.currentDestination?.route)
        }
}
