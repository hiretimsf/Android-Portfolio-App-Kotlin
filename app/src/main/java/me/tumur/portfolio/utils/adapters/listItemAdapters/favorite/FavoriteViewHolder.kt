package me.tumur.portfolio.utils.adapters.listItemAdapters.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.tumur.portfolio.databinding.ListItemFavoriteBinding
import me.tumur.portfolio.repository.database.model.favorite.FavoriteModel
import me.tumur.portfolio.utils.adapters.bindingAdapters.loadImage
import me.tumur.portfolio.utils.adapters.bindingAdapters.setDateConverter

/**
 * Favorite item viewholder
 * */
class FavoriteViewHolder private constructor(val binding: ListItemFavoriteBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: FavoriteModel?, clickListener: FavoriteClickListener) {
        binding.listItemFavoriteCardView.setOnClickListener {
            item?.let { favorite -> clickListener.onClick(favorite, false) }
        }
        binding.listItemFavoriteDeleteButton.setOnClickListener {
            item?.let { favorite -> clickListener.onClick(favorite, true) }
        }
        binding.listItemFavoriteImage.contentDescription = item?.imageDescription
        loadImage(binding.listItemFavoriteImage, item?.coverImage)
        binding.listItemFavoriteLogo.contentDescription = item?.logoDescription
        loadImage(binding.listItemFavoriteLogo, item?.logo)
        binding.listItemFavoriteSubTitle.text = item?.subTitle
        binding.listItemFavoriteText.text = item?.text
        binding.listItemFavoriteDate.setDateConverter(item?.date)
    }

    companion object {
        fun from(parent: ViewGroup): FavoriteViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemFavoriteBinding.inflate(layoutInflater, parent, false)
            return FavoriteViewHolder(binding)
        }
    }
}
