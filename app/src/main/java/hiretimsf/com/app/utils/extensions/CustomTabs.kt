package hiretimsf.com.app.utils.extensions

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
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
        .let { customTabsIntent ->
            try {
                customTabsIntent.launchUrl(this, url.toUri())
            } catch (_: ActivityNotFoundException) {
                openUrlCatching(url)
            } catch (_: RuntimeException) {
                openUrlCatching(url)
            }
        }
}

private fun Context.openUrlCatching(url: String) {
    try {
        startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
    } catch (_: RuntimeException) {
        // Ignore unresolved external links; the app should stay open.
    }
}
