package me.tumur.portfolio.utils.adapters.listItemAdapters.experience

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.tumur.portfolio.databinding.ListItemExperienceBinding
import me.tumur.portfolio.repository.database.model.experience.ExperienceModel
import me.tumur.portfolio.utils.adapters.bindingAdapters.loadImage
import me.tumur.portfolio.utils.adapters.bindingAdapters.setDateFromTo

/**
 * Experience item viewholder
 * */
class ExperienceViewHolder private constructor(val binding: ListItemExperienceBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ExperienceModel?, clickListener: ExperienceClickListener) {
        binding.listItemExperienceCardView.setOnClickListener {
            item?.let(clickListener::onClick)
        }
        binding.listItemExperienceLogo.contentDescription = item?.logoDescription
        loadImage(binding.listItemExperienceLogo, item?.logo)
        binding.listItemExperienceJobTitle.text = item?.title
        binding.listItemExperienceCompanyName.text = item?.company
        binding.listItemExperienceLocationName.text = item?.location
        binding.listItemExperienceDate.setDateFromTo(item?.dateFrom, item?.dateTo)
    }

    companion object {
        fun from(parent: ViewGroup): ExperienceViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemExperienceBinding.inflate(layoutInflater, parent, false)
            return ExperienceViewHolder(binding)
        }
    }
}
