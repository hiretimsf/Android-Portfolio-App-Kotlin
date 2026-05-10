package me.tumur.portfolio.utils.adapters.listItemAdapters.social

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.tumur.portfolio.databinding.ListItemBsSocialBinding
import me.tumur.portfolio.repository.database.model.profile.SocialModel
import me.tumur.portfolio.utils.adapters.bindingAdapters.setSocialIcon

/**
 * Constructor of ViewHolder
 * */
class SocialViewHolder private constructor(val binding: ListItemBsSocialBinding) : RecyclerView.ViewHolder(binding.root){

    fun bind(socialItem: SocialModel, clickListener: SocialClickListener){
        binding.listItemBsSocial.setOnClickListener { clickListener.onClick(socialItem) }
        binding.listItemBsSocialIcon.setSocialIcon(socialItem.name)
        binding.listItemBsSocialName.text = socialItem.name
    }

    companion object {
        fun from(parent: ViewGroup): SocialViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemBsSocialBinding.inflate(layoutInflater, parent, false)
            return SocialViewHolder(
                binding
            )
        }
    }
}
