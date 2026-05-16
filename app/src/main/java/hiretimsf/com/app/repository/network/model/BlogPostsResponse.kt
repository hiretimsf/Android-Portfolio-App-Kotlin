package hiretimsf.com.app.repository.network.model

import kotlinx.serialization.Serializable

@Serializable
data class BlogPostsResponse(
    val data: List<BlogPostResponse> = emptyList(),
)

@Serializable
data class BlogPostDetailResponse(
    val data: BlogPostResponse,
)

@Serializable
data class BlogPostResponse(
    val title: String = "",
    val description: String = "",
    val created: String = "",
    val lastUpdated: String = "",
    val imageUrl: String = "",
    val thumbnailUrl: String? = null,
    val imageAlt: String = "",
    val author: String = "",
    val authorAvatarUrl: String = "",
    val authorAvatarAlt: String = "",
    val category: String = "",
    val tags: List<String> = emptyList(),
    val seo: List<String> = emptyList(),
    val readingTime: String = "",
    val readingTimeMinutes: Int = 0,
    val slug: String = "",
    val url: String = "",
    val excerpt: String? = null,
    val score: Int? = null,
    val content: String? = null,
    val sections: List<BlogPostSectionResponse> = emptyList(),
)

@Serializable
data class BlogPostSectionResponse(
    val title: String = "",
    val content: String = "",
)
