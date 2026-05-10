package me.tumur.portfolio.utils.adapters.listItemAdapters.about

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.tumur.portfolio.databinding.ListItemAboutBinding

/**
 * About item viewholder
 * */
class AboutItemViewHolder private constructor(val binding: ListItemAboutBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(item: AboutItem.About){
        binding.aboutListItemText.text = item.about.content
    }

    companion object {
        fun from(parent: ViewGroup): AboutItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemAboutBinding.inflate(layoutInflater, parent, false)
            return AboutItemViewHolder(binding)
        }
    }
}
