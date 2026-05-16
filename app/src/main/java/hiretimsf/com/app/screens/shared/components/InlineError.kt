package hiretimsf.com.app.screens.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hiretimsf.com.app.R

private val inlineErrorFontFamily = FontFamily(Font(R.font.questrial))

@Composable
fun InlineError(
    message: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = message,
        color = colorResource(R.color.colorOnSurface),
        fontFamily = inlineErrorFontFamily,
        fontSize = 14.sp,
        modifier = modifier
            .fillMaxWidth()
            .background(colorResource(R.color.colorHeaderBackground), RoundedCornerShape(8.dp))
            .padding(12.dp),
    )
}
