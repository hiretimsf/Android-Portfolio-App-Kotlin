package me.tumur.portfolio.utils.adapters.listItemAdapters.about

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.tumur.portfolio.databinding.ListItemAboutHeaderBinding

/**
 * About item's header viewholder
 * */
class AboutHeaderViewHolder private constructor(val binding: ListItemAboutHeaderBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(header: AboutItem.Header){
        binding.aboutListItemHeaderTitle.text = header.header
    }

    companion object {
        fun from(parent: ViewGroup): AboutHeaderViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemAboutHeaderBinding.inflate(layoutInflater, parent, false)
            return AboutHeaderViewHolder(binding)
        }
    }
}
