package hiretimsf.com.app.screens.blog

import hiretimsf.com.app.repository.network.model.BlogPostResponse
import kotlinx.serialization.Serializable

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
        date = created,
        category = category,
        readTime = readingTime,
        excerpt = excerpt ?: description,
        coverImageUrl = thumbnailUrl ?: imageUrl,
        imageAlt = imageAlt,
        url = url,
        content = content.orEmpty(),
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
        date = "May 2026",
        category = "Android",
        readTime = "4 min read",
        excerpt = "Placeholder notes for a future post about replacing legacy screens with focused Compose UI.",
        coverImageUrl = "",
        imageAlt = "Blog cover placeholder",
        url = "",
    ),
)
