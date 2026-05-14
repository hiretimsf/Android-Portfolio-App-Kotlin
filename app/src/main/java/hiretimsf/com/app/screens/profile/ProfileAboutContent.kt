package hiretimsf.com.app.screens.profile

import android.widget.ImageView
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
    Column(modifier = Modifier.fillMaxWidth()) {
        sections.forEachIndexed { index, section ->
            AboutSectionHeader(title = section.title)
            AboutSectionBody(text = section.content)
            if (index == 0 && introductionImages.isNotEmpty()) {
                AboutPhotoCarousel(images = introductionImages)
            }
        }
    }
}

@Composable
private fun AboutSectionHeader(title: String) {
    Text(
        text = title.uppercase(),
        color = colorResource(R.color.colorHeaderTitle),
        fontFamily = profileFontFamily(),
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        maxLines = 1,
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(R.color.colorHeaderBackground))
            .padding(
                start = dimensionResource(SdpR.dimen._10sdp),
                top = dimensionResource(SdpR.dimen._10sdp),
                end = dimensionResource(SdpR.dimen._10sdp),
                bottom = dimensionResource(SdpR.dimen._5sdp),
            ),
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
            fontSize = 18.sp,
            lineHeight = 30.sp,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = dimensionResource(SdpR.dimen._10sdp),
                top = dimensionResource(SdpR.dimen._10sdp),
                end = dimensionResource(SdpR.dimen._10sdp),
                bottom = dimensionResource(SdpR.dimen._10sdp),
            ),
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
            .height(dimensionResource(SdpR.dimen._300sdp))
            .padding(dimensionResource(SdpR.dimen._10sdp)),
    ) {
        Card(
            shape = RoundedCornerShape(dimensionResource(SdpR.dimen._5sdp)),
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
                .padding(start = dimensionResource(SdpR.dimen._8sdp)),
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
                .padding(end = dimensionResource(SdpR.dimen._8sdp)),
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
                .padding(bottom = dimensionResource(SdpR.dimen._8sdp))
                .background(colorResource(R.color.colorTransparent), RoundedCornerShape(dimensionResource(SdpR.dimen._10sdp)))
                .padding(
                    horizontal = dimensionResource(SdpR.dimen._8sdp),
                    vertical = dimensionResource(SdpR.dimen._4sdp),
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
            .size(dimensionResource(SdpR.dimen._34sdp))
            .alpha(if (enabled) 1f else 0.35f)
            .background(colorResource(R.color.colorTransparent), CircleShape),
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = contentDescription,
            colorFilter = ColorFilter.tint(colorResource(R.color.colorOnPrimary)),
            modifier = Modifier.size(dimensionResource(SdpR.dimen._18sdp)),
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
