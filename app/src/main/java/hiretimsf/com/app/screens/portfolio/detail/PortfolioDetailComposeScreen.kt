package hiretimsf.com.app.screens.portfolio.detail

import android.widget.ImageView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import hiretimsf.com.app.R
import hiretimsf.com.app.repository.database.model.button.ButtonModel
import hiretimsf.com.app.repository.database.model.category.CategoryModel
import hiretimsf.com.app.repository.database.model.portfolio.PortfolioModel
import hiretimsf.com.app.repository.database.model.screenshot.ScreenShotModel
import hiretimsf.com.app.screens.shared.components.PrimaryGradientButton
import hiretimsf.com.app.utils.images.loadRemoteImage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val portfolioDetailFontFamily = FontFamily(Font(R.font.questrial))

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
fun PortfolioDetailComposeScreen(
    portfolio: PortfolioModel?,
    buttons: List<ButtonModel>,
    categories: List<CategoryModel>,
    screenshots: List<ScreenShotModel>,
    onButtonClick: (String) -> Unit,
    onVideoClick: (String) -> Unit,
    onScreenshotClick: (ScreenShotModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (portfolio == null) {
        Spacer(modifier = modifier.fillMaxSize())
        return
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.colorSurface)),
    ) {
        item {
            RemoteDetailImage(
                url = portfolio.coverImage,
                contentDescription = portfolio.imageDescription,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .height(220.dp)
                    .clickable(enabled = portfolio.videoUrl != null) {
                        portfolio.videoUrl?.let(onVideoClick)
                    },
            )
        }
        item {
            Column(Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (portfolio.logo.isNotBlank()) {
                        RemoteDetailImage(
                            url = portfolio.logo,
                            contentDescription = portfolio.logoDescription,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape),
                        )
                        Spacer(Modifier.width(10.dp))
                    }
                    Column(Modifier.weight(1f)) {
                        Text(
                            text = portfolio.subTitle,
                            color = colorResource(R.color.colorOnPrimarySurface),
                            fontFamily = portfolioDetailFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            lineHeight = 28.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            text = formatDateRange(portfolio.dateFrom, portfolio.dateTo),
                            color = colorResource(R.color.colorHeaderTitle),
                            fontFamily = portfolioDetailFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(top = 10.dp),
                        )
                    }
                }
                Text(
                    text = portfolio.text,
                    color = colorResource(R.color.colorOnSurface),
                    fontFamily = portfolioDetailFontFamily,
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    modifier = Modifier.padding(top = 16.dp),
                )
                Text(
                    text = portfolio.info,
                    color = colorResource(R.color.colorOnSurface),
                    fontFamily = portfolioDetailFontFamily,
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    modifier = Modifier.padding(top = 12.dp),
                )
            }
        }
        item {
            FlowRow(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                categories.forEach { category ->
                    AssistChip(
                        onClick = {},
                        label = { Text(category.title, fontFamily = portfolioDetailFontFamily) },
                    )
                }
            }
        }
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                buttons.forEach { button ->
                    if (button.title != "Portfolio") {
                        ProjectActionButton(
                            button = button,
                            onClick = { onButtonClick(button.url) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProjectActionButton(
    button: ButtonModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val title = if (button.title == "Website") "Live Demo" else button.title
    if (button.type == "outline") {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier.fillMaxWidth(),
        ) {
            Text(title, fontFamily = portfolioDetailFontFamily)
        }
    } else {
        PrimaryGradientButton(
            onClick = onClick,
            modifier = modifier.fillMaxWidth(),
        ) {
            Text(title, fontFamily = portfolioDetailFontFamily)
        }
    }
}

@Composable
private fun RemoteDetailImage(
    url: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            ImageView(context).apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
            }
        },
        update = { imageView ->
            imageView.contentDescription = contentDescription
            imageView.loadRemoteImage(url)
        },
    )
}

private fun formatDateRange(dateFrom: Date?, dateTo: Date?): String {
    if (dateFrom == null || dateTo == null) return ""
    val outputFormat = SimpleDateFormat("MMM yyyy", Locale.US)
    if (dateFrom.compareTo(dateTo) == 0) return outputFormat.format(dateFrom)
    val startText = outputFormat.format(dateFrom)
    val endText = outputFormat.format(dateTo)
    return if (startText == endText) startText else "$startText - $endText"
}
