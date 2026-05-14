package hiretimsf.com.app.utils.adapters.listItemAdapters.portfolio.category

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import hiretimsf.com.app.repository.database.model.category.CategoryModel

/**
 * An adapter that provides a list of [CategoryModel] to a [RecyclerView]
 * */

class CategoryAdapter : PagingDataAdapter<CategoryModel, CategoryViewHolder>(CategoryDiffCallBack()) {

    /**
     * Part of the RecyclerView adapter, called when RecyclerView needs a new [CategoryViewHolder]
     *
     * A [CategoryViewHolder] holds the view for the [RecyclerView] as well as providing information
     * to the RecyclerView such as where on the screen it was last drawn during scrolling.
     * */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder.from(parent)
    }

    /**
     * Part of the RecyclerView adapter, called when the RecyclerView needs to show an item.
     *
     * The [CategoryViewHolder] passed may be recycled so make sure that this sets any properties
     * that may be have been set previously
     * */
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}