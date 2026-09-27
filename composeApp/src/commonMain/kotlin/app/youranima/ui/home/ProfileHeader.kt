package app.youranima.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
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
import app.youranima.ui.theme.appColors
import app.youranima.ui.theme.appTypography
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ProfileHeader(
    user: UserProfile,
    modifier: Modifier = Modifier,
    onBirthChartClick: () -> Unit = {},
) {
    val colors = MaterialTheme.appColors
    val typography = MaterialTheme.appTypography
    Column(
        modifier = modifier.fillMaxWidth().padding(start = 24.dp, top = 12.dp, end = 24.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(Res.drawable.home_avatar_character),
            contentDescription = stringResource(Res.string.home_avatar),
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .size(100.dp)
                    .padding(4.dp)
                    .background(colors.background, CircleShape)
                    .clip(CircleShape),
        )
        Text(
            text = user.name,
            style = typography.name,
            color = colors.onSurface,
            modifier = Modifier.testTag(HomeScreenTags.USER_NAME),
        )
        ZodiacRow(user)
        BirthChartPill(onClick = onBirthChartClick)
    }
}

@Composable
private fun ZodiacRow(user: UserProfile) {
    val colors = MaterialTheme.appColors
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
        ZodiacItem(user.sun) {
            SymbolIcon(painterResource(Res.drawable.home_ic_sun), stringResource(Res.string.home_sun_symbol), colors.accentGold)
        }
        ZodiacItem(user.moon) {
            SymbolIcon(painterResource(Res.drawable.home_ic_moon), stringResource(Res.string.home_moon_symbol), colors.accentPink)
        }
        ZodiacItem(user.ascendant) { AscendantSymbol() }
    }
}

@Composable
private fun ZodiacItem(
    sign: ZodiacSign,
    symbol: @Composable () -> Unit,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
        symbol()
        Text(
            text = stringResource(sign.label),
            style = MaterialTheme.appTypography.body,
            color = MaterialTheme.appColors.accentLavender,
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
                withStyle(SpanStyle(fontSize = 0.6.em, baselineShift = BaselineShift.Superscript)) {
                    append(symbol.drop(1))
                }
            },
        style = MaterialTheme.appTypography.body.copy(fontSize = MaterialTheme.appTypography.caption.fontSize),
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.appColors.primary,
    )
}

@Composable
private fun BirthChartPill(onClick: () -> Unit) {
    val colors = MaterialTheme.appColors
    val shape = RoundedCornerShape(20.dp)
    Row(
        modifier =
            Modifier
                .clip(shape)
                .background(colors.glassFill, shape)
                .border(BorderStroke(1.dp, colors.glassBorder), shape)
                .clickable(onClick = onClick)
                .padding(PaddingValues(horizontal = 16.dp, vertical = 8.dp))
                .testTag(HomeScreenTags.BIRTH_CHART),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(Res.drawable.home_ic_crystal_ball),
            contentDescription = null,
            modifier = Modifier.size(14.dp),
        )
        Text(
            text = stringResource(Res.string.home_birth_chart),
            style = MaterialTheme.appTypography.pill,
            color = colors.onSurface,
        )
    }
}

@Preview
@Composable
private fun ProfileHeaderPreview() {
    HomePreview { ProfileHeader(user = PreviewHomeUiState.user) }
}
