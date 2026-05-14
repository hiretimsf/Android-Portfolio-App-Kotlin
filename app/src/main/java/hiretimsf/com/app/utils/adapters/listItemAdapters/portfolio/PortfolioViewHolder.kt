package hiretimsf.com.app.utils.adapters.listItemAdapters.portfolio

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hiretimsf.com.app.databinding.ListItemPortfolioBinding
import hiretimsf.com.app.repository.database.model.portfolio.PortfolioModel
import hiretimsf.com.app.utils.adapters.bindingAdapters.loadImage
import hiretimsf.com.app.utils.adapters.bindingAdapters.setDateFromTo

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
