package hiretimsf.com.app.screens.portfolio.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import hiretimsf.com.app.repository.database.model.portfolio.PortfolioModel
import hiretimsf.com.app.screens.shared.components.PrimaryGradientButton
import hiretimsf.com.app.screens.shared.components.primaryActionContentColor
import hiretimsf.com.app.screens.shared.components.RemoteImage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val portfolioCardFontFamily = FontFamily(Font(R.font.questrial))

@Composable
fun PortfolioCard(
    item: PortfolioModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.colorElevatedSurface)),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            RemoteImage(
                url = item.coverImage,
                contentDescription = item.imageDescription,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(178.dp)
                    .background(colorResource(R.color.colorBorder)),
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ProjectPill(
                        text = item.header.ifBlank { "Project" },
                        modifier = Modifier.weight(1f, fill = false),
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = formatDateRange(item.dateFrom, item.dateTo),
                        color = colorResource(R.color.colorHeaderTitle),
                        fontFamily = portfolioCardFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Text(
                    text = item.subTitle,
                    color = colorResource(R.color.colorOnPrimarySurface),
                    fontFamily = portfolioCardFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    lineHeight = 24.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 12.dp),
                )
                Text(
                    text = item.text,
                    color = colorResource(R.color.colorOnSurface),
                    fontFamily = portfolioCardFontFamily,
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 8.dp),
                )
                ProjectFooter(
                    techStacks = item.info.split(",").map { it.trim() }.filter { it.isNotBlank() },
                    onReadMoreClick = onClick,
                    modifier = Modifier.padding(top = 12.dp),
                )
            }
        }
    }
}

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
private fun ProjectFooter(
    techStacks: List<String>,
    onReadMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            techStacks.take(3).forEach { techStack ->
                ProjectPill(text = techStack)
            }
        }
        PrimaryGradientButton(
            onClick = onReadMoreClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
        ) {
            Text(
                text = "Read More",
                color = primaryActionContentColor(),
                fontFamily = portfolioCardFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
            )
            Spacer(Modifier.size(8.dp))
            Icon(
                painter = painterResource(R.drawable.ic_chevron_right),
                contentDescription = null,
                tint = primaryActionContentColor(),
                modifier = Modifier.size(16.dp),
            )
        }
    }
}

private fun formatDateRange(dateFrom: Date?, dateTo: Date?): String {
    if (dateFrom == null || dateTo == null) return ""
    val outputFormat = SimpleDateFormat("MMM yyyy", Locale.US)
    if (dateFrom.compareTo(dateTo) == 0) return outputFormat.format(dateFrom)

    val startText = outputFormat.format(dateFrom)
    val endText = outputFormat.format(dateTo)
    return if (startText == endText) startText else "$startText - $endText"
}
