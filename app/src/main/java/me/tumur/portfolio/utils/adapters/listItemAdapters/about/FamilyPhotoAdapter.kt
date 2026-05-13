package me.tumur.portfolio.utils.adapters.listItemAdapters.about

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import coil.imageLoader
import coil.load
import me.tumur.portfolio.R

class FamilyPhotoAdapter(
    private val images: List<CarouselImage>,
) : RecyclerView.Adapter<FamilyPhotoAdapter.FamilyPhotoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FamilyPhotoViewHolder {
        val container = FrameLayout(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
        }
        val imageView = AppCompatImageView(parent.context).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT,
            )
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
        val progressBar = ProgressBar(parent.context).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER,
            )
            isIndeterminate = true
        }
        container.addView(imageView)
        container.addView(progressBar)
        return FamilyPhotoViewHolder(container, imageView, progressBar)
    }

    override fun onBindViewHolder(holder: FamilyPhotoViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int = images.size

    class FamilyPhotoViewHolder(
        container: FrameLayout,
        private val imageView: AppCompatImageView,
        private val progressBar: ProgressBar,
    ) : RecyclerView.ViewHolder(container) {
        fun bind(image: CarouselImage) {
            imageView.contentDescription = image.description
            progressBar.visibility = View.VISIBLE
            imageView.load(image.url, imageLoader = imageView.context.imageLoader) {
                placeholder(R.drawable.profile)
                error(R.drawable.profile)
                listener(
                    onSuccess = { _, _ -> progressBar.visibility = View.GONE },
                    onError = { _, _ -> progressBar.visibility = View.GONE },
                    onCancel = { _ -> progressBar.visibility = View.GONE },
                )
            }
        }
    }
}
