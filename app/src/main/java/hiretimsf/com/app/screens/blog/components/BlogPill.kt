package hiretimsf.com.app.screens.blog.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hiretimsf.com.app.R

private val blogPillFontFamily = FontFamily(Font(R.font.questrial))

@Composable
fun BlogPill(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        color = colorResource(R.color.colorPrimary),
        fontFamily = blogPillFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
            .background(colorResource(R.color.colorPrimary).copy(alpha = 0.12f), CircleShape)
            .padding(horizontal = 10.dp, vertical = 6.dp),
    )
}
