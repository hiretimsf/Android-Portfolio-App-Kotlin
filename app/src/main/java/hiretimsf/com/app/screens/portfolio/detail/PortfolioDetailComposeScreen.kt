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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import hiretimsf.com.app.R
import hiretimsf.com.app.repository.database.model.button.ButtonModel
import hiretimsf.com.app.repository.database.model.category.CategoryModel
import hiretimsf.com.app.repository.database.model.portfolio.PortfolioModel
import hiretimsf.com.app.repository.database.model.screenshot.ScreenShotModel
import hiretimsf.com.app.utils.adapters.bindingAdapters.loadImage
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private val portfolioDetailFontFamily = FontFamily(Font(R.font.questrial))

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
fun PortfolioDetailComposeScreen(
    portfolio: PortfolioModel?,
    buttons: LazyPagingItems<ButtonModel>,
    categories: LazyPagingItems<CategoryModel>,
    screenshots: LazyPagingItems<ScreenShotModel>,
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
                    RemoteDetailImage(
                        url = portfolio.logo,
                        contentDescription = portfolio.logoDescription,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape),
                    )
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text(
                            text = portfolio.subTitle,
                            color = colorResource(R.color.colorOnPrimarySurface),
                            fontFamily = portfolioDetailFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            text = formatDateRange(portfolio.dateFrom, portfolio.dateTo),
                            color = colorResource(R.color.colorHeaderTitle),
                            fontFamily = portfolioDetailFontFamily,
                            fontSize = 12.sp,
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
                repeat(categories.itemCount) { index ->
                    categories[index]?.let { category ->
                        AssistChip(
                            onClick = {},
                            label = { Text(category.title, fontFamily = portfolioDetailFontFamily) },
                        )
                    }
                }
            }
        }
        item {
            FlowRow(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                repeat(buttons.itemCount) { index ->
                    buttons[index]?.let { button ->
                        if (button.type == "outline") {
                            OutlinedButton(onClick = { onButtonClick(button.url) }) {
                                Text(button.title, fontFamily = portfolioDetailFontFamily)
                            }
                        } else {
                            Button(
                                onClick = { onButtonClick(button.url) },
                                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.colorPrimary)),
                            ) {
                                Text(button.title, fontFamily = portfolioDetailFontFamily)
                            }
                        }
                    }
                }
            }
        }
        item {
            LazyRow(
                modifier = Modifier.padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp),
            ) {
                items(
                    count = screenshots.itemCount,
                    key = screenshots.itemKey { it.id },
                ) { index ->
                    screenshots[index]?.let { screenshot ->
                        Card(
                            modifier = Modifier
                                .width(130.dp)
                                .height(220.dp)
                                .clickable { onScreenshotClick(screenshot) },
                            shape = RoundedCornerShape(5.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        ) {
                            RemoteDetailImage(
                                url = screenshot.url,
                                contentDescription = screenshot.imageDescription,
                                modifier = Modifier.fillMaxSize(),
                            )
                        }
                    }
                }
            }
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
            loadImage(imageView, url)
        },
    )
}

private fun formatDateRange(dateFrom: Date?, dateTo: Date?): String {
    if (dateFrom == null || dateTo == null) return ""
    val outputFormat = SimpleDateFormat("MMM yyyy", Locale.US)
    if (dateFrom.compareTo(dateTo) == 0) return outputFormat.format(dateFrom)
    val startText = outputFormat.format(dateFrom)
    val endText = outputFormat.format(dateTo)
    val start = Calendar.getInstance().apply { time = dateFrom }
    val end = Calendar.getInstance().apply { time = dateTo }
    val diff = (end.get(Calendar.YEAR) - start.get(Calendar.YEAR)) * 12L +
        (end.get(Calendar.MONTH) - start.get(Calendar.MONTH))
    val duration = if (diff / 12 > 0) "${diff / 12}.${diff % 12} year(s)" else "${diff % 12} month(s)"
    return "$startText - $endText | $duration"
}
