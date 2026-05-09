package me.tumur.portfolio.screens

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.tumur.portfolio.R
import me.tumur.portfolio.databinding.ActivityMainBinding
import me.tumur.portfolio.utils.constants.Constants
import me.tumur.portfolio.utils.extensions.activityBinding
import me.tumur.portfolio.utils.extensions.collectFlow
import me.tumur.portfolio.utils.state.*


/**
 * An activity that inflates a layout that has [BottomNavigationView] and [NavigationView].
 * This is a single activity application that uses the Navigation library.
 * Content is displayed by Fragments.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    /** VARIABLES * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /** ViewModel */

    /**
     * Returns a property delegate to access ViewModel
     * by default scoped to this Activity:
     * Default scope may be overridden with parameter ownerProducer:
     * This property can be accessed only after
     * this Activity is created i.e.,after Activity.onCreate,
     * and access prior to that will result in IllegalArgumentException.
     * */
    private val viewModel: MainViewModel by viewModels()

    /** Databinding */
    private val binding by activityBinding<ActivityMainBinding>(R.layout.activity_main)

    /** Navigation controller */
    private lateinit var navController: NavController

    /** Coroutine scope for delayed initialization */
    private val activityScope = CoroutineScope(Dispatchers.Default)

    /** Shared preference */
    private val prefs: SharedPreferences by lazy {
        this.getSharedPreferences(Constants.APP, Context.MODE_PRIVATE)
    }

    /** Shared preference listener */
    private var prefsListener: SharedPreferences.OnSharedPreferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            when (key) {
                Constants.FIRST -> {
                    if(!prefs.getBoolean(Constants.FIRST, true) && viewModel.navigation == HideNavigation)
                        viewModel.setNavigationState(ShowNavigation)
                }
            }
        }

    /** INITIALIZATION * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Called when the activity is starting.
     * This is where most initialization should go.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { viewModel.screenState is SplashScreen }

        /** Binding lifecycle and viewmodel to layout */
        binding.apply {
            this.lifecycleOwner = this@MainActivity
            this.model = viewModel
        }

        /** Binding navigation controller with host fragment */
        navController = findNavController(R.id.main_screen_host_fragment)

        /** Set action bar */
        setSupportActionBar(binding.root.findViewById(R.id.main_screen_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        /** Delayed initialization */
        delayedInit()

        /** Set observers*/
        setObservers()
    }

    /**
     * Called when the activity is resuming.
     * This is where it's starting to observe
     * the shared preference value change
     */
    override fun onResume() {
        super.onResume()
        prefs.registerOnSharedPreferenceChangeListener(prefsListener)
    }

    /**
     * Called when the activity is resuming.
     * This is where it's stopping to observe
     * the shared preference value change
     */
    override fun onPause() {
        super.onPause()
        prefs.unregisterOnSharedPreferenceChangeListener(prefsListener)
    }

    /** FUNCTIONS * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /** Set delayed initialization */
    private fun delayedInit(){
        activityScope.launch {
            setBottomMenu()
            setSideMenu()
        }
    }

    /** Set observers */
    private fun setObservers() {

        /** Setup welcome screen observer  */
        collectFlow(viewModel.screenStateFlow) { state ->
            binding.invalidateAll()
            if (state is WelcomeScreen && prefs.getBoolean(Constants.FIRST, true)) {
                navController.navigate(R.id.action_global_to_welcome_screen)
            }
        }

        /** Setup fragment state observer  */
        collectFlow(viewModel.fragmentStateFlow) { state ->
            when(state){
                Constants.FRAGMENT_PROFILE -> navController.navigate(R.id.profile_screen)
                Constants.FRAGMENT_PORTFOLIO -> navController.navigate(R.id.portfolio_screen)
                Constants.FRAGMENT_EXPERIENCE -> navController.navigate(R.id.experience_screen)
                Constants.FRAGMENT_SETTINGS -> navController.navigate(R.id.settings_screen)
                Constants.FRAGMENT_FAVORITE -> navController.navigate(R.id.favorite_screen)
            }
            if (state != null) viewModel.clearFragmentState()
        }

        collectFlow(viewModel.navigationFlow) {
            binding.invalidateAll()
        }

        /** Set observer for a toast message */
        collectFlow(viewModel.showToastFlow) {
            when (it) {
                ToastShow -> showToastMessage()
                else -> Unit
            }
        }
    }

    /** Setup bottom menu */
    private fun setBottomMenu(){
        // Variables
        val navigationGraphTopLevel =
            setOf(R.id.profile_screen, R.id.portfolio_screen, R.id.experience_screen, R.id.settings_screen)
        // Application bar configuration for navigationController
        val appBarConfiguration = AppBarConfiguration(navigationGraphTopLevel)
        // Setup bottom menu
        binding.root.findViewById<BottomNavigationView>(R.id.main_screen_bottom_menu)?.apply {
            // Show the true colors of menu icons
            this.itemIconTintList = null
            // Setup navController with bottom menu and toolbar
            this.setupWithNavController(navController)
        }
        binding.root.findViewById<MaterialToolbar>(R.id.main_screen_toolbar)
            ?.setupWithNavController(navController, appBarConfiguration)
    }

    /** Setup side menu */
    private fun setSideMenu(){
        // Variables
        val navigationGraphTopLevel =
            setOf(R.id.profile_screen, R.id.portfolio_screen, R.id.experience_screen, R.id.settings_screen)
        val drawerLayout = findViewById<DrawerLayout>(R.id.main_screen_drawer_layout)
        // Application bar configuration for navigationController
        val appBarConfiguration = AppBarConfiguration(navigationGraphTopLevel, drawerLayout)
        // Setup side menu
        binding.root.findViewById<NavigationView>(R.id.main_screen_side_menu)?.apply {
            // Show the true colors of menu icons
            this.itemIconTintList = null
            // Setup navController with side menu with drawerlayout and toolbar
            this.setupWithNavController(navController)
        }
        binding.root.findViewById<MaterialToolbar>(R.id.main_screen_toolbar)
            ?.setupWithNavController(navController, appBarConfiguration)
    }

    /** Show toast message */
    private fun showToastMessage() {
        /** Parameters for toast message */
        val toastBg = ContextCompat.getColor(this, R.color.colorPrimary)
        val toastTextColor = ContextCompat.getColor(this, R.color.colorOnPrimary)
        val toastIcon = ContextCompat.getDrawable(this, R.drawable.ic_no_connection)
        val toastMessage = this.getString(R.string.toast_failed)
        /** Show toast message */
        Toasty.custom(this, toastMessage, toastIcon, toastBg, toastTextColor, Toasty.LENGTH_SHORT, true, true).show()
        /** Reset toast message observer value */
        viewModel.setShowToast(ToastEmpty)
    }
}
