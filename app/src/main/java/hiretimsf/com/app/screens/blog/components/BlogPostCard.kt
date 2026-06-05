package hiretimsf.com.app.screens.blog.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hiretimsf.com.app.R
import hiretimsf.com.app.screens.blog.BlogPost
import hiretimsf.com.app.screens.shared.components.PrimaryGradientButton
import hiretimsf.com.app.screens.shared.components.RemoteImage
import hiretimsf.com.app.screens.shared.components.primaryActionContentColor

private val blogCardFontFamily = FontFamily(Font(R.font.questrial))

@Composable
fun BlogPostCard(
    post: BlogPost,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.colorElevatedSurface)),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            RemoteImage(
                url = post.coverImageUrl,
                contentDescription = post.imageAlt,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(178.dp)
                    .background(colorResource(R.color.colorBorder)),
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                BlogPill(
                    text = post.category,
                    modifier = Modifier.weight(1f, fill = false),
                )
                BlogMetaText(text = post.date)
                BlogMetaText(text = "|", alpha = 0.7f)
                BlogMetaText(text = post.readTime)
            }
            Text(
                text = post.title,
                color = colorResource(R.color.colorOnPrimarySurface),
                fontFamily = blogCardFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                lineHeight = 24.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 14.dp),
            )
            Text(
                text = post.excerpt,
                color = colorResource(R.color.colorOnSurface),
                fontFamily = blogCardFontFamily,
                fontSize = 15.sp,
                lineHeight = 21.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp),
            )
            PrimaryGradientButton(
                onClick = onClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text(
                    text = "Read More",
                    color = primaryActionContentColor(),
                    fontFamily = blogCardFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                )
                Spacer(Modifier.width(8.dp))
                Icon(
                    painter = painterResource(R.drawable.ic_chevron_right),
                    contentDescription = null,
                    tint = primaryActionContentColor(),
                    modifier = Modifier.size(16.dp),
                )
            }
        }
    }
}

@Composable
private fun BlogMetaText(
    text: String,
    alpha: Float = 1f,
) {
    Text(
        text = text,
        color = colorResource(R.color.colorOnSurface).copy(alpha = alpha),
        fontFamily = blogCardFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}
