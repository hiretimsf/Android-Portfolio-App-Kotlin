package hiretimsf.com.app.screens.experience.detail

import android.widget.ImageView
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import hiretimsf.com.app.R
import hiretimsf.com.app.repository.database.model.button.ButtonModel
import hiretimsf.com.app.repository.database.model.experience.ExperienceModel
import hiretimsf.com.app.repository.database.model.resource.ResourceModel
import hiretimsf.com.app.repository.database.model.task.TaskModel
import hiretimsf.com.app.utils.adapters.bindingAdapters.loadImage
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private val experienceDetailFontFamily = FontFamily(Font(R.font.questrial))

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
fun ExperienceDetailComposeScreen(
    item: ExperienceModel?,
    buttons: LazyPagingItems<ButtonModel>,
    tasks: LazyPagingItems<TaskModel>,
    resources: LazyPagingItems<ResourceModel>,
    onUrlClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (item == null) {
        Spacer(modifier = modifier.fillMaxSize())
        return
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.colorSurface)),
    ) {
        item {
            RemoteExperienceDetailImage(
                url = item.coverImage,
                contentDescription = item.imageDescription,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
            )
        }
        item {
            Column(Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RemoteExperienceDetailImage(
                        url = item.logo,
                        contentDescription = item.logoDescription,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape),
                    )
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text(
                            text = item.title,
                            color = colorResource(R.color.colorOnPrimarySurface),
                            fontFamily = experienceDetailFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        ExperienceMetaRow(R.drawable.ic_office, item.company)
                        ExperienceMetaRow(R.drawable.ic_map, item.location)
                        ExperienceMetaRow(R.drawable.ic_date, formatDateRange(item.dateFrom, item.dateTo))
                    }
                }
                Text(
                    text = item.info,
                    color = colorResource(R.color.colorOnSurface),
                    fontFamily = experienceDetailFontFamily,
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    modifier = Modifier.padding(top = 16.dp),
                )
            }
        }
        item {
            FlowRow(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                repeat(buttons.itemCount) { index ->
                    buttons[index]?.let { button ->
                        Button(
                            onClick = { onUrlClick(button.url) },
                            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.colorPrimary)),
                        ) {
                            Text(button.title, fontFamily = experienceDetailFontFamily)
                        }
                    }
                }
            }
        }
        item {
            Text(
                text = "Responsibilities",
                color = colorResource(R.color.colorHeaderTitle),
                fontFamily = experienceDetailFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(R.color.colorHeaderBackground))
                    .padding(16.dp),
            )
        }
        items(
            count = tasks.itemCount,
            key = tasks.itemKey { it.id },
        ) { index ->
            tasks[index]?.let { task ->
                Text(
                    text = task.task,
                    color = colorResource(R.color.colorOnSurface),
                    fontFamily = experienceDetailFontFamily,
                    fontSize = 15.sp,
                    lineHeight = 21.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                )
            }
        }
        item {
            if (resources.itemCount > 0) {
                Text(
                    text = "Links",
                    color = colorResource(R.color.colorHeaderTitle),
                    fontFamily = experienceDetailFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorResource(R.color.colorHeaderBackground))
                        .padding(16.dp),
                )
                LazyRow(
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    items(
                        count = resources.itemCount,
                        key = resources.itemKey { it.id },
                    ) { index ->
                        resources[index]?.let { resource ->
                            Button(onClick = { resource.url?.let(onUrlClick) }) {
                                Text(resource.title, fontFamily = experienceDetailFontFamily)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ExperienceMetaRow(icon: Int, text: String) {
    Row(
        modifier = Modifier.padding(top = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = colorResource(R.color.colorHeaderTitle),
            modifier = Modifier.size(10.dp),
        )
        Spacer(Modifier.width(5.dp))
        Text(
            text = text,
            color = colorResource(R.color.colorHeaderTitle),
            fontFamily = experienceDetailFontFamily,
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun RemoteExperienceDetailImage(
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
