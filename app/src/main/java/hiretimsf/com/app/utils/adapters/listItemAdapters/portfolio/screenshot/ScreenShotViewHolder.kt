package hiretimsf.com.app.utils.adapters.listItemAdapters.portfolio.screenshot

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hiretimsf.com.app.databinding.ListItemScreenshotBinding
import hiretimsf.com.app.repository.database.model.screenshot.ScreenShotModel
import hiretimsf.com.app.utils.adapters.bindingAdapters.loadImage

/**
 * Screenshot viewholder
 * */
class ScreenShotViewHolder private constructor(val binding: ListItemScreenshotBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ScreenShotModel?, clickListener: ScreenShotClickListener) {
        binding.listItemScreenshot.setOnClickListener {
            item?.let(clickListener::onClick)
        }
        binding.listItemScreenshotImage.contentDescription = item?.imageDescription
        loadImage(binding.listItemScreenshotImage, item?.url)
    }

    companion object {
        fun from(parent: ViewGroup): ScreenShotViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemScreenshotBinding.inflate(layoutInflater, parent, false)
            return ScreenShotViewHolder(binding)
        }
    }
}
