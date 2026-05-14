package hiretimsf.com.app.utils.adapters.bindingAdapters

import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import hiretimsf.com.app.R
import hiretimsf.com.app.utils.state.Empty
import hiretimsf.com.app.utils.state.FavoriteState
import hiretimsf.com.app.utils.state.NotEmpty
import java.text.SimpleDateFormat
import java.util.*

/** Empty Screen */
fun setScreenFavoriteEmpty(view: View, screen: FavoriteState?) {
    val fadeIn = AnimationUtils.loadAnimation(view.context, R.anim.fade_in)
    val fadeOut = AnimationUtils.loadAnimation(view.context, R.anim.fade_out)
    when (screen != null && screen is Empty) {
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

/** Not Empty Screen */
fun setScreenFavoriteNotEmpty(view: View, screen: FavoriteState?) {
    val fadeIn = AnimationUtils.loadAnimation(view.context, R.anim.fade_in)
    val fadeOut = AnimationUtils.loadAnimation(view.context, R.anim.fade_out)
    when (screen != null && screen is NotEmpty) {
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

/** Date converter */
fun TextView.setDateConverter(date: Date?) {
    date?.let {
        val outputFormat = SimpleDateFormat("MMMM yyyy", Locale.US)
        val formattedDate = outputFormat.format(it)
        text = formattedDate
    }
}
