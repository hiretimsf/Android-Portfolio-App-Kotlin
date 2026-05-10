package me.tumur.portfolio.utils.adapters.listItemAdapters.portfolio.button

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.tumur.portfolio.databinding.ListItemButtonOutlineBinding
import me.tumur.portfolio.repository.database.model.button.ButtonModel
import me.tumur.portfolio.utils.adapters.bindingAdapters.setButtonIcon

/**
 * Button normal viewholder
 * */
class ButtonOutlineViewHolder private constructor(val binding: ListItemButtonOutlineBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ButtonModel, clickListener: ButtonClickListener) {
        binding.listItemMaterialButtonOutline.text = item.title
        binding.listItemMaterialButtonOutline.setButtonIcon(item.type)
        binding.listItemMaterialButtonOutline.setOnClickListener { clickListener.onClick(item.url) }
    }

    companion object {
        fun from(parent: ViewGroup): ButtonOutlineViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemButtonOutlineBinding.inflate(layoutInflater, parent, false)
            return ButtonOutlineViewHolder(binding)
        }
    }
}
