package hiretimsf.com.app.repository.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ProjectsResponse(
    val data: List<ProjectResponse> = emptyList(),
)

@Serializable
data class ProjectResponse(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val imageAlt: String = "",
    val fromDate: String = "",
    val toDate: String = "",
    val category: String = "",
    val websiteUrl: String? = null,
    val githubUrl: String? = null,
    val videoEmbedUrl: String? = null,
    val techStacks: List<String> = emptyList(),
    val featured: Boolean = false,
    val showOnPortfolio: Boolean = true,
    val comingSoon: Boolean = false,
    val weight: Int = 0,
    val slug: String = "",
    val portfolioUrl: String? = null,
)
