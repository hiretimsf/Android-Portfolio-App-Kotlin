package hiretimsf.com.app.screens.profile

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import coil.imageLoader
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import hiretimsf.com.app.R
import hiretimsf.com.app.databinding.FragmentProfileBinding
import hiretimsf.com.app.screens.MainViewModel
import hiretimsf.com.app.utils.constants.Constants
import hiretimsf.com.app.utils.extensions.collectFlow
import hiretimsf.com.app.utils.extensions.launchCustomTab

/**
 * An fragment that inflates a profile layout.
 */
@AndroidEntryPoint
class ProfileFragment : Fragment() {

    /** VARIABLES * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /** Create a new instance */
    companion object {
        fun newInstance() = ProfileFragment()
    }

    /** ViewModel */

    /**
     * Returns a property delegate to access ViewModel
     * by default scoped to this Fragment:
     * Default scope may be overridden with parameter ownerProducer:
     * This property can be accessed only after
     * this Fragment is attached i.e.,after Fragment.onAttach,
     * and access prior to that will result in IllegalArgumentException.
     * */
    private val viewModel: ProfileViewModel by hiltNavGraphViewModels(R.id.profile_screen)
    private val sharedViewModel: MainViewModel by activityViewModels()

    /** View binding */
    private lateinit var binding: FragmentProfileBinding

    /** INITIALIZATION * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Return the View for the fragment's UI.
     */

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        setupAboutContent()
        setupContactButton()

        setupOptionsMenu()
        setupCollapsedProfileHeader()

        /** Set fragment state in shared view model */
        sharedViewModel.setFragmentStateHolder(Constants.FRAGMENT_PROFILE)

        /** Set observers */
        setObservers()

        return binding.root
    }

    private fun setupAboutContent() {
        binding.root.findViewById<ComposeView>(R.id.profile_screen_about_content).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                // Profile body text starts in LocalPortfolioStrings.about and flows through this state.
                val aboutState by viewModel.aboutScreenFlow.collectAsStateWithLifecycle()
                ProfileAboutContent(
                    sections = aboutState.sections,
                    introductionImages = aboutState.introductionImages,
                )
            }
        }
    }

    private fun setupContactButton() {
        val isContactButtonVisible = mutableStateOf(true)
        binding.root.findViewById<androidx.core.widget.NestedScrollView>(R.id.screen_profile_about_content)
            .setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                when {
                    scrollY > oldScrollY -> isContactButtonVisible.value = false
                    scrollY < oldScrollY -> isContactButtonVisible.value = true
                }
            }

        binding.screenProfileFabButton.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ProfileContactButton(
                    visible = isContactButtonVisible.value,
                    onClick = { viewModel.setShowProfileBottomsheet(true) },
                )
            }
        }
    }

    private fun setupOptionsMenu() {
        (requireActivity() as MenuHost).addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.profile_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.menu_share -> {
                            getShareIntent()
                            true
                        }
                        R.id.menu_privacy -> {
                            startCustomTab(Constants.PRIVACY_URL)
                            true
                        }
                        else -> false
                    }
                }
            },
            viewLifecycleOwner,
            Lifecycle.State.RESUMED,
        )
    }

    private fun setupCollapsedProfileHeader() {
        val collapsedRow = binding.root.findViewById<View>(R.id.profile_screen_header_collapsed_row)
        binding.root.findViewById<com.google.android.material.appbar.AppBarLayout>(R.id.app_bar)
            .addOnOffsetChangedListener { appBarLayout, verticalOffset ->
                val progress = -verticalOffset / appBarLayout.totalScrollRange.toFloat()
                collapsedRow.alpha = ((progress - 0.75f) / 0.25f).coerceIn(0f, 1f)
            }
    }

    private fun setObservers(){

        /** Observer for show profile bottom sheet dialog ------------------------------------------------------------*/
        viewLifecycleOwner.collectFlow(viewModel.profileFlow) { profile ->
            val profileName = getString(R.string.name)
            val profileTitle = getString(R.string.title)

            binding.root.findViewById<android.widget.TextView>(R.id.profile_screen_header_greeting).text = profile?.greeting
            binding.root.findViewById<android.widget.TextView>(R.id.profile_screen_header_name).text = profileName
            binding.root.findViewById<android.widget.TextView>(R.id.profile_screen_header_title).text = profileTitle
            binding.root.findViewById<android.widget.TextView>(R.id.profile_screen_header_collapsed_name).text = profileName
            binding.root.findViewById<android.widget.ImageView>(R.id.profile_screen_header_avatar).apply {
                contentDescription = profile?.imageDescription
                load(profile?.image, imageLoader = context.imageLoader) {
                    placeholder(R.drawable.profile)
                    error(R.drawable.profile)
                }
            }
            binding.root.findViewById<android.widget.ImageView>(R.id.profile_screen_header_collapsed_avatar).apply {
                contentDescription = profile?.imageDescription
                load(profile?.image, imageLoader = context.imageLoader) {
                    placeholder(R.drawable.profile)
                    error(R.drawable.profile)
                }
            }
        }

        viewLifecycleOwner.collectFlow(viewModel.showProfileBottomSheetFlow) { data ->
            if(data) {
                this.findNavController().navigate(ProfileFragmentDirections.actionToSocial())
                viewModel.resetShowProfileBottomsheet()
            }
        }

    }

    /** Get a share intent */
    private fun getShareIntent(): Intent{
        /** Share intent */
        val intent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_SUBJECT, context?.resources?.getString(R.string.app_text))
            putExtra(Intent.EXTRA_TITLE, context?.resources?.getString(R.string.app_text))
            putExtra(Intent.EXTRA_TEXT, context?.resources?.getString(R.string.app_url))
            type = "text/plain"
        }
        startActivity(Intent.createChooser(intent, resources.getText(R.string.app_share_message)))
        return intent
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
