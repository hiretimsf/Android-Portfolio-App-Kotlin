package me.tumur.portfolio

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import me.tumur.portfolio.repository.network.DbRefresh
import me.tumur.portfolio.utils.constants.Constants
import me.tumur.portfolio.utils.theme.ThemeHelper
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), Configuration.Provider {

    /**
     * onCreate is called before the first screen is shown to the user.
     *
     * Use it to setup any background tasks, running expensive setup operations in a background
     * thread to avoid delaying app start.
     */

    /** Coroutine scope for application background work. */
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()

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

        /**
         * AUTO BACKGROUND PERIODIC SYNC
         * */
        /** Shared preferences */
        if(sharedPref.getBoolean(resources.getString(R.string.preference_key_bg_sync), true)) setDelayedBackgroundSync()

    }

    /**
     * WorkManager
     * On-demand initialization
     * */
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()

    private fun setDelayedBackgroundSync() {
        applicationScope.launch {
            setupBackgroundSyncWork()
        }
    }

    private fun setupBackgroundSyncWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        val repeatingRequest
                = PeriodicWorkRequestBuilder<DbRefresh>(12, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            resources.getString(R.string.preference_key_bg_sync),
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest)
    }
}
