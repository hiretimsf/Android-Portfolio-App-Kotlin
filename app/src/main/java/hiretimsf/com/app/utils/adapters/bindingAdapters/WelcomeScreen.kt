package hiretimsf.com.app.utils.adapters.bindingAdapters

import android.widget.ImageView
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import hiretimsf.com.app.R

/** Pager Adapter Icon */
fun setPagerIcon(view: ImageView, order: Int, scrolledPagerItem: Int, pagerPosition: Int) {
    val pagerIcon = when(order){
        1 -> R.drawable.ic_welcome_screen_icon_1_avd
        2 -> R.drawable.ic_welcome_screen_icon_2_avd
        else -> R.drawable.ic_welcome_screen_icon_3_avd
    }
    val avdPagerIcon = AnimatedVectorDrawableCompat.create(view.context, pagerIcon)!!
    if (scrolledPagerItem == pagerPosition){
        view.setImageDrawable(avdPagerIcon)
        avdPagerIcon.start()
    } else {
        view.setImageResource(pagerIcon)
    }
}
