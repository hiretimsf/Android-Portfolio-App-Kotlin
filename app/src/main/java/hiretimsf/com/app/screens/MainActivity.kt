package hiretimsf.com.app.screens

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import hiretimsf.com.app.HireTimSfApp
import hiretimsf.com.app.utils.state.SplashScreen
import hiretimsf.com.app.utils.state.ToastShow

/**
 * Single-activity Compose host. App chrome and destinations are rendered through Compose
 * Navigation instead of XML NavHostFragment.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { viewModel.screenState is SplashScreen }

        setContent {
            val toastState = viewModel.showToastFlow.collectAsStateWithLifecycle().value
            if (toastState is ToastShow) {
                Toasty.error(this, getString(hiretimsf.com.app.R.string.toast_failed), Toasty.LENGTH_SHORT).show()
                viewModel.setShowToast(hiretimsf.com.app.utils.state.ToastEmpty)
            }

            HireTimSfApp(viewModel)
        }
    }
}
