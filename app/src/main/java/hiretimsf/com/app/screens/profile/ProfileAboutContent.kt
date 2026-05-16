package hiretimsf.com.app.screens.profile

import android.widget.ImageView
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.imageLoader
import coil.load
import com.intuit.sdp.R as SdpR
import kotlinx.coroutines.launch
import hiretimsf.com.app.R
import hiretimsf.com.app.repository.network.model.AboutSection

@Composable
fun ProfileAboutContent(
    sections: List<AboutSection>,
    introductionImages: List<CarouselImage>,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 22.dp, bottom = 4.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        sections.forEachIndexed { index, section ->
            AboutTextSection(section = section)
            if (index == 0 && introductionImages.isNotEmpty()) {
                AboutPhotoCarousel(images = introductionImages)
            }
        }
    }
}

@Composable
private fun AboutTextSection(
    section: AboutSection,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
            .then(modifier),
    ) {
        AboutSectionHeader(title = section.title)
        AboutSectionBody(text = section.content)
    }
}

@Composable
private fun AboutSectionHeader(title: String) {
    Text(
        text = title,
        color = colorResource(R.color.colorOnPrimarySurface),
        fontFamily = profileFontFamily(),
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 26.sp,
        maxLines = 2,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun AboutSectionBody(text: String) {
    val linkColor = colorResource(R.color.colorPrimary)
    val annotatedText = remember(text, linkColor) {
        text.toClickableMarkdownLinks(linkColor)
    }

    // Final rendering point for each profile About section body.
    Text(
        text = annotatedText,
        style = TextStyle(
            color = colorResource(R.color.colorOnSurface),
            fontFamily = profileFontFamily(),
            fontSize = 17.sp,
            lineHeight = 28.sp,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AboutPhotoCarousel(images: List<CarouselImage>) {
    val pagerState = rememberPagerState(pageCount = { images.size })
    val scope = rememberCoroutineScope()
    val currentPage = pagerState.currentPage

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(SdpR.dimen._320sdp))
            .padding(horizontal = 18.dp),
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.dimen_2dp)),
            modifier = Modifier.fillMaxSize(),
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
            ) { page ->
                FamilyPhoto(
                    image = images[page],
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }

        CarouselButton(
            icon = R.drawable.ic_chevron_left,
            contentDescription = stringResource(R.string.cd_previous_photo),
            enabled = currentPage > 0,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 10.dp),
        ) {
            scope.launch {
                pagerState.animateScrollToPage((currentPage - 1).coerceAtLeast(0))
            }
        }

        CarouselButton(
            icon = R.drawable.ic_chevron_right,
            contentDescription = stringResource(R.string.cd_next_photo),
            enabled = currentPage < images.lastIndex,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 10.dp),
        ) {
            scope.launch {
                pagerState.animateScrollToPage((currentPage + 1).coerceAtMost(images.lastIndex))
            }
        }

        Text(
            text = "${currentPage + 1} / ${images.size}",
            color = colorResource(R.color.colorOnPrimary),
            fontFamily = profileFontFamily(),
            fontSize = 14.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp)
                .background(colorResource(R.color.colorTransparent), RoundedCornerShape(50))
                .padding(
                    horizontal = 12.dp,
                    vertical = 6.dp,
                ),
        )
    }
}

@Composable
private fun CarouselButton(
    icon: Int,
    contentDescription: String,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .size(44.dp)
            .alpha(if (enabled) 1f else 0.35f)
            .background(colorResource(R.color.colorTransparent), CircleShape),
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = contentDescription,
            colorFilter = ColorFilter.tint(colorResource(R.color.colorOnPrimary)),
            modifier = Modifier.size(22.dp),
        )
    }
}

@Composable
private fun FamilyPhoto(
    image: CarouselImage,
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
            imageView.contentDescription = image.description
            imageView.load(image.url, imageLoader = imageView.context.imageLoader) {
                placeholder(R.drawable.profile)
                error(R.drawable.profile)
            }
        },
    )
}

@Composable
private fun profileFontFamily(): FontFamily = FontFamily(Font(R.font.questrial))

private fun String.toClickableMarkdownLinks(linkColor: Color): AnnotatedString {
    val markdownLinkPattern = Regex("\\[([^]]+)]\\(([^)]+)\\)")
    val builder = AnnotatedString.Builder()
    var lastIndex = 0

    markdownLinkPattern.findAll(this).forEach { match ->
        builder.append(substring(lastIndex, match.range.first))

        val label = match.groupValues[1]
        val url = match.groupValues[2].toProfileUrl()
        val start = builder.length
        builder.append(label)
        builder.addStyle(
            style = SpanStyle(
                color = linkColor,
                textDecoration = TextDecoration.Underline,
            ),
            start = start,
            end = builder.length,
        )
        builder.addLink(
            url = LinkAnnotation.Url(
                url = url,
                styles = TextLinkStyles(
                    style = SpanStyle(
                        color = linkColor,
                        textDecoration = TextDecoration.Underline,
                    ),
                ),
            ),
            start = start,
            end = builder.length,
        )

        lastIndex = match.range.last + 1
    }

    builder.append(substring(lastIndex))
    return builder.toAnnotatedString()
}

private fun String.toProfileUrl(): String {
    return if (startsWith("/")) {
        "https://hiretimsf.com$this"
    } else {
        this
    }
}
