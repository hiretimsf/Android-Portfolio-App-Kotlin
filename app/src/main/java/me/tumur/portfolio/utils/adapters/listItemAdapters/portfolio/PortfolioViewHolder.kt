package me.tumur.portfolio.utils.adapters.listItemAdapters.portfolio

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.tumur.portfolio.databinding.ListItemPortfolioBinding
import me.tumur.portfolio.repository.database.model.portfolio.PortfolioModel
import me.tumur.portfolio.utils.adapters.bindingAdapters.loadImage
import me.tumur.portfolio.utils.adapters.bindingAdapters.setDateFromTo

/**
 * Portfolio item viewholder
 * */
class PortfolioViewHolder private constructor(val binding: ListItemPortfolioBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(item: PortfolioModel?, clickListener: PortfolioClickListener){
        binding.listItemPortfolioCardView.setOnClickListener {
            item?.let(clickListener::onClick)
        }
        binding.listItemPortfolioImage.contentDescription = item?.imageDescription
        loadImage(binding.listItemPortfolioImage, item?.coverImage)
        binding.listItemPortfolioLogo.contentDescription = item?.logoDescription
        loadImage(binding.listItemPortfolioLogo, item?.logo)
        binding.listItemPortfolioSubTitle.text = item?.subTitle
        binding.listItemPortfolioText.text = item?.text
        binding.listItemPortfolioDate.setDateFromTo(item?.dateFrom, item?.dateTo)
    }

    companion object {
        fun from(parent: ViewGroup): PortfolioViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemPortfolioBinding.inflate(layoutInflater, parent, false)
            return PortfolioViewHolder(binding)
        }
    }
}
