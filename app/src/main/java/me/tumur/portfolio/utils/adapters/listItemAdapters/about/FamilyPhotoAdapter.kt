package me.tumur.portfolio.utils.adapters.listItemAdapters.about

import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import me.tumur.portfolio.R

class FamilyPhotoAdapter(
    private val images: List<CarouselImage>,
) : RecyclerView.Adapter<FamilyPhotoAdapter.FamilyPhotoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FamilyPhotoViewHolder {
        val imageView = AppCompatImageView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
            scaleType = ImageView.ScaleType.FIT_CENTER
        }
        return FamilyPhotoViewHolder(imageView)
    }

    override fun onBindViewHolder(holder: FamilyPhotoViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int = images.size

    class FamilyPhotoViewHolder(
        private val imageView: AppCompatImageView,
    ) : RecyclerView.ViewHolder(imageView) {
        fun bind(image: CarouselImage) {
            imageView.contentDescription = image.description
            imageView.load(image.url) {
                placeholder(R.drawable.profile)
                error(R.drawable.profile)
            }
        }
    }
}
