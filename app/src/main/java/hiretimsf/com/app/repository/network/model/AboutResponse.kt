package hiretimsf.com.app.repository.network.model

import kotlinx.serialization.Serializable

@Serializable
data class AboutResponse(
    val data: AboutData,
)

@Serializable
data class AboutData(
    val sections: List<AboutSection>,
    val photos: List<AboutPhoto>,
)

@Serializable
data class AboutSection(
    val title: String,
    val content: String,
)

@Serializable
data class AboutPhoto(
    val src: String,
    val width: Int,
    val height: Int,
    val alt: String,
    val url: String,
)
