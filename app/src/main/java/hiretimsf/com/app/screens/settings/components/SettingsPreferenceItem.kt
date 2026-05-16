package hiretimsf.com.app.screens.settings.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hiretimsf.com.app.R

@Composable
fun SettingsPreferenceItem(
    @DrawableRes icon: Int,
    title: String,
    summary: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Dp = 22.dp,
    iconTint: androidx.compose.ui.graphics.Color? = colorResource(R.color.colorHeaderTitle),
    showDivider: Boolean = false,
    showIconContainer: Boolean = true,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(74.dp)
                .clickable(onClick = onClick)
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconSlot(
                icon = icon,
                iconSize = iconSize,
                iconTint = iconTint,
                showContainer = showIconContainer,
                modifier = Modifier.size(44.dp),
            )
            Spacer(Modifier.size(14.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = title,
                    color = colorResource(R.color.colorOnPrimarySurface),
                    fontFamily = settingsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = summary,
                    color = colorResource(R.color.colorOnSurface),
                    fontFamily = settingsFontFamily,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Icon(
                painter = painterResource(R.drawable.ic_chevron_right),
                contentDescription = null,
                tint = colorResource(R.color.colorHeaderTitle),
                modifier = Modifier.size(18.dp),
            )
        }
        if (showDivider) {
            HorizontalDivider(
                color = colorResource(R.color.colorBorder),
            )
        }
    }
}

@Composable
private fun IconSlot(
    @DrawableRes icon: Int,
    iconSize: Dp,
    iconTint: androidx.compose.ui.graphics.Color?,
    showContainer: Boolean,
    modifier: Modifier = Modifier,
) {
    if (showContainer) {
        Surface(
            shape = CircleShape,
            color = colorResource(R.color.colorSurface),
            modifier = modifier,
        ) {
            IconSlotContent(icon = icon, iconSize = iconSize, iconTint = iconTint)
        }
    } else {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center,
        ) {
            IconSlotContent(icon = icon, iconSize = iconSize, iconTint = iconTint)
        }
    }
}

@Composable
private fun IconSlotContent(
    @DrawableRes icon: Int,
    iconSize: Dp,
    iconTint: androidx.compose.ui.graphics.Color?,
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (iconTint == null) {
            Image(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier
                    .size(iconSize)
                    .clip(CircleShape),
            )
        } else {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(iconSize),
            )
        }
    }
}
