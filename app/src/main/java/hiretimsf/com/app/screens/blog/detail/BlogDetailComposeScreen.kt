package hiretimsf.com.app.screens.blog.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import hiretimsf.com.app.R
import hiretimsf.com.app.screens.blog.placeholderBlogPosts

private val blogDetailFontFamily = FontFamily(Font(R.font.questrial))

@Composable
fun BlogDetailComposeScreen(
    state: BlogDetailScreenState,
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
                Text(
                    text = selectedPost.category,
                    color = colorResource(R.color.colorHeaderTitle),
                    fontFamily = blogDetailFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .background(colorResource(R.color.colorHeaderBackground), RoundedCornerShape(6.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp),
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
                    text = "${selectedPost.date}  |  ${selectedPost.readTime}",
                    color = colorResource(R.color.colorHeaderTitle),
                    fontFamily = blogDetailFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 10.dp),
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
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }
            }
        }
        if (selectedPost.sections.isEmpty() && selectedPost.content.isNotBlank()) {
            item {
                BlogContentBlocks(
                    blocks = selectedPost.content.toContentBlocks(),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )
            }
        }
    }
}

@Composable
private fun BlogContentBlocks(
    blocks: List<BlogContentBlock>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        blocks.forEach { block ->
            when (block) {
                is BlogContentBlock.Image -> {
                    RemoteImage(
                        url = block.url.toAbsoluteBlogImageUrl(),
                        contentDescription = block.alt,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp, bottom = 6.dp)
                            .aspectRatio(16f / 10f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(colorResource(R.color.colorBorder)),
                    )
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
            }
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
    data class Text(val text: String) : BlogContentBlock()
    data class Image(val alt: String, val url: String) : BlogContentBlock()
}

private fun String.toContentBlocks(): List<BlogContentBlock> {
    val imagePattern = Regex("!\\[([\\s\\S]*?)]\\(([^)]+)\\)")
    val blocks = mutableListOf<BlogContentBlock>()
    var lastIndex = 0

    imagePattern.findAll(this).forEach { match ->
        val text = substring(lastIndex, match.range.first).trim()
        if (text.isNotBlank()) blocks += BlogContentBlock.Text(text)

        val alt = match.groupValues[1].replace(Regex("\\s+"), " ").trim()
        val url = match.groupValues[2].trim()
        if (url.isNotBlank()) blocks += BlogContentBlock.Image(alt = alt, url = url)
        lastIndex = match.range.last + 1
    }

    val remainingText = substring(lastIndex).trim()
    if (remainingText.isNotBlank()) blocks += BlogContentBlock.Text(remainingText)
    return blocks
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

@Preview(showBackground = true)
@Composable
private fun BlogDetailComposeScreenPreview() {
    BlogDetailComposeScreen(state = BlogDetailScreenState(post = placeholderBlogPosts.first()))
}
