package me.tumur.portfolio.utils.adapters.bindingAdapters

import android.view.View
import android.view.animation.AnimationUtils
import androidx.databinding.BindingAdapter
import androidx.viewpager.widget.ViewPager
import me.tumur.portfolio.R
import me.tumur.portfolio.screens.portfolio.detail.preview.PreviewFragmentViewModel
import me.tumur.portfolio.utils.state.PreviewImage
import me.tumur.portfolio.utils.state.PreviewState
import me.tumur.portfolio.utils.state.ProgressBar

/** BINDING ADAPTERS FOR PREVIEW SCREEN */

/** OnPage listener */
@BindingAdapter("onScreenShotScrolled")
fun setPreviewViewPagerPageChangeListener(viewPager: ViewPager, viewModel: PreviewFragmentViewModel) {
    if (viewPager.getTag(R.id.tag_preview_page_listener) == viewModel) return
    viewPager.setTag(R.id.tag_preview_page_listener, viewModel)
    viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            viewModel.setCurrentItem(position)
        }

        override fun onPageScrollStateChanged(state: Int) {}
        override fun onPageSelected(position: Int) {}
    })
}

/** Current item */
@BindingAdapter("scrollToScreenShot", "smoothScrollScreenShot", requireAll = false)
fun setPreviewViewPagerCurrentItem(viewPager: ViewPager, scrollToItem: Int?, smoothScroll: Boolean = false) {
    scrollToItem?.let { viewPager.setCurrentItem(it, smoothScroll) }
}

/** Progress bar */
@BindingAdapter("screenPreviewProgressBar")
fun setPreviewProgressBar(view: View, state: PreviewState?) {
    val fadeIn = AnimationUtils.loadAnimation(view.context, R.anim.fade_in)
    val fadeOut = AnimationUtils.loadAnimation(view.context, R.anim.fade_out)
    when (state != null && state is ProgressBar) {
        true -> {
            view.visibility = View.VISIBLE
            view.startAnimation(fadeIn)
        }
        false -> {
            view.visibility = View.GONE
            view.startAnimation(fadeOut)
        }
    }
}

/** Preview image */
@BindingAdapter("screenPreviewImage")
fun setPreviewImage(view: View, state: PreviewState?) {
    val fadeIn = AnimationUtils.loadAnimation(view.context, R.anim.fade_in)
    val fadeOut = AnimationUtils.loadAnimation(view.context, R.anim.fade_out)
    when (state != null && state is PreviewImage) {
        true -> {
            view.visibility = View.VISIBLE
            view.startAnimation(fadeIn)
        }
        false -> {
            view.visibility = View.GONE
            view.startAnimation(fadeOut)
        }
    }
}
