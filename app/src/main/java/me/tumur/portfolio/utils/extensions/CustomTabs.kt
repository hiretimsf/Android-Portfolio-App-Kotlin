package me.tumur.portfolio.utils.extensions

import android.content.Context
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import me.tumur.portfolio.R

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
