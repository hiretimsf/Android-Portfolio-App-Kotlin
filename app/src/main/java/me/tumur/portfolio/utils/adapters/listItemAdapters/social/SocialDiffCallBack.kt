package me.tumur.portfolio.utils.adapters.listItemAdapters.social

import androidx.recyclerview.widget.DiffUtil
import me.tumur.portfolio.repository.database.model.profile.SocialModel

/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter or PagingDataAdapter to calculate the minimum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */

class SocialDiffCallBack: DiffUtil.ItemCallback<SocialModel>() {
    override fun areItemsTheSame(oldItem: SocialModel, newItem: SocialModel): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: SocialModel, newItem: SocialModel): Boolean {
        return oldItem == newItem
    }

}