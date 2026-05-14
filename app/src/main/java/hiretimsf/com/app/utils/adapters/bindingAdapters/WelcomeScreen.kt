package hiretimsf.com.app.utils.adapters.bindingAdapters

import android.widget.ImageView
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import androidx.viewpager2.widget.ViewPager2
import hiretimsf.com.app.R
import hiretimsf.com.app.screens.welcome.WelcomeViewModel

/** BINDING ADAPTERS FOR WELCOME SCREEN */

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

/** OnPage listener */
fun setViewPagerPageChangeListener(viewPager: ViewPager2, viewModel: WelcomeViewModel) {
    if (viewPager.getTag(R.id.tag_welcome_page_listener) == viewModel) return
    viewPager.setTag(R.id.tag_welcome_page_listener, viewModel)
    viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            viewModel.setCurrentItem(position)
        }
    })
}

/** Current item */
fun setViewPagerCurrentItem(viewPager: ViewPager2, scrollToItem: Int?, smoothScroll: Boolean = false) {
    scrollToItem?.let { viewPager.setCurrentItem(it, smoothScroll)}
}
