package hiretimsf.com.app.utils.images

import android.widget.ImageView
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import hiretimsf.com.app.R

fun ImageView.showWelcomePagerIcon(
    pageOrder: Int,
    selectedPage: Int,
    page: Int,
) {
    val iconRes = when (pageOrder) {
        1 -> R.drawable.ic_welcome_screen_icon_1_avd
        2 -> R.drawable.ic_welcome_screen_icon_2_avd
        else -> R.drawable.ic_welcome_screen_icon_3_avd
    }

    if (selectedPage != page) {
        setImageResource(iconRes)
        return
    }

    val animatedIcon = AnimatedVectorDrawableCompat.create(context, iconRes)
    if (animatedIcon == null) {
        setImageResource(iconRes)
        return
    }

    setImageDrawable(animatedIcon)
    animatedIcon.start()
}
