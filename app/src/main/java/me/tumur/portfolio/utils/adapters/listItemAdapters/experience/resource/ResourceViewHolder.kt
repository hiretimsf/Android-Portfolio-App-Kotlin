package me.tumur.portfolio.utils.adapters.listItemAdapters.experience.resource

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.tumur.portfolio.databinding.ListItemResourceBinding
import me.tumur.portfolio.repository.database.model.resource.ResourceModel
import me.tumur.portfolio.utils.adapters.bindingAdapters.loadImage
import me.tumur.portfolio.utils.adapters.bindingAdapters.setDateFromTo
import me.tumur.portfolio.utils.adapters.listItemAdapters.portfolio.button.ButtonClickListener

/**
 * Resource item viewholder
 * */
class ResourceViewHolder private constructor(val binding: ListItemResourceBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ResourceModel?, clickListener: ButtonClickListener) {
        binding.listItemResourceCardView.setOnClickListener {
            item?.url?.let(clickListener::onClick)
        }
        binding.listItemResourceImage.contentDescription = item?.imageDescription
        loadImage(binding.listItemResourceImage, item?.image)
        binding.listItemResourceTitle.text = item?.title
        binding.listItemResourceDate.setDateFromTo(item?.dateFrom, item?.dateTo)
    }

    companion object {
        fun from(parent: ViewGroup): ResourceViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemResourceBinding.inflate(layoutInflater, parent, false)
            return ResourceViewHolder(binding)
        }
    }
}
