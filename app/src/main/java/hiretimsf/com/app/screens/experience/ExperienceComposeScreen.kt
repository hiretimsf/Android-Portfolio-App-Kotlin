package hiretimsf.com.app.screens.experience

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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import hiretimsf.com.app.repository.database.model.experience.ExperienceModel
import hiretimsf.com.app.utils.adapters.bindingAdapters.loadImage
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private val questrial = FontFamily(Font(R.font.questrial))

@Composable
fun ExperienceComposeScreen(
    items: LazyPagingItems<ExperienceModel>,
    onExperienceClick: (ExperienceModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        items(
            count = items.itemCount,
            key = items.itemKey { it.id },
        ) { index ->
            items[index]?.let { item ->
                ExperienceCard(
                    item = item,
                    onClick = { onExperienceClick(item) },
                )
            }
        }
    }
}

@Composable
private fun ExperienceCard(
    item: ExperienceModel,
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RemoteImage(
                url = item.logo,
                contentDescription = item.logoDescription,
                modifier = Modifier
                    .width(80.dp)
                    .height(80.dp),
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp, end = 5.dp, top = 5.dp, bottom = 5.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RemoteImage(
                        url = item.logo,
                        contentDescription = item.logoDescription,
                        modifier = Modifier
                            .size(15.dp)
                            .clip(CircleShape),
                    )
                    Spacer(Modifier.width(5.dp))
                    Text(
                        text = item.title,
                        color = colorResource(R.color.colorOnPrimarySurface),
                        fontFamily = questrial,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                    )
                }
                ExperienceInfoRow(
                    icon = R.drawable.ic_office,
                    text = item.company,
                )
                ExperienceInfoRow(
                    icon = R.drawable.ic_map,
                    text = item.location,
                )
                ExperienceInfoRow(
                    icon = R.drawable.ic_date,
                    text = formatDateRange(item.dateFrom, item.dateTo),
                    fontSize = 12,
                )
            }
        }
    }
}

@Composable
private fun ExperienceInfoRow(
    icon: Int,
    text: String,
    modifier: Modifier = Modifier,
    fontSize: Int = 14,
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
            fontSize = fontSize.sp,
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
private fun ExperienceCardPreview() {
    Box(Modifier.fillMaxWidth()) {
        ExperienceCard(
            item = ExperienceModel(
                id = "preview",
                ownerId = "owner",
                title = "Design Engineer",
                company = "Personal projects",
                info = "Self study through online resources",
                dateFrom = Date(1461571200000),
                dateTo = Date(1565161200000),
                location = "Hayward, CA",
                logo = "",
                logoDescription = "Logo",
                coverImage = "",
                imageDescription = "Cover",
                order = 1,
            ),
            onClick = {},
        )
    }
}
