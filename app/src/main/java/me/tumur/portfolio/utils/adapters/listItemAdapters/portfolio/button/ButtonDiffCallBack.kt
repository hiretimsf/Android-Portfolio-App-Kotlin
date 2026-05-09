package me.tumur.portfolio.utils.adapters.listItemAdapters.portfolio.button

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import me.tumur.portfolio.repository.database.model.button.ButtonModel

/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter or PagingDataAdapter  to calculate the minimum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */

class ButtonDiffCallBack : DiffUtil.ItemCallback<ButtonModel>() {
    override fun areItemsTheSame(oldItem: ButtonModel, newItem: ButtonModel): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ButtonModel, newItem: ButtonModel): Boolean {
        return oldItem == newItem
    }

}