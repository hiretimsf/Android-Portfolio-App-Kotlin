package hiretimsf.com.app.screens.blog.detail

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import hiretimsf.com.app.R
import hiretimsf.com.app.screens.blog.components.BlogPill
import hiretimsf.com.app.screens.blog.placeholderBlogPosts
import kotlinx.coroutines.launch

private val blogDetailFontFamily = FontFamily(Font(R.font.questrial))

@Composable
fun BlogDetailComposeScreen(
    state: BlogDetailScreenState,
    onLinkClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (state.isLoading && state.post == null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(color = colorResource(R.color.colorPrimary))
        }
        return
    }

    val selectedPost = state.post ?: placeholderBlogPosts.first()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.colorSurface)),
        contentPadding = PaddingValues(bottom = 16.dp),
    ) {
        item {
            RemoteImage(
                url = selectedPost.coverImageUrl,
                contentDescription = selectedPost.imageAlt,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(colorResource(R.color.colorBorder)),
            )
        }
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                BlogByline(post = selectedPost, modifier = Modifier.padding(top = 0.dp))
                BlogDetailMetadataRow(
                    post = selectedPost,
                    modifier = Modifier.padding(top = 12.dp),
                )
                Text(
                    text = selectedPost.title,
                    color = colorResource(R.color.colorOnPrimarySurface),
                    fontFamily = blogDetailFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    lineHeight = 34.sp,
                    modifier = Modifier.padding(top = 16.dp),
                )
                Text(
                    text = selectedPost.excerpt,
                    color = colorResource(R.color.colorOnSurface),
                    fontFamily = blogDetailFontFamily,
                    fontSize = 17.sp,
                    lineHeight = 25.sp,
                    modifier = Modifier.padding(top = 22.dp),
                )
                state.errorMessage?.let { message ->
                    Text(
                        text = message,
                        color = colorResource(R.color.colorOnSurface),
                        fontFamily = blogDetailFontFamily,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 18.dp)
                            .background(colorResource(R.color.colorHeaderBackground), RoundedCornerShape(8.dp))
                            .padding(12.dp),
                    )
                }
            }
        }
        if (selectedPost.content.isNotBlank()) {
            item {
                BlogContentBlocks(
                    blocks = selectedPost.content.toContentBlocks(),
                    onLinkClick = onLinkClick,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )
            }
        } else {
            selectedPost.sections.forEach { section ->
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                    ) {
                        Text(
                            text = section.title,
                            color = colorResource(R.color.colorOnPrimarySurface),
                            fontFamily = blogDetailFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            lineHeight = 25.sp,
                        )
                        BlogContentBlocks(
                            blocks = section.content.toContentBlocks(),
                            onLinkClick = onLinkClick,
                            modifier = Modifier.padding(top = 8.dp),
                        )
                    }
                }
            }
        }
        if (selectedPost.tags.isNotEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                ) {
                    Text(
                        text = "Tags",
                        color = colorResource(R.color.colorOnPrimarySurface),
                        fontFamily = blogDetailFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                    )
                    TagFlow(
                        tags = selectedPost.tags,
                        modifier = Modifier.padding(top = 10.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun BlogContentBlocks(
    blocks: List<BlogContentBlock>,
    onLinkClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        blocks.forEach { block ->
            when (block) {
                is BlogContentBlock.Heading -> {
                    Text(
                        text = block.title,
                        color = colorResource(R.color.colorOnPrimarySurface),
                        fontFamily = blogDetailFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = if (block.level == 1) 22.sp else 20.sp,
                        lineHeight = 26.sp,
                        modifier = Modifier.padding(top = if (block.level == 1) 10.dp else 6.dp),
                    )
                }
                is BlogContentBlock.Image -> {
                    BlogImageCard(image = block.image)
                }
                is BlogContentBlock.ImageCarousel -> {
                    BlogImageCarousel(images = block.images)
                }
                is BlogContentBlock.Text -> {
                    Text(
                        text = block.text.toDisplayText(),
                        color = colorResource(R.color.colorOnSurface),
                        fontFamily = blogDetailFontFamily,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }
                is BlogContentBlock.Video -> {
                    VideoLinkCard(
                        title = block.title,
                        url = block.url,
                        onClick = { onLinkClick(block.watchUrl) },
                    )
                }
            }
        }
    }
}

@Composable
private fun BlogDetailMetadataRow(
    post: hiretimsf.com.app.screens.blog.BlogPost,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        BlogPill(
            text = post.category,
            modifier = Modifier.weight(1f, fill = false),
        )
        BlogDetailMetaText(text = post.date)
        BlogDetailMetaText(text = "|", alpha = 0.7f)
        BlogDetailMetaText(text = post.readTime)
    }
}

@Composable
private fun BlogDetailMetaText(
    text: String,
    alpha: Float = 1f,
) {
    Text(
        text = text,
        color = colorResource(R.color.colorOnSurface).copy(alpha = alpha),
        fontFamily = blogDetailFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
private fun BlogByline(
    post: hiretimsf.com.app.screens.blog.BlogPost,
    modifier: Modifier = Modifier,
) {
    if (post.author.isBlank() && post.authorAvatarUrl.isBlank()) return

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        RemoteImage(
            url = post.authorAvatarUrl,
            contentDescription = post.authorAvatarAlt.ifBlank { post.author },
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(colorResource(R.color.colorBorder)),
        )
        Text(
            text = post.author.ifBlank { "Tim Baz" },
            color = colorResource(R.color.colorOnPrimarySurface),
            fontFamily = blogDetailFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BlogImageCarousel(
    images: List<BlogImage>,
    modifier: Modifier = Modifier,
) {
    if (images.isEmpty()) return
    val pagerState = rememberPagerState(pageCount = { images.size })
    val scope = rememberCoroutineScope()
    val currentPage = pagerState.currentPage.coerceIn(images.indices)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 6.dp)
            .aspectRatio(16f / 11f),
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            BlogImageCard(
                image = images[page],
                includeDefaultSize = false,
                modifier = Modifier.fillMaxSize(),
            )
        }

        if (images.size > 1) {
            CarouselButton(
                icon = R.drawable.ic_chevron_left,
                contentDescription = "Previous image",
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 8.dp),
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(if (currentPage == 0) images.lastIndex else currentPage - 1)
                    }
                },
            )

            CarouselButton(
                icon = R.drawable.ic_chevron_right,
                contentDescription = "Next image",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp),
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(if (currentPage == images.lastIndex) 0 else currentPage + 1)
                    }
                },
            )

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 10.dp)
                    .background(Color.Black.copy(alpha = 0.22f), CircleShape)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                images.forEachIndexed { index, _ ->
                    Box(
                        modifier = Modifier
                            .size(if (index == currentPage) 9.dp else 7.dp)
                            .background(
                                color = if (index == currentPage) {
                                    Color.White
                                } else {
                                    Color.White.copy(alpha = 0.45f)
                                },
                                shape = CircleShape,
                            ),
                    )
                }
            }
        }
    }
}

@Composable
private fun CarouselButton(
    icon: Int,
    contentDescription: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(32.dp)
            .background(Color.Black.copy(alpha = 0.18f), CircleShape),
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = contentDescription,
            colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier.size(16.dp),
        )
    }
}

@Composable
private fun BlogImageCard(
    image: BlogImage,
    modifier: Modifier = Modifier,
    includeDefaultSize: Boolean = true,
) {
    val imageModifier = if (includeDefaultSize) {
        modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 6.dp)
            .aspectRatio(16f / 10f)
    } else {
        modifier
    }

    Box(
        modifier = imageModifier
            .clip(RoundedCornerShape(8.dp))
            .background(colorResource(R.color.colorBorder))
            .border(1.dp, colorResource(R.color.colorHeaderBackground), RoundedCornerShape(8.dp)),
    ) {
        RemoteImage(
            url = image.url.toAbsoluteBlogImageUrl(),
            contentDescription = image.alt,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize(),
        )
        image.caption?.let { caption ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.62f)),
                        ),
                    )
                    .padding(12.dp),
            ) {
                Text(
                    text = caption,
                    color = Color.White,
                    fontFamily = blogDetailFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    maxLines = 2,
                )
            }
        }
    }
}

@Composable
private fun VideoLinkCard(
    title: String,
    url: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 6.dp)
            .aspectRatio(16f / 9f)
            .clip(RoundedCornerShape(8.dp))
            .background(colorResource(R.color.colorHeaderBackground))
            .border(1.dp, colorResource(R.color.colorHeaderBackground), RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
    ) {
        val thumbnailUrl = url.youtubeVideoId()?.let { "https://i.ytimg.com/vi/$it/hqdefault.jpg" }
        if (thumbnailUrl != null) {
            RemoteImage(
                url = thumbnailUrl,
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Black.copy(alpha = 0.1f), Color.Black.copy(alpha = 0.58f)),
                    ),
                ),
        )
        Box(
            modifier = Modifier
                .size(62.dp)
                .align(Alignment.Center)
                .background(Color.Black.copy(alpha = 0.55f), CircleShape)
                .border(1.dp, Color.White.copy(alpha = 0.75f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "PLAY",
                color = Color.White,
                fontFamily = blogDetailFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
            )
        }
        Text(
            text = title,
            color = Color.White,
            fontFamily = blogDetailFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            maxLines = 2,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(14.dp),
        )
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun TagFlow(
    tags: List<String>,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        tags.forEach { tag ->
            BlogPill(text = tag)
        }
    }
}

@Composable
private fun RemoteImage(
    url: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
) {
    AsyncImage(
        model = url,
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier,
    )
}

private sealed class BlogContentBlock {
    data class Heading(val level: Int, val title: String) : BlogContentBlock()
    data class Text(val text: String) : BlogContentBlock()
    data class Image(val image: BlogImage) : BlogContentBlock()
    data class ImageCarousel(val images: List<BlogImage>) : BlogContentBlock()
    data class Video(val title: String, val url: String) : BlogContentBlock() {
        val watchUrl: String = url.toYouTubeWatchUrl()
    }
}

private data class BlogImage(
    val alt: String,
    val url: String,
    val caption: String? = null,
)

private data class ContentMatch(
    val start: Int,
    val end: Int,
    val block: BlogContentBlock,
)

private fun String.toContentBlocks(): List<BlogContentBlock> {
    val imagePattern = Regex("!\\[([\\s\\S]*?)]\\(([^)]+)\\)")
    val videoPattern = Regex("<div[\\s\\S]*?<iframe[\\s\\S]*?(?:</iframe>|/>)[\\s\\S]*?</div>|<iframe[\\s\\S]*?(?:</iframe>|/>)", RegexOption.IGNORE_CASE)
    val matches = buildList {
        imagePattern.findAll(this@toContentBlocks).forEach { match ->
            val alt = match.groupValues[1].replace(Regex("\\s+"), " ").trim()
            val url = match.groupValues[2].trim()
            if (url.isNotBlank()) {
                add(ContentMatch(match.range.first, match.range.last + 1, BlogContentBlock.Image(BlogImage(alt = alt, url = url))))
            }
        }
        videoPattern.findAll(this@toContentBlocks).forEach { match ->
            val html = match.value
            val source = html.htmlAttribute("src")?.normalizedEmbedUrl()
            if (!source.isNullOrBlank()) {
                add(
                    ContentMatch(
                        match.range.first,
                        match.range.last + 1,
                        BlogContentBlock.Video(
                            title = html.htmlAttribute("title") ?: "Embedded video",
                            url = source,
                        ),
                    ),
                )
            }
        }
    }.sortedBy { it.start }
    val blocks = mutableListOf<BlogContentBlock>()
    var lastIndex = 0

    matches.forEach { match ->
        if (match.start < lastIndex) return@forEach
        val text = substring(lastIndex, match.start).trim()
        if (!text.attachAsCaptionIfPossible(blocks)) {
            text.appendTextBlocks(blocks)
        }
        blocks += match.block
        lastIndex = match.end
    }

    val remainingText = substring(lastIndex).trim()
    if (!remainingText.attachAsCaptionIfPossible(blocks)) {
        remainingText.appendTextBlocks(blocks)
    }
    return blocks.ifEmpty { listOf(BlogContentBlock.Text(this)) }.coalescingImageCarousels()
}

private fun String.toAbsoluteBlogImageUrl(): String {
    return when {
        startsWith("http://") || startsWith("https://") -> this
        startsWith("/") -> "https://hiretimsf.com$this"
        else -> "https://hiretimsf.com/$this"
    }
}

private fun String.toDisplayText(): String {
    return replace(Regex("^#{1,6}\\s*", RegexOption.MULTILINE), "")
        .replace(Regex("\\[([^]]+)]\\(([^)]+)\\)"), "$1")
        .replace(Regex("\\n{3,}"), "\n\n")
        .trim()
}

private fun String.appendTextBlocks(blocks: MutableList<BlogContentBlock>) {
    val text = trim()
    if (text.isBlank()) return
    val headingPattern = Regex("(?m)^\\s{0,3}(#{1,3})\\s+(.+?)\\s*$")
    var cursor = 0
    var foundHeading = false

    headingPattern.findAll(text).forEach { match ->
        foundHeading = true
        val preceding = text.substring(cursor, match.range.first).trim()
        if (preceding.isNotBlank()) blocks += BlogContentBlock.Text(preceding)
        blocks += BlogContentBlock.Heading(
            level = match.groupValues[1].length,
            title = match.groupValues[2].toDisplayText(),
        )
        cursor = match.range.last + 1
    }

    val trailing = text.substring(cursor).trim()
    if (trailing.isNotBlank()) blocks += BlogContentBlock.Text(trailing)
    if (!foundHeading && text.isNotBlank()) blocks += BlogContentBlock.Text(text)
}

private fun String.attachAsCaptionIfPossible(blocks: MutableList<BlogContentBlock>): Boolean {
    val caption = captionText() ?: return false
    val lastIndex = blocks.lastIndex
    if (lastIndex < 0) return false
    val imageBlock = blocks[lastIndex] as? BlogContentBlock.Image ?: return false
    blocks[lastIndex] = BlogContentBlock.Image(imageBlock.image.copy(caption = caption))
    return true
}

private fun String.captionText(): String? {
    val text = toDisplayText()
    if (text.isBlank() || text.contains("\n\n") || text.length > 180) return null
    if (text.contains("://") || text.contains("](")) return null
    return text
        .lines()
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .joinToString(" ")
        .takeIf { it.isNotBlank() }
}

private fun List<BlogContentBlock>.coalescingImageCarousels(): List<BlogContentBlock> {
    val result = mutableListOf<BlogContentBlock>()
    val imageRun = mutableListOf<BlogImage>()

    fun flushRun() {
        if (imageRun.isEmpty()) return
        if (imageRun.size > 1) {
            result += BlogContentBlock.ImageCarousel(imageRun.toList())
        } else {
            result += BlogContentBlock.Image(imageRun.first())
        }
        imageRun.clear()
    }

    forEach { block ->
        when (block) {
            is BlogContentBlock.Image -> imageRun += block.image
            else -> {
                flushRun()
                result += block
            }
        }
    }
    flushRun()
    return result
}

private fun String.htmlAttribute(name: String): String? {
    val pattern = Regex("$name\\s*=\\s*[\"']([^\"']+)[\"']", RegexOption.IGNORE_CASE)
    return pattern.find(this)?.groupValues?.getOrNull(1)
}

private fun String.normalizedEmbedUrl(): String {
    val value = trim()
        .replace("&amp;", "&")
        .replace("&#38;", "&")
    return when {
        value.startsWith("//") -> "https:$value"
        else -> value
    }
}

private fun String.youtubeVideoId(): String? {
    val uri = runCatching { Uri.parse(normalizedEmbedUrl()) }.getOrNull() ?: return null
    val host = uri.host.orEmpty()
    if (host.contains("youtu.be")) return uri.pathSegments.firstOrNull()
    val shortsIndex = uri.pathSegments.indexOf("shorts")
    if (shortsIndex >= 0 && uri.pathSegments.size > shortsIndex + 1) return uri.pathSegments[shortsIndex + 1]
    val embedIndex = uri.pathSegments.indexOf("embed")
    if (embedIndex >= 0 && uri.pathSegments.size > embedIndex + 1) return uri.pathSegments[embedIndex + 1]
    return uri.getQueryParameter("v")
}

private fun String.toYouTubeWatchUrl(): String {
    val id = youtubeVideoId() ?: return this
    val uri = runCatching { Uri.parse(normalizedEmbedUrl()) }.getOrNull()
    val start = uri?.getQueryParameter("start") ?: uri?.getQueryParameter("t")
    return buildString {
        append("https://www.youtube.com/watch?v=")
        append(id)
        if (!start.isNullOrBlank()) {
            append("&t=")
            append(start)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BlogDetailComposeScreenPreview() {
    BlogDetailComposeScreen(state = BlogDetailScreenState(post = placeholderBlogPosts.first()), onLinkClick = {})
}
