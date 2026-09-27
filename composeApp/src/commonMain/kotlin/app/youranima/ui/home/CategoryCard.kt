package app.youranima.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.youranima.data.home.CategoryForecast
import app.youranima.data.home.ForecastCategory
import app.youranima.resources.Res
import app.youranima.resources.home_category_career
import app.youranima.resources.home_category_health
import app.youranima.resources.home_category_love
import app.youranima.resources.home_cd_locked
import app.youranima.resources.home_ic_category_career
import app.youranima.resources.home_ic_category_health
import app.youranima.resources.home_ic_category_love
import app.youranima.resources.home_ic_lock
import app.youranima.ui.theme.appColors
import app.youranima.ui.theme.appTypography
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private val CardWidth = 144.dp
private val IllustrationSize = 48.dp
private val LockBadgeSize = 28.dp

internal val ForecastCategory.title: StringResource
    get() =
        when (this) {
            ForecastCategory.Career -> Res.string.home_category_career
            ForecastCategory.Love -> Res.string.home_category_love
            ForecastCategory.Health -> Res.string.home_category_health
        }

/** Same colours as the Focus & Mood rings. */
internal val ForecastCategory.tint: Color
    @Composable @ReadOnlyComposable
    get() =
        when (this) {
            ForecastCategory.Career -> MaterialTheme.appColors.primary
            ForecastCategory.Love -> MaterialTheme.appColors.accentOrange
            ForecastCategory.Health -> MaterialTheme.appColors.accentTeal
        }

internal val ForecastCategory.illustration: DrawableResource
    get() =
        when (this) {
            ForecastCategory.Career -> Res.drawable.home_ic_category_career
            ForecastCategory.Love -> Res.drawable.home_ic_category_love
            ForecastCategory.Health -> Res.drawable.home_ic_category_health
        }

/** Horizontal row of category forecast cards; spans the full screen width. */
@Composable
fun CategoryRow(
    categories: List<CategoryForecast>,
    onCategoryClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier.testTag(HomeScreenTags.CATEGORIES),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(categories, key = { it.id }) { forecast ->
            CategoryCard(forecast = forecast, onClick = { onCategoryClick(forecast.id) })
        }
    }
}

@Composable
fun CategoryCard(
    forecast: CategoryForecast,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val colors = MaterialTheme.appColors
    Column(
        modifier =
            modifier
                .width(CardWidth)
                .testTag(HomeScreenTags.categoryCard(forecast.id))
                .homeCard(PaddingValues())
                .clip(MaterialTheme.shapes.large)
                .clickable(onClick = onClick)
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Icon(
                painter = painterResource(forecast.category.illustration),
                contentDescription = null,
                tint = forecast.category.tint,
                modifier = Modifier.size(IllustrationSize),
            )
            if (forecast.isLocked) {
                Box(
                    modifier =
                        Modifier
                            .size(LockBadgeSize)
                            .background(colors.glassFill, CircleShape)
                            .border(1.dp, colors.glassBorder, CircleShape)
                            .testTag(HomeScreenTags.lockBadge(forecast.id)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.home_ic_lock),
                        contentDescription = stringResource(Res.string.home_cd_locked),
                        tint = colors.onSurface,
                        modifier = Modifier.size(14.dp),
                    )
                }
            }
        }
        Column {
            Text(
                text = stringResource(forecast.category.title),
                style = MaterialTheme.appTypography.cardTitle,
                color = colors.onSurface,
                maxLines = 1,
            )
            Text(
                text = forecast.preview,
                style = MaterialTheme.appTypography.body,
                color = colors.accentLavender,
                minLines = 2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Preview
@Composable
private fun CategoryRowPreview() {
    HomePreview { CategoryRow(categories = PreviewHomeUiState.categories, onCategoryClick = {}) }
}

@Preview
@Composable
private fun CategoryCardUnlockedPreview() {
    HomePreview { CategoryCard(forecast = PreviewHomeUiState.categories.first().copy(isLocked = false)) }
}
