package hiretimsf.com.app.screens.settings

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.content.edit
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import hiretimsf.com.app.R
import hiretimsf.com.app.screens.MainViewModel
import hiretimsf.com.app.utils.constants.Constants
import hiretimsf.com.app.utils.extensions.launchCustomTab
import hiretimsf.com.app.utils.theme.ThemeHelper

/**
 * Settings screen rendered with Compose while preserving the same preference keys and actions.
 */
@AndroidEntryPoint
class SettingsFragment : Fragment() {

    /** ViewModel */
    private val sharedViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        /** Set fragment state in shared view model */
        sharedViewModel.setFragmentStateHolder(Constants.FRAGMENT_SETTINGS)

        val sharedPreferences = requireContext().getSharedPreferences(Constants.APP, Context.MODE_PRIVATE)
        val themeKey = getString(R.string.preference_key_theme_option)
        val initialTheme = sharedPreferences.getString(themeKey, Constants.THEME_DEFAULT) ?: Constants.THEME_DEFAULT
        val themeOptions = resources.getStringArray(R.array.pref_theme_labels)
            .zip(resources.getStringArray(R.array.pref_theme_values))
            .map { (label, value) -> ThemeOption(label, value) }
        val appVersion = getAppVersion()

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                var selectedTheme by remember { mutableStateOf(initialTheme) }

                SettingsComposeScreen(
                    appVersion = appVersion,
                    selectedThemeValue = selectedTheme,
                    themeOptions = themeOptions,
                    onThemeSelected = { themeOption ->
                        selectedTheme = themeOption
                        sharedPreferences.edit {
                            putString(themeKey, themeOption)
                        }
                        ThemeHelper.applyTheme(themeOption)
                    },
                    onAppVersionClick = {
                        findNavController().navigate(R.id.action_global_to_app_info_dialog)
                    },
                    onSourceCodeClick = {
                        requireContext().launchCustomTab(Constants.SOURCE_CODE_URL)
                    },
                    onPrivacyClick = {
                        requireContext().launchCustomTab(Constants.PRIVACY_URL)
                    },
                    onRateClick = {
                        openUri("market://details?id=${requireContext().packageName}")
                    },
                    onTwitterClick = {
                        requireContext().launchCustomTab(Constants.TWITTER_URL)
                    },
                    onEmailClick = {
                        openUri("${Constants.MAILTO}${Constants.EMAIL}?subject=${Constants.SUBJECT}")
                    },
                )
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun getAppVersion(): String {
        return requireContext().packageManager
            .getPackageInfo(requireContext().packageName, 0)
            .versionName
            .orEmpty()
    }

    private fun openUri(uri: String) {
        val intent = Intent(Intent.ACTION_VIEW, uri.toUri())
        try {
            startActivity(intent)
        } catch (_: ActivityNotFoundException) {
            Unit
        }
    }
}
