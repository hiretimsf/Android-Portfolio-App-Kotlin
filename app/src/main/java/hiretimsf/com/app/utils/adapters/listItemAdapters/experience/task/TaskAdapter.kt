package hiretimsf.com.app.utils.adapters.listItemAdapters.experience.task

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import hiretimsf.com.app.repository.database.model.task.TaskModel

/**
 * An adapter that provides a list of [TaskModel] to a [RecyclerView]
 * */

class TaskAdapter : PagingDataAdapter<TaskModel, TaskViewHolder>(TaskDiffCallBack()) {

    /**
     * Part of the RecyclerView adapter, called when RecyclerView needs a new [TaskViewHolder]
     *
     * A [TaskViewHolder] holds the view for the [RecyclerView] as well as providing information
     * to the RecyclerView such as where on the screen it was last drawn during scrolling.
     * */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder.from(parent)
    }

    /**
     * Part of the RecyclerView adapter, called when the RecyclerView needs to show an item.
     *
     * The [TaskViewHolder] passed may be recycled so make sure that this sets any properties
     * that may be have been set previously
     * */
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}