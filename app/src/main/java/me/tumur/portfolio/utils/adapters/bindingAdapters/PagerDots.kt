package me.tumur.portfolio.utils.adapters.bindingAdapters

import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import me.tumur.portfolio.R
import com.intuit.sdp.R as SdpR

fun setPagerDots(container: LinearLayout, dotCount: Int, selectedDot: Int) {
    if (container.childCount != dotCount) {
        container.removeAllViews()
        repeat(dotCount) {
            container.addView(View(container.context), dotLayoutParams(container))
        }
    }

    val activeColor = ContextCompat.getColor(container.context, R.color.colorPageIndicatorActive)
    val inactiveColor = ContextCompat.getColor(container.context, R.color.colorPageIndicatorBackground)
    repeat(container.childCount) { index ->
        container.getChildAt(index).background = dotDrawable(if (index == selectedDot) activeColor else inactiveColor)
    }
}

private fun dotLayoutParams(container: LinearLayout): LinearLayout.LayoutParams {
    val size = container.resources.getDimensionPixelSize(SdpR.dimen._8sdp)
    val margin = container.resources.getDimensionPixelSize(SdpR.dimen._4sdp)
    return LinearLayout.LayoutParams(size, size).apply {
        marginStart = margin
        marginEnd = margin
    }
}

private fun dotDrawable(color: Int): GradientDrawable {
    return GradientDrawable().apply {
        shape = GradientDrawable.OVAL
        setColor(color)
    }
}
