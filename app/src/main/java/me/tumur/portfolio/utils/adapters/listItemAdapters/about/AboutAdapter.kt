package me.tumur.portfolio.utils.adapters.listItemAdapters.about

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.tumur.portfolio.repository.database.model.profile.AboutModel
import me.tumur.portfolio.utils.constants.Constants

/**
 * An adapter that provides a list of [AboutModel] to a [RecyclerView]
 * */

class AboutAdapter : ListAdapter<AboutItem, RecyclerView.ViewHolder>(AboutDiffCallBack()) {

    /**
     * Composition of about item text and header
     * into a separate list for the [RecyclerView].
     * */
    fun addHeaderAndSubmitList(list: List<AboutModel>?) {
        val compositedList = list.orEmpty()
            .groupBy(AboutModel::header)
            .flatMap { (header, items) ->
                listOf(AboutItem.Header(header)) + items.map(AboutItem::About)
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
        }
    }
}
