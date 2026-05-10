package me.tumur.portfolio.utils.toolbar

import android.content.Context
import android.view.View
import android.view.ViewParent
import android.util.AttributeSet
import androidx.constraintlayout.motion.widget.MotionLayout
import com.google.android.material.appbar.AppBarLayout

class CollapsibleToolbar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MotionLayout(context, attrs, defStyleAttr), AppBarLayout.OnOffsetChangedListener {

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        progress = -verticalOffset / appBarLayout?.totalScrollRange?.toFloat()!!
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        findAppBarLayoutParent()?.addOnOffsetChangedListener(this)
    }

    private fun findAppBarLayoutParent(): AppBarLayout? {
        var currentParent: ViewParent? = parent
        while (currentParent is View) {
            if (currentParent is AppBarLayout) return currentParent
            currentParent = currentParent.parent
        }
        return null
    }
}
