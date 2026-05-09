package me.tumur.portfolio.screens.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import me.tumur.portfolio.R
import me.tumur.portfolio.databinding.FragmentProfileBinding
import me.tumur.portfolio.repository.database.model.profile.AboutModel
import me.tumur.portfolio.screens.MainViewModel
import me.tumur.portfolio.utils.adapters.listItemAdapters.about.AboutAdapter
import me.tumur.portfolio.utils.constants.Constants
import me.tumur.portfolio.utils.extensions.collectFlow

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

    /** Databinding */
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

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        /** Lock fragment in portrait screen orientation */
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        /** Data binding */
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        /** About items */
        val aboutAdapter = AboutAdapter()
        val layoutManagerAbout = LinearLayoutManager(context)
        layoutManagerAbout.orientation = LinearLayoutManager.VERTICAL
        val aboutList = binding.root.findViewById<RecyclerView>(R.id.profile_screen_about_content)

        aboutList.apply {
            this.layoutManager = layoutManagerAbout
            this.hasFixedSize()
            this.adapter = aboutAdapter
        }

        binding.apply {
            // Set the lifecycleOwner so DataBinding can observe Flow
            this.lifecycleOwner = viewLifecycleOwner
            // Set the viewmodel so layout can display data
            this.model = viewModel
            this.shared = sharedViewModel
        }

        /** Set options menu */
        setHasOptionsMenu(true)

        /** Set fragment state in shared view model */
        sharedViewModel.setFragmentStateHolder(Constants.FRAGMENT_PROFILE)

        /** Set observers */
        setObservers(aboutAdapter)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Inflate menu resource file.
        inflater.inflate(R.menu.profile_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_share -> {
                getShareIntent()

            }
            R.id.menu_privacy -> startCustomTab(Constants.PRIVACY_URL)
        }
        return true
    }

    /** FUNCTIONS * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    private fun setObservers(aboutAdapter: AboutAdapter){

        /** Observer for show profile bottom sheet dialog ------------------------------------------------------------*/
        viewLifecycleOwner.collectFlow(viewModel.profileFlow) {
            binding.invalidateAll()
        }

        viewLifecycleOwner.collectFlow(viewModel.showProfileBottomSheetFlow) { data ->
            if(data) {
                this.findNavController().navigate(ProfileFragmentDirections.actionToSocial())
                viewModel.resetShowProfileBottomsheet()
            }
        }

        /** Observer for about adapter -------------------------------------------------------------------------------*/
        viewLifecycleOwner.collectFlow(viewModel.aboutFlow) { data ->
            aboutAdapter.addHeaderAndSubmitList(data)
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
            /** Chrome custom tab  */
            val builder = CustomTabsIntent.Builder().apply {
                this.setToolbarColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                this.setShowTitle(true)
            }
            builder.build().launchUrl(requireContext(), (Uri.parse(url)))
        }
    }
}
