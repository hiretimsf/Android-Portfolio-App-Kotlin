package hiretimsf.com.app

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import cat.ereza.customactivityoncrash.config.CaocConfig
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import dagger.hilt.android.HiltAndroidApp
import hiretimsf.com.app.screens.MainActivity
import hiretimsf.com.app.utils.constants.Constants
import hiretimsf.com.app.utils.privacy.DataDeletionIdentifier
import hiretimsf.com.app.utils.theme.ThemeHelper
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class App : Application(), ImageLoaderFactory {

    /**
     * onCreate is called before the first screen is shown to the user.
     *
     * Use it to setup any background tasks, running expensive setup operations in a background
     * thread to avoid delaying app start.
     */

    override fun onCreate() {
        super.onCreate()

        CaocConfig.Builder.create()
            .restartActivity(MainActivity::class.java)
            .apply()

        DataDeletionIdentifier.applyToFirebase(this)

        /**
         * THEME SETTINGS
         * */
        /** Shared preferences */
        val sharedPref: SharedPreferences = getSharedPreferences(Constants.APP, Context.MODE_PRIVATE)
        val theme =
            sharedPref.getString(resources.getString(R.string.preference_key_theme_option), Constants.THEME_DEFAULT)
        theme?.let {
            ThemeHelper.applyTheme(it)
        }
    }

    override fun newImageLoader(): ImageLoader {
        val coilOkhttpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .build()

        return ImageLoader(this).newBuilder()
            .memoryCachePolicy(CachePolicy.ENABLED)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25)
                    .strongReferencesEnabled(true)
                    .build()
            }
            .diskCachePolicy(CachePolicy.ENABLED)
            .diskCache {
                DiskCache.Builder()
                    .maxSizePercent(0.05)
                    .directory(cacheDir)
                    .build()
            }
            .okHttpClient(coilOkhttpClient)
            .logger(DebugLogger())
            .build()
    }
}
