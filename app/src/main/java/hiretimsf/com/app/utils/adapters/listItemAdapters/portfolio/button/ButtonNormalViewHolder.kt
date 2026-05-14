package hiretimsf.com.app.utils.adapters.listItemAdapters.portfolio.button

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hiretimsf.com.app.databinding.ListItemButtonBinding
import hiretimsf.com.app.repository.database.model.button.ButtonModel
import hiretimsf.com.app.utils.adapters.bindingAdapters.setButtonIcon

/**
 * Button normal viewholder
 * */
class ButtonNormalViewHolder private constructor(val binding: ListItemButtonBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ButtonModel, clickListener: ButtonClickListener) {
        binding.listItemMaterialButton.text = item.title
        binding.listItemMaterialButton.setButtonIcon(item.type)
        binding.listItemMaterialButton.setOnClickListener { clickListener.onClick(item.url) }
    }

    companion object {
        fun from(parent: ViewGroup): ButtonNormalViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemButtonBinding.inflate(layoutInflater, parent, false)
            return ButtonNormalViewHolder(binding)
        }
    }
}
