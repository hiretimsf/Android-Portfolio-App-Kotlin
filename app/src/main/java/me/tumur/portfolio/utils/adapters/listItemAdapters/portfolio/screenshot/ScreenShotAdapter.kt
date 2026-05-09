package me.tumur.portfolio.utils.adapters.listItemAdapters.portfolio.screenshot

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import me.tumur.portfolio.repository.database.model.screenshot.ScreenShotModel

/**
 * An adapter that provides a list of [ScreenShotModel] to a [RecyclerView]
 * */

class ScreenShotAdapter(private val clickListener: ScreenShotClickListener) :
    PagingDataAdapter<ScreenShotModel, ScreenShotViewHolder>(ScreenShotDiffCallBack()) {

    /**
     * Part of the RecyclerView adapter, called when RecyclerView needs a new [ScreenShotViewHolder]
     *
     * A [ScreenShotViewHolder] holds the view for the [RecyclerView] as well as providing information
     * to the RecyclerView such as where on the screen it was last drawn during scrolling.
     * */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScreenShotViewHolder {
        return ScreenShotViewHolder.from(parent)
    }

    /**
     * Part of the RecyclerView adapter, called when the RecyclerView needs to show an item.
     *
     * The [ScreenShotViewHolder] passed may be recycled so make sure that this sets any properties
     * that may be have been set previously
     * */
    override fun onBindViewHolder(holder: ScreenShotViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }
}