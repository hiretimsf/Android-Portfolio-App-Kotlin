package hiretimsf.com.app.screens.settings

import android.os.Bundle
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.Preference.OnPreferenceChangeListener
import androidx.preference.PreferenceFragmentCompat
import dagger.hilt.android.AndroidEntryPoint
import hiretimsf.com.app.R
import hiretimsf.com.app.R.string
import hiretimsf.com.app.R.xml
import hiretimsf.com.app.screens.MainViewModel
import hiretimsf.com.app.utils.constants.Constants
import hiretimsf.com.app.utils.extensions.launchCustomTab
import hiretimsf.com.app.utils.theme.ThemeHelper


/**
 * An fragment that inflates a settings preferences xml.
 */

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    /** VARIABLES * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /** ViewModel */

    /**
     * Returns a property delegate to access ViewModel
     * by default scoped to this Fragment:
     * Default scope may be overridden with parameter ownerProducer:
     * This property can be accessed only after
     * this Fragment is attached i.e.,after Fragment.onAttach,
     * and access prior to that will result in IllegalArgumentException.
     * */
    private val sharedViewModel: MainViewModel by activityViewModels()

    /** INITIALIZATION * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        /** Set fragment state in shared view model */
        sharedViewModel.setFragmentStateHolder(Constants.FRAGMENT_SETTINGS)

        /** Set name for shared preference */
        preferenceManager.sharedPreferencesName = Constants.APP

        /** Set resource for preference screen */
        setPreferencesFromResource(xml.preferences, rootKey)

        /** Update summary and intent data of some preferences*/
        updatePreferenceSummary()

    }

    /**
     * Called when a preference in the tree rooted
     * at this PreferenceScreen has been clicked.
     * */

    override fun onPreferenceTreeClick(preference: Preference): Boolean {

        /** Preference keys */
        val appInfo = resources.getString(string.preference_key_app_version)
        val sourceCode = resources.getString(string.preference_key_source_code)
        val privacy = resources.getString(string.preference_key_privacy)
        val twitter = resources.getString(string.preference_key_tumur)

        /** OnClick handler */
        when(preference.key){
            sourceCode -> startCustomTab(Constants.SOURCE_CODE_URL)
            privacy -> startCustomTab(Constants.PRIVACY_URL)
            twitter -> startCustomTab(Constants.TWITTER_URL)
            appInfo -> findNavController().navigate(R.id.action_global_to_app_info_dialog)
        }

        return super.onPreferenceTreeClick(preference)
    }

    /** FUNCTIONS * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Set summary values
     * for app version
     * preferences key
     * */
    private fun updatePreferenceSummary(){

        /** Preference keys */
        val rate = resources.getString(string.preference_key_rate)
        val appVersion = resources.getString(string.preference_key_app_version)
        val theme = resources.getString(string.preference_key_theme_option)

        /** Preference screen */
        val pref = preferenceManager.preferenceScreen

        /** Custom summary and intent data */
        val version = requireContext().packageManager.getPackageInfo(requireContext().packageName, 0).versionName
        // App version
        pref.findPreference<Preference>(appVersion)?.summary = version
        // Rate this app
        pref.findPreference<Preference>(rate)?.intent?.data =
            "market://details?id=${requireContext().packageName}".toUri()
        // Theme
        val themePref = pref.findPreference<Preference>(theme)
        themePref?.let {
            it.onPreferenceChangeListener = OnPreferenceChangeListener { _, newValue ->
                val themeOption = newValue as String
                ThemeHelper.applyTheme(themeOption)
                true
            }
        }
    }

    /**
     * Starts custom tab
     * as an new intent
     * */
    private fun startCustomTab(url: String?){

        url?.let {
            requireContext().launchCustomTab(it)
        }
    }
}
