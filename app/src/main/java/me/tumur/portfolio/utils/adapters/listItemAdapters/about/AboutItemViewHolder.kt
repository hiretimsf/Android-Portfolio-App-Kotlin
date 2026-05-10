package me.tumur.portfolio.utils.adapters.listItemAdapters.about

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.tumur.portfolio.R
import me.tumur.portfolio.databinding.ListItemAboutBinding

/**
 * About item viewholder
 * */
class AboutItemViewHolder private constructor(val binding: ListItemAboutBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(item: AboutItem.About){
        val context = binding.root.context
        val name = context.getString(R.string.name)
        val title = context.getString(R.string.title)
        binding.aboutListItemText.text = item.about.text
            .replace(
                "My name is Tumur Bazarragchaa. You can call me Alex. I am an Android developer",
                "My name is $name. I am a $title",
            )
            .replace("Tumur Bazarragchaa", name)
            .replace("Tumur.B (Alex)", name)
            .replace("Tumur.B(Alex)", name)
            .replace("an Android Developer", "a $title")
            .replace("an Android developer", "a $title")
            .replace("Android Developer", title)
            .replace("Android developer", title)
    }

    companion object {
        fun from(parent: ViewGroup): AboutItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemAboutBinding.inflate(layoutInflater, parent, false)
            return AboutItemViewHolder(binding)
        }
    }
}
