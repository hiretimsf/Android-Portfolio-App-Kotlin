package hiretimsf.com.app.screens.portfolio

import android.widget.ImageView
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import hiretimsf.com.app.R
import hiretimsf.com.app.repository.database.model.portfolio.PortfolioModel
import hiretimsf.com.app.utils.adapters.bindingAdapters.loadImage
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private val questrial = FontFamily(Font(R.font.questrial))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioComposeScreen(
    items: LazyPagingItems<PortfolioModel>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onPortfolioClick: (PortfolioModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier.fillMaxSize(),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            items(
                count = items.itemCount,
                key = items.itemKey { it.id },
            ) { index ->
                items[index]?.let { item ->
                    PortfolioCard(
                        item = item,
                        onClick = { onPortfolioClick(item) },
                    )
                }
            }
        }
    }
}

@Composable
private fun PortfolioCard(
    item: PortfolioModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp),
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.colorSurface)),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            RemoteImage(
                url = item.coverImage,
                contentDescription = item.imageDescription,
                modifier = Modifier
                    .width(80.dp)
                    .height(100.dp),
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp)
                    .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 10.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RemoteImage(
                        url = item.logo,
                        contentDescription = item.logoDescription,
                        modifier = Modifier
                            .size(15.dp)
                            .clip(CircleShape),
                    )
                    Spacer(Modifier.width(5.dp))
                    Text(
                        text = item.subTitle,
                        color = colorResource(R.color.colorOnPrimarySurface),
                        fontFamily = questrial,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                    )
                }
                Text(
                    text = item.text,
                    color = colorResource(R.color.colorOnSurface),
                    fontFamily = questrial,
                    fontSize = 14.sp,
                    lineHeight = 19.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 5.dp),
                )
                MetadataRow(
                    icon = R.drawable.ic_date,
                    text = formatDateRange(item.dateFrom, item.dateTo),
                )
            }
        }
    }
}

@Composable
private fun MetadataRow(
    icon: Int,
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = colorResource(R.color.colorOnSurface),
            modifier = Modifier.size(10.dp),
        )
        Spacer(Modifier.width(5.dp))
        Text(
            text = text,
            color = colorResource(R.color.colorOnSurface),
            fontFamily = questrial,
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun RemoteImage(
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
    val diffYear = diff / 12
    val diffMonth = diff % 12
    val duration = if (diffYear > 0) "$diffYear.$diffMonth year(s)" else "$diffMonth month(s)"
    return "$startText - $endText | $duration"
}

@Preview(showBackground = true)
@Composable
private fun PortfolioCardPreview() {
    Box(Modifier.fillMaxWidth()) {
        PortfolioCard(
            item = PortfolioModel(
                id = "preview",
                ownerId = "owner",
                title = "Portfolio App",
                subTitle = "Portfolio App 2.0",
                logo = "",
                logoDescription = "Logo",
                coverImage = "",
                imageDescription = "Cover",
                text = "Applied best practices from the Android community and shared knowledge through a working portfolio app.",
                info = "",
                dateFrom = Date(1541113200000),
                dateTo = Date(1565161200000),
                header = "Android Apps",
                categoryType = 1,
                videoUrl = null,
                linkToShare = null,
                order = 1,
            ),
            onClick = {},
        )
    }
}
