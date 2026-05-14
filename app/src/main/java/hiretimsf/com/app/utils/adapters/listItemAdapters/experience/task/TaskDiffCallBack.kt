package hiretimsf.com.app.utils.adapters.listItemAdapters.experience.task

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import hiretimsf.com.app.repository.database.model.task.TaskModel

/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter or PagingDataAdapter to calculate the minimum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */

class TaskDiffCallBack : DiffUtil.ItemCallback<TaskModel>() {
    override fun areItemsTheSame(oldItem: TaskModel, newItem: TaskModel): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: TaskModel, newItem: TaskModel): Boolean {
        return oldItem == newItem
    }

}