package hiretimsf.com.app.utils.adapters.listItemAdapters.experience

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import hiretimsf.com.app.repository.database.model.experience.ExperienceModel

/**
 * An adapter that provides a list of [ExperienceModel] to a [RecyclerView]
 * */

class ExperienceAdapter(private val clickListener: ExperienceClickListener) :
    PagingDataAdapter<ExperienceModel, ExperienceViewHolder>(
        ExperienceDiffCallBack()
    ) {

    /**
     * Part of the RecyclerView adapter, called when RecyclerView needs a new [ExperienceViewHolder]
     *
     * A [ExperienceViewHolder] holds the view for the [RecyclerView] as well as providing information
     * to the RecyclerView such as where on the screen it was last drawn during scrolling.
     * */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExperienceViewHolder {
        return ExperienceViewHolder.from(parent)
    }

    /**
     * Part of the RecyclerView adapter, called when the RecyclerView needs to show an item.
     *
     * The [ExperienceViewHolder] passed may be recycled so make sure that this sets any properties
     * that may be have been set previously
     * */
    override fun onBindViewHolder(holder: ExperienceViewHolder, position: Int) {
        val experienceItem = getItem(position)
        holder.bind(experienceItem, clickListener)
    }
}