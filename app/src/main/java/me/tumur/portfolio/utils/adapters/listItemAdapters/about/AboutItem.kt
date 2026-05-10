package me.tumur.portfolio.utils.adapters.listItemAdapters.about

import androidx.recyclerview.widget.RecyclerView
import me.tumur.portfolio.repository.network.model.AboutSection

/**
 * Sealed class for composited list data
 * to differentiate [AboutItemViewHolder] and [AboutHeaderViewHolder]
 * for [RecyclerView]
 * */
sealed class AboutItem {
    data class About(val about: AboutSection): AboutItem() {
        override val id = about.title + about.content
    }

    data class Carousel(val images: List<CarouselImage>): AboutItem() {
        override val id = images.joinToString(prefix = "carousel-") { it.url }
    }

    data class Header(val header: String): AboutItem() {
        override val id = header
    }

    abstract val id: String
}

data class CarouselImage(
    val url: String,
    val description: String,
)
