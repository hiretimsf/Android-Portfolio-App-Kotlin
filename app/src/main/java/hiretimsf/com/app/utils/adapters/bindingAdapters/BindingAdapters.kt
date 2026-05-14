package hiretimsf.com.app.utils.adapters.bindingAdapters

import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.widget.ImageView
import coil.imageLoader
import coil.load
import coil.size.Size
import coil.transform.RoundedCornersTransformation
import coil.transform.Transformation
import hiretimsf.com.app.R

private class GrayscaleTransformation : Transformation {
    override val cacheKey = "hiretimsf.com.app.GrayscaleTransformation"

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val output = input.copy(input.config ?: Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(output)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            colorFilter = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f) })
        }
        canvas.drawBitmap(input, 0f, 0f, paint)
        return output
    }
}

/** Load image from the network or cache with placeholder and dark-mode treatment. */
fun loadImage(imageView: ImageView, url: String?) {
    url?.let {
        imageView.load(it, imageLoader = imageView.context.imageLoader) {
            crossfade(true)
            placeholder(R.color.colorBorder)
            when (imageView.context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_NO -> transformations(RoundedCornersTransformation(0.0F))
                Configuration.UI_MODE_NIGHT_YES -> transformations(GrayscaleTransformation())
            }
        }
    }
}
