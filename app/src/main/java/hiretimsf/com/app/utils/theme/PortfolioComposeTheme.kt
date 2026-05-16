package hiretimsf.com.app.utils.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import hiretimsf.com.app.R

@Composable
fun PortfolioComposeTheme(content: @Composable () -> Unit) {
    val primary = colorResource(R.color.colorPrimary)
    val onPrimary = colorResource(R.color.colorOnPrimary)
    val surface = colorResource(R.color.colorSurface)
    val elevatedSurface = colorResource(R.color.colorElevatedSurface)
    val onPrimarySurface = colorResource(R.color.colorOnPrimarySurface)
    val surfaceVariant = colorResource(R.color.colorHeaderBackground)
    val onSurfaceVariant = colorResource(R.color.colorHeaderTitle)
    val outline = colorResource(R.color.colorBorder)

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = primary,
            onPrimary = onPrimary,
            secondary = primary,
            onSecondary = onPrimary,
            background = surface,
            onBackground = onPrimarySurface,
            surface = surface,
            onSurface = onPrimarySurface,
            surfaceContainerLowest = surface,
            surfaceContainerLow = elevatedSurface,
            surfaceContainer = elevatedSurface,
            surfaceContainerHigh = surfaceVariant,
            surfaceContainerHighest = surfaceVariant,
            surfaceVariant = surfaceVariant,
            onSurfaceVariant = onSurfaceVariant,
            outline = outline,
            outlineVariant = outline,
            inverseSurface = onPrimarySurface,
            inverseOnSurface = surface,
            inversePrimary = primary,
        ),
        content = content,
    )
}
