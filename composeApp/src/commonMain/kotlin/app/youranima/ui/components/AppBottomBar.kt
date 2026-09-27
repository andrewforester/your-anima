package app.youranima.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import app.youranima.data.navigation.NavBadges
import app.youranima.resources.Res
import app.youranima.resources.home_ic_book
import app.youranima.resources.home_ic_heart
import app.youranima.resources.home_ic_message_circle
import app.youranima.resources.home_ic_star
import app.youranima.resources.home_ic_user
import app.youranima.resources.nav_badge_free
import app.youranima.resources.nav_chatroom
import app.youranima.resources.nav_compatibility
import app.youranima.resources.nav_psychics
import app.youranima.resources.nav_readings
import app.youranima.resources.nav_today
import app.youranima.ui.theme.AppTheme
import app.youranima.ui.theme.appColors
import app.youranima.ui.theme.appTypography
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/** Bottom padding of the bar in the design (home indicator area); the real inset wins if it is larger. */
private val BarBottomPadding = 24.dp

/** Bar content height: 84 (design) - 12 top - 24 bottom. */
private val NavItemMinHeight = 48.dp

/** Shared bottom nav bar shown by [app.youranima.ui.navigation.AppShell] under every tab's content. */
@Composable
fun AppBottomBar(
    selected: AppTab,
    badges: NavBadges,
    onSelect: (AppTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.appColors
    Column(modifier = modifier.fillMaxWidth().background(colors.backgroundDeep).testTag(AppBottomBarTags.BAR)) {
        Spacer(Modifier.fillMaxWidth().height(1.dp).background(colors.outline))
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars.union(WindowInsets(bottom = BarBottomPadding)))
                    .padding(top = 11.dp) // + 1dp top border = 12
                    .selectableGroup(),
        ) {
            AppTab.entries.forEach { tab ->
                NavItem(
                    tab = tab,
                    selected = tab == selected,
                    badges = badges,
                    onClick = { onSelect(tab) },
                    modifier = Modifier.weight(1f).testTag(AppBottomBarTags.navItem(tab)),
                )
            }
        }
    }
}

@Composable
private fun NavItem(
    tab: AppTab,
    selected: Boolean,
    badges: NavBadges,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.appColors
    val tint = if (selected) colors.primary else colors.onSurfaceMuted
    Column(
        modifier = modifier.heightIn(min = NavItemMinHeight).selectable(selected = selected, onClick = onClick, role = Role.Tab),
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box {
            Icon(
                painter = painterResource(tab.icon),
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(20.dp),
            )
            val badge = tab.badge(badges)
            if (badge != null) {
                Box(Modifier.badgeAnchor()) { badge() }
            }
        }
        Text(
            text = stringResource(tab.label),
            style = MaterialTheme.appTypography.caption,
            fontWeight = if (selected) FontWeight.SemiBold else null,
            color = tint,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

/** Places the badge at (+14, -6) from the icon's top-left without affecting the icon's size. */
private fun Modifier.badgeAnchor() =
    layout { measurable, _ ->
        val placeable = measurable.measure(Constraints())
        layout(0, 0) { placeable.place(14.dp.roundToPx(), (-6).dp.roundToPx()) }
    }

private fun AppTab.badge(badges: NavBadges): (@Composable () -> Unit)? =
    when {
        this == AppTab.Psychics && badges.psychicsFree -> {
            { FreeBadge() }
        }

        this == AppTab.Chatroom && badges.unreadChats > 0 -> {
            { CountBadge(badges.unreadChats) }
        }

        else -> {
            null
        }
    }

@Composable
private fun FreeBadge() {
    Text(
        text = stringResource(Res.string.nav_badge_free).uppercase(),
        style = MaterialTheme.appTypography.badge,
        color = MaterialTheme.appColors.onSurface,
        modifier =
            Modifier
                .background(MaterialTheme.appColors.accentOrange, RoundedCornerShape(6.dp))
                .padding(horizontal = 4.dp, vertical = 2.dp),
    )
}

@Composable
private fun CountBadge(count: Int) {
    Box(
        modifier = Modifier.widthIn(min = 14.dp).height(14.dp).background(MaterialTheme.appColors.accentOrange, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = count.toString(),
            style = MaterialTheme.appTypography.badge.copy(fontSize = MaterialTheme.appTypography.badge.fontSize * 9 / 8),
            color = MaterialTheme.appColors.onSurface,
        )
    }
}

private val AppTab.icon: DrawableResource
    get() =
        when (this) {
            AppTab.Today -> Res.drawable.home_ic_star
            AppTab.Psychics -> Res.drawable.home_ic_user
            AppTab.Compatibility -> Res.drawable.home_ic_heart
            AppTab.Chatroom -> Res.drawable.home_ic_message_circle
            AppTab.Readings -> Res.drawable.home_ic_book
        }

private val AppTab.label: StringResource
    get() =
        when (this) {
            AppTab.Today -> Res.string.nav_today
            AppTab.Psychics -> Res.string.nav_psychics
            AppTab.Compatibility -> Res.string.nav_compatibility
            AppTab.Chatroom -> Res.string.nav_chatroom
            AppTab.Readings -> Res.string.nav_readings
        }

@Preview
@Composable
private fun AppBottomBarPreview() {
    AppTheme {
        AppBottomBar(selected = AppTab.Today, badges = NavBadges(psychicsFree = true, unreadChats = 3), onSelect = {})
    }
}
