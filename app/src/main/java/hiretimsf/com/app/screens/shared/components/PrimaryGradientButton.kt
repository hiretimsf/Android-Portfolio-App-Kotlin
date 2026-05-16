package hiretimsf.com.app.screens.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun PrimaryGradientButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = CircleShape,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    elevation: Dp = 1.dp,
    content: @Composable RowScope.() -> Unit,
) {
    val contentColor = primaryActionContentColor()
    val enabledAlpha = if (enabled) 1f else 0.5f

    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Row(
            modifier = modifier
                .shadow(elevation, shape)
                .clip(shape)
                .background(primaryActionBrush())
                .alpha(enabledAlpha)
                .clickable(
                    enabled = enabled,
                    role = Role.Button,
                    onClick = onClick,
                )
                .defaultMinSize(
                    minWidth = ButtonDefaults.MinWidth,
                    minHeight = ButtonDefaults.MinHeight,
                )
                .padding(contentPadding),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
    }
}

@Composable
fun primaryActionBrush(): Brush {
    return Brush.linearGradient(
        colors = if (isSystemInDarkTheme()) {
            listOf(
                Color(0xFFEA580C),
                Color(0xFFF59E0B),
            )
        } else {
            listOf(
                Color(0xFF3B82F6),
                Color(0xFF06B6D4),
            )
        },
    )
}

@Composable
fun primaryActionContentColor(): Color = Color.White
