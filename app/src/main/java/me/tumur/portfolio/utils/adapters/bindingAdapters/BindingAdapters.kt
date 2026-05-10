package me.tumur.portfolio.utils.adapters.bindingAdapters

import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.IdRes
import coil.load
import coil.transform.RoundedCornersTransformation
import coil.transform.Transformation
import coil.size.Size
import com.google.android.material.button.MaterialButton
import me.tumur.portfolio.R
import me.tumur.portfolio.utils.constants.BsConstants
import me.tumur.portfolio.utils.constants.Constants
import java.text.SimpleDateFormat
import java.util.*

private class GrayscaleTransformation : Transformation {
    override val cacheKey = "me.tumur.portfolio.GrayscaleTransformation"

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

/** Load image from the network or cache with placeholder and error images */
fun loadImage(imageView: ImageView, url: String?) {
    url?.let {
        imageView.load(it) {
            crossfade(true)
            placeholder(R.color.colorBorder)
            when (imageView.context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_NO -> {
                    transformations(RoundedCornersTransformation(0.0F))
                } // Night mode is not active, we're using the light theme
                Configuration.UI_MODE_NIGHT_YES -> {
                    transformations(GrayscaleTransformation())
                } // Night mode is active, we're using dark theme
            }
        }
    }
}

/** Load image from the network or cache with placeholder and error images */
fun setImageDrawable(imageView: ImageView, @IdRes drawable: Int?) {
    drawable?.let {
        imageView.load(it) {
            crossfade(true)
            placeholder(R.color.colorBorder)
            when (imageView.context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_NO -> {
                    transformations(RoundedCornersTransformation(0.0F))
                } // Night mode is not active, we're using the light theme
                Configuration.UI_MODE_NIGHT_YES -> {
                    transformations(GrayscaleTransformation())
                } // Night mode is active, we're using dark theme
            }
        }
    }
}

/** Date from and date to */
fun TextView.setDateFromTo(dateFrom: Date?, dateTo: Date?) {
    if (dateFrom != null && dateTo != null) {

        if (dateFrom.compareTo(dateTo) == 0) {
            val outputFormat = SimpleDateFormat("MMM yyyy", Locale.US)
            val a = outputFormat.format(dateFrom)
            text = a
        } else {

            val outputFormat = SimpleDateFormat("MMM yyyy", Locale.US)
            val a = outputFormat.format(dateFrom)
            val b = outputFormat.format(dateTo)

            val start = Calendar.getInstance().apply { time = dateFrom }
            val end = Calendar.getInstance().apply { time = dateTo }
            val diff = (end.get(Calendar.YEAR) - start.get(Calendar.YEAR)) * 12L +
                (end.get(Calendar.MONTH) - start.get(Calendar.MONTH))
            val diffYear = diff / 12
            val diffMonth = diff % 12
            val d = if (diffYear > 0) "$diffYear.$diffMonth year(s)" else "$diffMonth month(s)"

            val result = "$a - $b | $d"
            text = result
        }
    }
}

/** Social icon */
fun ImageView.setSocialIcon(name: String?) {
    name?.let {
        setImageResource( when(name){
            BsConstants.GITHUB -> R.drawable.ic_github
            BsConstants.LINKEDIN -> R.drawable.ic_linkedin
            BsConstants.TWITTER -> R.drawable.ic_twitter
            BsConstants.PDF -> R.drawable.ic_pdf
            else -> R.drawable.ic_globe
        })
    }
}

/** Category icon */
fun ImageView.setCategoryIcon(icon: String?) {
    icon?.let {
        setImageResource(
            when (icon) {
                Constants.CATEGORY_ANDROID -> R.drawable.ic_category_android
                Constants.CATEGORY_WEB -> R.drawable.ic_category_web
                Constants.CATEGORY_CODE -> R.drawable.ic_category_code
                else -> R.drawable.ic_category_structure
            }
        )
    }
}

fun MaterialButton.setButtonIcon(type: String?) {
    type?.let {
        setIconResource(
            when (type) {
                Constants.BUTTON_GITHUB -> R.drawable.ic_github
                Constants.BUTTON_GOOGLE -> R.drawable.ic_play_store
                Constants.BUTTON_WEB -> R.drawable.ic_category_web
                Constants.BUTTON_YOUTUBE -> R.drawable.ic_youtube
                Constants.BUTTON_TWITTER -> R.drawable.ic_twitter
                else -> R.drawable.ic_pdf
            }
        )
    }
}
