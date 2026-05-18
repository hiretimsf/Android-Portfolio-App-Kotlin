package hiretimsf.com.app.utils.images

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
import coil.transform.Transformation
import hiretimsf.com.app.R

private class GrayscaleTransformation : Transformation {
    override val cacheKey = "hiretimsf.com.app.GrayscaleTransformation"

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val output = input.copy(input.config ?: Bitmap.Config.ARGB_8888, true)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            colorFilter = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f) })
        }

        Canvas(output).drawBitmap(input, 0f, 0f, paint)
        return output
    }
}

fun ImageView.loadRemoteImage(url: String?) {
    if (url.isNullOrBlank()) {
        setImageResource(R.color.colorBorder)
        return
    }

    load(url, imageLoader = context.imageLoader) {
        crossfade(true)
        placeholder(R.color.colorBorder)

        if (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES) {
            transformations(GrayscaleTransformation())
        }
    }
}
