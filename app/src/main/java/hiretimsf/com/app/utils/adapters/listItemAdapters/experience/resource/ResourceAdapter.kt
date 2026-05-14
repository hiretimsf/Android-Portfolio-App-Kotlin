package hiretimsf.com.app.utils.adapters.listItemAdapters.experience.resource

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import hiretimsf.com.app.repository.database.model.resource.ResourceModel
import hiretimsf.com.app.utils.adapters.listItemAdapters.portfolio.button.ButtonClickListener

/**
 * An adapter that provides a list of [ResourceModel] to a [RecyclerView]
 * */

class ResourceAdapter(private val clickListener: ButtonClickListener) :
    PagingDataAdapter<ResourceModel, ResourceViewHolder>(
        ResourceDiffCallBack()
    ) {

    /**
     * Part of the RecyclerView adapter, called when RecyclerView needs a new [ResourceViewHolder]
     *
     * A [ResourceViewHolder] holds the view for the [RecyclerView] as well as providing information
     * to the RecyclerView such as where on the screen it was last drawn during scrolling.
     * */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceViewHolder {
        return ResourceViewHolder.from(parent)
    }

    /**
     * Part of the RecyclerView adapter, called when the RecyclerView needs to show an item.
     *
     * The [ResourceViewHolder] passed may be recycled so make sure that this sets any properties
     * that may be have been set previously
     * */
    override fun onBindViewHolder(holder: ResourceViewHolder, position: Int) {
        val resourceItem = getItem(position)
        holder.bind(resourceItem, clickListener)
    }
}