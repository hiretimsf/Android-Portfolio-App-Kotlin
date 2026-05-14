package hiretimsf.com.app.utils.adapters.bindingAdapters

import android.view.View
import android.view.animation.AnimationUtils
import hiretimsf.com.app.R
import hiretimsf.com.app.utils.state.*

/** BINDING ADAPTERS FOR MAIN SCREEN */

/** Main Screen */
fun setScreenMain(view: View, screen: ScreenState?) {
    val fadeIn = AnimationUtils.loadAnimation(view.context, R.anim.fade_in)
    val fadeOut = AnimationUtils.loadAnimation(view.context, R.anim.fade_out)
    when(screen != null && screen is MainScreen || screen is WelcomeScreen || screen is LoaderScreen){
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

/** Hide or Show Navigation */
fun hideOrShowNavigation(view: View, state :NavigationState) {
    view.visibility = if(state is HideNavigation) View.INVISIBLE else View.VISIBLE
}

/** Hide or Show Bottom Navigation */
fun hideOrShowBottomNav(view: View, state: NavigationState) {
    when (state) {
        is HideNavigation -> view.visibility = View.INVISIBLE
        is ShowNavigation -> view.visibility = View.VISIBLE
        is GoneNavigation -> view.visibility = View.GONE
    }
}
