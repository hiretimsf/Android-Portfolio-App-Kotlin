package hiretimsf.com.app.screens.profile.bottomsheet

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.net.toUri
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import hiretimsf.com.app.R
import hiretimsf.com.app.screens.profile.ProfileViewModel
import hiretimsf.com.app.utils.constants.Constants
import hiretimsf.com.app.utils.extensions.collectFlow
import hiretimsf.com.app.utils.extensions.launchCustomTab

@AndroidEntryPoint
class ProfileBottomSheetFragment: BottomSheetDialogFragment() {

    /** VARIABLES * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /** Create a new instance */
    companion object {
        fun newInstance() = ProfileBottomSheetFragment()
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

    /** Bottom sheet dialog fragment */
    private lateinit var dlg: BottomSheetDialog

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

    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: android.view.ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val content = ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val profile by viewModel.profileFlow.collectAsStateWithLifecycle()
                val socialItems by viewModel.socialFlow.collectAsStateWithLifecycle()

                ProfileBottomSheetContent(
                    profile = profile,
                    socialItems = socialItems,
                    onSocialClick = viewModel::socialItemOnClick,
                    onContactClick = { viewModel.emailItemOnClick(true) },
                )
            }
        }

        /** Observer for social item on click */
        viewLifecycleOwner.collectFlow(viewModel.socialItemOnClickFlow) { item ->
            item?.let {
                //Hide dialog and reset onclick value
                dlg.dismiss()
                viewModel.socialItemClicked()
                requireContext().launchCustomTab(item.url)
                this.findNavController().popBackStack()
            }
        }

        /** Observer for email item on click */
        viewLifecycleOwner.collectFlow(viewModel.emailItemOnClickFlow) { item ->
            if(item){
                //Set email intent
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Constants.MAILTO.toUri() // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, Constants.EMAIL)
                intent.putExtra(Intent.EXTRA_SUBJECT, Constants.SUBJECT)
                if (intent.resolveActivity(requireActivity().packageManager) != null) {
                    //Hide dialog and reset onclick value
                    dlg.dismiss()
                    viewModel.emailItemClicked()
                    // Start email intent
                    startActivity(intent)
                }
            }
        }

        return content

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dlg = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dlg.setOnShowListener {
            val d = it as BottomSheetDialog
            val sheet = d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            val behavior = sheet?.let { it1 -> BottomSheetBehavior.from(it1) }
            behavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        return dlg
    }
}
