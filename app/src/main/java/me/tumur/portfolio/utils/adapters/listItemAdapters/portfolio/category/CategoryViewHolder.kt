package me.tumur.portfolio.utils.adapters.listItemAdapters.portfolio.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.tumur.portfolio.databinding.ListItemCategoryBinding
import me.tumur.portfolio.repository.database.model.category.CategoryModel
import me.tumur.portfolio.utils.adapters.bindingAdapters.setCategoryIcon

/**
 * Category viewholder
 * */
class CategoryViewHolder private constructor(val binding: ListItemCategoryBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: CategoryModel?) {
        binding.listItemCategoryIcon.contentDescription = item?.iconDescription
        binding.listItemCategoryIcon.setCategoryIcon(item?.icon)
        binding.listItemCategoryTitle.text = item?.title
    }

    companion object {
        fun from(parent: ViewGroup): CategoryViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemCategoryBinding.inflate(layoutInflater, parent, false)
            return CategoryViewHolder(binding)
        }
    }
}
