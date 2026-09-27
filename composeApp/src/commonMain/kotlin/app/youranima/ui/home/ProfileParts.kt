package app.youranima.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import app.youranima.data.home.UserProfile
import app.youranima.data.home.ZodiacSign
import app.youranima.resources.Res
import app.youranima.resources.home_ascendant_symbol
import app.youranima.resources.home_avatar
import app.youranima.resources.home_avatar_character
import app.youranima.resources.home_birth_chart
import app.youranima.resources.home_ic_crystal_ball
import app.youranima.resources.home_ic_moon
import app.youranima.resources.home_ic_sun
import app.youranima.resources.home_moon_symbol
import app.youranima.resources.home_sun_symbol
import app.youranima.ui.components.GlassPill
import app.youranima.ui.theme.appColors
import app.youranima.ui.theme.appTypography
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

// Pieces of the profile header (Figma item 4). [CollapsingProfileHeader] places and scales them.

/** Avatar at its expanded size; the header scales it down to [TopBarButtonSize] when collapsed. */
@Composable
fun ProfileAvatar(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(Res.drawable.home_avatar_character),
        contentDescription = stringResource(Res.string.home_avatar),
        contentScale = ContentScale.Crop,
        modifier =
            modifier
                .size(ProfileAvatarSize)
                .padding(4.dp)
                .background(MaterialTheme.appColors.background, CircleShape)
                .clip(CircleShape),
    )
}

@Composable
fun ProfileName(
    name: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = name,
        style = MaterialTheme.appTypography.name,
        color = MaterialTheme.appColors.onSurface,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier.testTag(HomeScreenTags.USER_NAME),
    )
}

/** ☉ sun · ☽ moon · Asc ascendant, one line. [labelColor] is read at draw time, so it can animate without recomposition. */
@Composable
fun ZodiacRow(
    user: UserProfile,
    modifier: Modifier = Modifier,
    labelColor: ColorProducer? = null,
) {
    val colors = MaterialTheme.appColors
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ZodiacItem(user.sun, labelColor) {
            SymbolIcon(painterResource(Res.drawable.home_ic_sun), stringResource(Res.string.home_sun_symbol), colors.accentGold)
        }
        ZodiacItem(user.moon, labelColor) {
            SymbolIcon(painterResource(Res.drawable.home_ic_moon), stringResource(Res.string.home_moon_symbol), colors.accentPink)
        }
        ZodiacItem(user.ascendant, labelColor) { AscendantSymbol() }
    }
}

@Composable
private fun ZodiacItem(
    sign: ZodiacSign,
    labelColor: ColorProducer?,
    symbol: @Composable () -> Unit,
) {
    val style = MaterialTheme.appTypography.body.copy(color = MaterialTheme.appColors.accentLavender)
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
        symbol()
        BasicText(
            text = stringResource(sign.label),
            style = style,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = labelColor,
        )
    }
}

@Composable
private fun SymbolIcon(
    painter: Painter,
    description: String,
    tint: Color,
) {
    Icon(painter = painter, contentDescription = description, tint = tint, modifier = Modifier.size(12.dp))
}

/** "Aˢᶜ": Geist has no superscript letters, so the tail is drawn as a superscript span. */
@Composable
private fun AscendantSymbol() {
    val symbol = stringResource(Res.string.home_ascendant_symbol)
    Text(
        text =
            buildAnnotatedString {
                append(symbol.take(1))
                withStyle(SpanStyle(fontSize = 0.7.em, baselineShift = BaselineShift.Superscript)) {
                    append(symbol.drop(1))
                }
            },
        style = MaterialTheme.appTypography.body.copy(fontSize = MaterialTheme.appTypography.caption.fontSize),
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.appColors.primary,
    )
}

@Composable
fun BirthChartPill(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    GlassPill(
        label = stringResource(Res.string.home_birth_chart),
        onClick = onClick,
        modifier = modifier.testTag(HomeScreenTags.BIRTH_CHART),
        icon = {
            Image(
                painter = painterResource(Res.drawable.home_ic_crystal_ball),
                contentDescription = null,
                modifier = Modifier.size(14.dp),
            )
        },
    )
}

internal val ProfileAvatarSize = 100.dp

@Preview
@Composable
private fun ProfilePartsPreview() {
    HomePreview {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            ProfileAvatar()
            ProfileName(PreviewHomeUiState.user.name)
            ZodiacRow(PreviewHomeUiState.user)
            BirthChartPill(onClick = {})
        }
    }
}
