package hiretimsf.com.app.utils.adapters.listItemAdapters.experience.task

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hiretimsf.com.app.databinding.ListItemTaskBinding
import hiretimsf.com.app.repository.database.model.task.TaskModel
import java.util.Locale

/**
 * Task viewholder
 * */
class TaskViewHolder private constructor(val binding: ListItemTaskBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: TaskModel?) {
        binding.listItemTaskNumber.text = item?.order?.let { String.format(Locale.US, "%d", it) }
        binding.listItemTaskText.text = item?.task
    }

    companion object {
        fun from(parent: ViewGroup): TaskViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemTaskBinding.inflate(layoutInflater, parent, false)
            return TaskViewHolder(binding)
        }
    }
}
