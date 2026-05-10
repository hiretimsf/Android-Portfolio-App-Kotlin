package me.tumur.portfolio.utils.adapters.listItemAdapters.app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.tumur.portfolio.databinding.ListItemDialogAppInfoBinding
import me.tumur.portfolio.repository.database.model.settings.AppModel

/**
 * Constructor of ViewHolder
 * */
class AppViewHolder private constructor(val binding: ListItemDialogAppInfoBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(appInfoItem: AppModel){
        binding.listItemDialogAppInfoTitle.text = appInfoItem.title
        binding.listItemDialogAppInfoText.text = appInfoItem.text
    }

    companion object {
        fun from(parent: ViewGroup): AppViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemDialogAppInfoBinding.inflate(layoutInflater, parent, false)
            return AppViewHolder(binding)
        }
    }
}
