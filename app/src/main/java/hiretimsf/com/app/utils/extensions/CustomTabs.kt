package hiretimsf.com.app.utils.extensions

import android.content.Context
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import hiretimsf.com.app.R

fun Context.launchCustomTab(url: String) {
    val colorSchemeParams = CustomTabColorSchemeParams.Builder()
        .setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
        .build()

    CustomTabsIntent.Builder()
        .setDefaultColorSchemeParams(colorSchemeParams)
        .setShowTitle(true)
        .build()
        .launchUrl(this, url.toUri())
}
