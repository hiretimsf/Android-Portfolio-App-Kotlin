package me.tumur.portfolio.screens.profile.bottomsheet

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import me.tumur.portfolio.R
import me.tumur.portfolio.databinding.FragmentProfileBottomSheetBinding
import me.tumur.portfolio.repository.database.model.profile.SocialModel
import me.tumur.portfolio.screens.profile.ProfileViewModel
import me.tumur.portfolio.utils.adapters.listItemAdapters.social.SocialAdapter
import me.tumur.portfolio.utils.adapters.listItemAdapters.social.SocialClickListener
import me.tumur.portfolio.utils.constants.Constants
import me.tumur.portfolio.utils.extensions.collectFlow

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

    /** Databinding */
    private lateinit var binding: FragmentProfileBottomSheetBinding

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        /** Data binding */
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_bottom_sheet, container, false)

        /** Social items */
        val socialAdapter = SocialAdapter(SocialClickListener { viewModel.socialItemOnClick(it) })
        val layoutManagerSocial = LinearLayoutManager(context)
        layoutManagerSocial.orientation = LinearLayoutManager.VERTICAL
        val socialList = binding.root.findViewById<RecyclerView>(R.id.profile_bs_social_list)

        socialList.apply {
            this.layoutManager = layoutManagerSocial
            this.hasFixedSize()
            this.adapter = socialAdapter
        }

        binding.apply {
            // Set the lifecycleOwner so DataBinding can observe Flow
            this.lifecycleOwner = viewLifecycleOwner
            // Set the viewmodel so layout can display data
            this.model = viewModel
        }

        /** Observer for social adapter */
        viewLifecycleOwner.collectFlow(viewModel.profileFlow) {
            binding.invalidateAll()
        }

        val socialAdapterObserver: suspend (List<SocialModel>) -> Unit = { data ->
            socialAdapter.submitList(data)
        }
        viewLifecycleOwner.collectFlow(viewModel.socialFlow, collector = socialAdapterObserver)

        /** Observer for social item on click */
        viewLifecycleOwner.collectFlow(viewModel.socialItemOnClickFlow) { item ->
            item?.let {
                //Hide dialog and reset onclick value
                dlg.dismiss()
                viewModel.socialItemClicked()
                // Set chrome custom tab
                val builder = CustomTabsIntent.Builder()
                builder.setToolbarColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                builder.setShowTitle(true)
                val intent = builder.build()
                intent.launchUrl(requireContext(), Uri.parse(item.url))
                this.findNavController().popBackStack()
            }
        }

        /** Observer for email item on click */
        viewLifecycleOwner.collectFlow(viewModel.emailItemOnClickFlow) { item ->
            if(item){
                //Set email intent
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse(Constants.MAILTO) // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, Constants.EMAIL)
                intent.putExtra(Intent.EXTRA_SUBJECT, Constants.SUBJECT)
                if (intent.resolveActivity(activity!!.packageManager) != null) {
                    //Hide dialog and reset onclick value
                    dlg.dismiss()
                    viewModel.emailItemClicked()
                    // Start email intent
                    startActivity(intent)
                }
            }
        }

        return binding.root

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
