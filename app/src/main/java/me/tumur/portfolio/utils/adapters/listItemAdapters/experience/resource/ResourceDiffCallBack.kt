package me.tumur.portfolio.utils.adapters.listItemAdapters.experience.resource

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import me.tumur.portfolio.repository.database.model.resource.ResourceModel

/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter or PagingDataAdapter to calculate the minimum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */

class ResourceDiffCallBack : DiffUtil.ItemCallback<ResourceModel>() {
    override fun areItemsTheSame(oldItem: ResourceModel, newItem: ResourceModel): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ResourceModel, newItem: ResourceModel): Boolean {
        return oldItem == newItem
    }

}