package me.tumur.portfolio.utils.adapters.listItemAdapters.portfolio.button

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import me.tumur.portfolio.repository.database.model.button.ButtonModel
import me.tumur.portfolio.utils.constants.Constants

/**
 * An adapter that provides a list of [ButtonModel] to a [RecyclerView]
 * */

class ButtonAdapter(private val clickListener: ButtonClickListener) :
    PagingDataAdapter<ButtonModel, RecyclerView.ViewHolder>(ButtonDiffCallBack()) {

    /**
     * Part of the RecyclerView adapter, called when RecyclerView needs a new [ButtonNormalViewHolder] or [ButtonOutlineViewHolder]
     *
     * [ButtonNormalViewHolder] and [ButtonOutlineViewHolder] hold the views for the [RecyclerView] as well as providing information
     * to the RecyclerView such as where on the screen it was last drawn during scrolling.
     * */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Constants.BUTTON_NORMAL -> ButtonNormalViewHolder.from(parent)
            Constants.BUTTON_OUTLINE -> ButtonOutlineViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    /**
     * Part of the RecyclerView adapter, called when the RecyclerView needs to show an item.
     *
     * The [ButtonNormalViewHolder] and [ButtonOutlineViewHolder] passed may be recycled so make sure that this sets any properties
     * that may be have been set previously
     * */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ButtonNormalViewHolder -> {
                holder.bind(getItem(position) as ButtonModel, clickListener)
            }
            is ButtonOutlineViewHolder -> {
                holder.bind(getItem(position) as ButtonModel, clickListener)
            }
        }

    }

    /**
     * Get a correct item view type
     * based on composited list of data
     * */
    override fun getItemViewType(position: Int): Int {

        val item: ButtonModel? = getItem(position)
        return when (item?.order) {
            1 -> Constants.BUTTON_OUTLINE
            else -> Constants.BUTTON_NORMAL
        }
    }
}