package hiretimsf.com.app.utils.adapters.listItemAdapters.portfolio.button

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hiretimsf.com.app.databinding.ListItemButtonOutlineBinding
import hiretimsf.com.app.repository.database.model.button.ButtonModel
import hiretimsf.com.app.utils.adapters.bindingAdapters.setButtonIcon

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
