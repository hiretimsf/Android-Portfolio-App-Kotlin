package me.tumur.portfolio.utils.adapters.listItemAdapters.about

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.tumur.portfolio.repository.network.model.AboutSection
import me.tumur.portfolio.utils.constants.Constants

/**
 * An adapter that provides a list of [AboutSection] to a [RecyclerView]
 * */

class AboutAdapter : ListAdapter<AboutItem, RecyclerView.ViewHolder>(AboutDiffCallBack()) {

    /**
     * Composition of about item text and header
     * into a separate list for the [RecyclerView].
     * */
    fun addHeaderAndSubmitList(list: List<AboutSection>?, introductionImages: List<CarouselImage>) {
        var carouselAdded = false
        val compositedList = list.orEmpty()
            .flatMap { section ->
                buildList {
                    add(AboutItem.Header(section.title))
                    add(AboutItem.About(section))
                    if (!carouselAdded && introductionImages.isNotEmpty()) {
                        add(AboutItem.Carousel(introductionImages))
                        carouselAdded = true
                    }
                }
            }
        submitList(compositedList)
    }

    /**
     * Part of the RecyclerView adapter, called when RecyclerView needs a new [AboutHeaderViewHolder] or [AboutItemViewHolder]
     *
     * [AboutItemViewHolder] and [AboutHeaderViewHolder] hold the views for the [RecyclerView] as well as providing information
     * to the RecyclerView such as where on the screen it was last drawn during scrolling.
     * */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Constants.HEADER -> AboutHeaderViewHolder.from(parent)
            Constants.ITEM -> AboutItemViewHolder.from(parent)
            Constants.CAROUSEL -> AboutCarouselViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    /**
     * Part of the RecyclerView adapter, called when the RecyclerView needs to show an item.
     *
     * The [AboutItemViewHolder] and [AboutHeaderViewHolder] passed may be recycled so make sure that this sets any properties
     * that may be have been set previously
     * */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AboutItemViewHolder -> {
                val item = getItem(position) as AboutItem.About
                holder.bind(item)
            }
            is AboutHeaderViewHolder -> {
                val header = getItem(position) as AboutItem.Header
                holder.bind(header)
            }
            is AboutCarouselViewHolder -> {
                val carousel = getItem(position) as AboutItem.Carousel
                holder.bind(carousel)
            }
        }

    }

    /**
     * Get a correct item view type
     * based on composited list of data
     * */
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is AboutItem.Header -> Constants.HEADER
            is AboutItem.About -> Constants.ITEM
            is AboutItem.Carousel -> Constants.CAROUSEL
        }
    }
}
