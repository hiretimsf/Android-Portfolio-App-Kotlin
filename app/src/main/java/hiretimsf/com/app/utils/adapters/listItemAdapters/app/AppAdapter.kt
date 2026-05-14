package hiretimsf.com.app.utils.adapters.listItemAdapters.app

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hiretimsf.com.app.repository.database.model.settings.AppModel

/**
 * An adapter that provides a list of [AppModel] to a [RecyclerView]
 * */

class AppAdapter: ListAdapter<AppModel, AppViewHolder>(AppDiffCallBack()) {

    /**
     * Part of the RecyclerView adapter, called when RecyclerView needs a new [AppViewHolder]
     *
     * A [AppViewHolder] holds the view for the [RecyclerView] as well as providing information
     * to the RecyclerView such as where on the screen it was last drawn during scrolling.
     * */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return AppViewHolder.from(parent)
    }

    /**
     * Part of the RecyclerView adapter, called when the RecyclerView needs to show an item.
     *
     * The [AppViewHolder] passed may be recycled so make sure that this sets any properties
     * that may be have been set previously
     * */
    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val appInfoItem = getItem(position)
        holder.bind(appInfoItem)
    }
}