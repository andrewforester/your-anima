package app.youranima.ui.compatibility

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.youranima.resources.Res
import app.youranima.resources.compatibility_add_partner
import app.youranima.resources.home_ic_plus
import app.youranima.ui.components.ProfileAvatarInset
import app.youranima.ui.theme.AppTheme
import app.youranima.ui.theme.appColors
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private val BorderWidth = 1.5.dp
private val DashLength = 8.dp
private val DashGap = 5.dp
private val AddButtonSize = 24.dp
private val AddGlyphSize = 16.dp

/** Empty Partner circle: `surface` fill, dashed `onSurfaceMuted` border and a white "+" button in the centre. */
@Composable
fun PartnerSlot(
    size: Dp,
    onAddPartner: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.appColors
    Box(
        modifier =
            modifier
                .size(size)
                .padding(ProfileAvatarInset)
                .testTag(CompatibilityScreenTags.PARTNER_SLOT)
                .drawBehind {
                    val stroke = BorderWidth.toPx()
                    drawCircle(colors.surface)
                    drawCircle(
                        color = colors.onSurfaceMuted,
                        radius = (this.size.minDimension - stroke) / 2,
                        style =
                            Stroke(
                                width = stroke,
                                cap = StrokeCap.Round,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(DashLength.toPx(), DashGap.toPx())),
                            ),
                    )
                },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier =
                Modifier
                    .size(AddButtonSize)
                    .clip(CircleShape)
                    .background(colors.onSurface)
                    .clickable(role = Role.Button, onClick = onAddPartner)
                    .testTag(CompatibilityScreenTags.ADD_PARTNER),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(Res.drawable.home_ic_plus),
                contentDescription = stringResource(Res.string.compatibility_add_partner),
                tint = colors.background,
                modifier = Modifier.size(AddGlyphSize),
            )
        }
    }
}

@Preview
@Composable
private fun PartnerSlotPreview() {
    AppTheme {
        Box(Modifier.background(MaterialTheme.appColors.background)) {
            PartnerSlot(size = 140.dp, onAddPartner = {})
        }
    }
}
