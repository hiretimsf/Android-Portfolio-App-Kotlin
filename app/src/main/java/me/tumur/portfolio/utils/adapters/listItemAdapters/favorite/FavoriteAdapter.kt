package me.tumur.portfolio.utils.adapters.listItemAdapters.favorite

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import me.tumur.portfolio.repository.database.model.favorite.FavoriteModel

/**
 * An adapter that provides a list of [FavoriteModel] to a [RecyclerView]
 * */

class FavoriteAdapter(private val clickListener: FavoriteClickListener) :
    PagingDataAdapter<FavoriteModel, FavoriteViewHolder>(FavoriteDiffCallBack()) {

    /**
     * Part of the RecyclerView adapter, called when RecyclerView needs a new [FavoriteViewHolder]
     *
     * A [FavoriteViewHolder] holds the view for the [RecyclerView] as well as providing information
     * to the RecyclerView such as where on the screen it was last drawn during scrolling.
     * */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder.from(parent)
    }

    /**
     * Part of the RecyclerView adapter, called when the RecyclerView needs to show an item.
     *
     * The [FavoriteViewHolder] passed may be recycled so make sure that this sets any properties
     * that may be have been set previously
     * */
    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val favoriteItem = getItem(position)
        holder.bind(favoriteItem, clickListener)
    }
}