package me.tumur.portfolio.utils.adapters.listItemAdapters.about

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import me.tumur.portfolio.databinding.ListItemAboutCarouselBinding

class AboutCarouselViewHolder private constructor(
    private val binding: ListItemAboutCarouselBinding,
) : RecyclerView.ViewHolder(binding.root) {

    private var pageChangeCallback: ViewPager2.OnPageChangeCallback? = null

    fun bind(item: AboutItem.Carousel) {
        pageChangeCallback?.let(binding.aboutCarousel::unregisterOnPageChangeCallback)

        binding.aboutCarousel.adapter = FamilyPhotoAdapter(item.images)
        binding.aboutCarousel.setCurrentItem(0, false)

        fun updateControls(position: Int) {
            val count = item.images.size
            binding.aboutCarouselStatus.text = "${position + 1} / $count"
            binding.aboutCarouselPrevious.isEnabled = position > 0
            binding.aboutCarouselNext.isEnabled = position < count - 1
            binding.aboutCarouselPrevious.alpha = if (position > 0) 1f else 0.35f
            binding.aboutCarouselNext.alpha = if (position < count - 1) 1f else 0.35f
        }

        binding.aboutCarouselPrevious.setOnClickListener {
            val previous = (binding.aboutCarousel.currentItem - 1).coerceAtLeast(0)
            binding.aboutCarousel.setCurrentItem(previous, true)
        }
        binding.aboutCarouselNext.setOnClickListener {
            val next = (binding.aboutCarousel.currentItem + 1).coerceAtMost(item.images.lastIndex)
            binding.aboutCarousel.setCurrentItem(next, true)
        }

        pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateControls(position)
            }
        }.also(binding.aboutCarousel::registerOnPageChangeCallback)

        updateControls(binding.aboutCarousel.currentItem)
    }

    companion object {
        fun from(parent: ViewGroup): AboutCarouselViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemAboutCarouselBinding.inflate(layoutInflater, parent, false)
            return AboutCarouselViewHolder(binding)
        }
    }
}
