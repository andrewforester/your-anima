package app.youranima.ui.psychics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.youranima.resources.Res
import app.youranima.resources.psychics_cd_rating
import app.youranima.resources.psychics_ic_star_filled
import app.youranima.ui.theme.appColors
import app.youranima.ui.theme.appTypography
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.roundToInt

private const val MAX_STARS = 5

/** Five stars (rating rounded to whole stars), a thin divider and the review count. Read out as one phrase. */
@Composable
fun RatingRow(
    rating: Double,
    reviewCount: Int,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.appColors
    val stars = rating.roundToInt().coerceIn(0, MAX_STARS)
    val description = stringResource(Res.string.psychics_cd_rating, stars, reviewCount)
    Row(
        modifier = modifier.clearAndSetSemantics { contentDescription = description },
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
            repeat(MAX_STARS) { index ->
                Icon(
                    painter = painterResource(Res.drawable.psychics_ic_star_filled),
                    contentDescription = null,
                    tint = if (index < stars) colors.accentGold else colors.outline,
                    modifier = Modifier.size(12.dp),
                )
            }
        }
        Box(Modifier.width(1.dp).height(10.dp).background(colors.outline))
        Text(text = reviewCount.toString(), style = MaterialTheme.appTypography.caption, color = colors.accentLavender)
    }
}

@Preview
@Composable
private fun RatingRowPreview() {
    PsychicsPreview { RatingRow(rating = 4.0, reviewCount = 436) }
}
