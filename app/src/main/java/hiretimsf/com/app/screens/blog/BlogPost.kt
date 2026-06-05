package hiretimsf.com.app.screens.blog

import hiretimsf.com.app.repository.network.model.BlogPostResponse
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@Serializable
data class BlogPost(
    val slug: String,
    val title: String,
    val date: String,
    val category: String,
    val readTime: String,
    val excerpt: String,
    val coverImageUrl: String,
    val imageAlt: String,
    val url: String,
    val author: String = "",
    val authorAvatarUrl: String = "",
    val authorAvatarAlt: String = "",
    val tags: List<String> = emptyList(),
    val sections: List<BlogPostSection> = emptyList(),
    val content: String = "",
)

@Serializable
data class BlogPostSection(
    val title: String,
    val content: String,
)

fun BlogPostResponse.toBlogPost(): BlogPost {
    return BlogPost(
        slug = slug,
        title = title,
        date = created.toUsBlogDate(),
        category = category,
        readTime = readingTime,
        excerpt = excerpt ?: description,
        coverImageUrl = thumbnailUrl ?: imageUrl,
        imageAlt = imageAlt,
        url = url,
        author = author,
        authorAvatarUrl = authorAvatarUrl,
        authorAvatarAlt = authorAvatarAlt,
        tags = tags,
        content = content.orEmpty().withAndroidPortfolioScreenshotsFallback(slug),
        sections = sections.map { section ->
            BlogPostSection(
                title = section.title,
                content = section.content,
            )
        },
    )
}

val placeholderBlogPosts = listOf(
    BlogPost(
        slug = "android-compose-notes",
        title = "Building calmer Android screens with Compose",
        date = "May 1, 2026",
        category = "Android",
        readTime = "4 min read",
        excerpt = "Placeholder notes for a future post about replacing legacy screens with focused Compose UI.",
        coverImageUrl = "",
        imageAlt = "Blog cover placeholder",
        url = "",
        author = "Tim Baz",
        authorAvatarUrl = "https://hiretimsf.com/images/logo.png",
        authorAvatarAlt = "Tim Baz",
        tags = listOf("Android", "Compose"),
    ),
)

private fun String.toUsBlogDate(): String {
    val value = trim()
    if (value.isBlank()) return value

    val inputFormats = listOf(
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        "yyyy-MM-dd'T'HH:mm:ss'Z'",
        "yyyy-MM-dd",
        "MM/dd/yyyy",
        "MMM d, yyyy",
        "MMMM d, yyyy",
    )
    val outputFormat = SimpleDateFormat("MMM d, yyyy", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    inputFormats.forEach { pattern ->
        val formatter = SimpleDateFormat(pattern, Locale.US).apply {
            isLenient = false
            timeZone = TimeZone.getTimeZone("UTC")
        }
        val date = runCatching { formatter.parse(value) }.getOrNull()
        if (date != null) return outputFormat.format(date)
    }

    return value
}

private fun String.withAndroidPortfolioScreenshotsFallback(slug: String): String {
    if (slug != "rebuilding-android-portfolio-app-jetpack-compose") return this
    if (contains("/images/blog/android-compose-portfolio-app/old-app-screen-01.webp")) return this

    val oldScreenshots = (1..9).joinToString(separator = "\n") { index ->
        markdownImage(
            path = "/images/blog/android-compose-portfolio-app/old-app-screen-${String.format(Locale.US, "%02d", index)}.webp",
            alt = "Original Android portfolio app screenshot $index",
        )
    }

    val newScreenshots = (1..8).joinToString(separator = "\n") { index ->
        markdownImage(
            path = "/images/blog/android-compose-portfolio-app/app-screen-${String.format(Locale.US, "%02d", index)}.webp",
            alt = "New Android portfolio app screenshot $index",
        )
    }

    val insertionPoint = indexOf("## The new Android stack")
    val resolved = "$oldScreenshots\n\n$this"
    return if (insertionPoint >= 0) {
        val adjustedInsertionPoint = oldScreenshots.length + 2 + insertionPoint
        resolved.replaceRange(adjustedInsertionPoint, adjustedInsertionPoint, "\n\n$newScreenshots\n\n")
    } else {
        "$resolved\n\n$newScreenshots"
    }
}

private fun markdownImage(path: String, alt: String): String = "![$alt]($path)"
