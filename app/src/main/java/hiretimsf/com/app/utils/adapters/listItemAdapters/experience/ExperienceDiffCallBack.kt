package hiretimsf.com.app.utils.adapters.listItemAdapters.experience

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import hiretimsf.com.app.repository.database.model.experience.ExperienceModel

/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter or PagingDataAdapter to calculate the minimum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */

class ExperienceDiffCallBack : DiffUtil.ItemCallback<ExperienceModel>() {
    override fun areItemsTheSame(oldItem: ExperienceModel, newItem: ExperienceModel): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ExperienceModel, newItem: ExperienceModel): Boolean {
        return oldItem == newItem
    }

}