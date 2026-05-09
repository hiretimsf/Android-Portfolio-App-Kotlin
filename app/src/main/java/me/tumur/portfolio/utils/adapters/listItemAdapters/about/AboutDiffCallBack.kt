package me.tumur.portfolio.utils.adapters.listItemAdapters.about

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter or PagingDataAdapter to calculate the minimum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */

class AboutDiffCallBack: DiffUtil.ItemCallback<AboutItem>() {
    override fun areItemsTheSame(oldItem: AboutItem, newItem: AboutItem): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: AboutItem, newItem: AboutItem): Boolean {
        return oldItem == newItem
    }

}