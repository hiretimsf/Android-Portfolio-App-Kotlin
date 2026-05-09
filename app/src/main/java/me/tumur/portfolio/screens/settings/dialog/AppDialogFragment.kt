package me.tumur.portfolio.screens.settings.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import me.tumur.portfolio.R
import me.tumur.portfolio.databinding.FragmentDialogAppInfoBinding
import me.tumur.portfolio.repository.database.model.settings.AppModel
import me.tumur.portfolio.utils.adapters.listItemAdapters.app.AppAdapter
import me.tumur.portfolio.utils.extensions.collectFlow

@AndroidEntryPoint
class AppDialogFragment : DialogFragment() {

    /** VARIABLES * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /** Create a new instance */
    companion object {
        fun newInstance() = AppDialogFragment()
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
    private val viewModel: AppDialogViewModel by viewModels()

    /** Databinding */
    private lateinit var binding: FragmentDialogAppInfoBinding

    /** App info dialog fragment */
    private lateinit var dialog: DialogFragment

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dialog_app_info, container, false)

        /** Social items */
        val appInfoAdapter = AppAdapter()
        val layoutManagerAppInfo = LinearLayoutManager(context)
        layoutManagerAppInfo.orientation = LinearLayoutManager.VERTICAL
        val appInfoList = binding.dialogBodyList

        appInfoList.apply {
            this.layoutManager = layoutManagerAppInfo
            this.hasFixedSize()
            this.adapter = appInfoAdapter
        }

        binding.apply {
            // Set the lifecycleOwner so DataBinding can observe Flow
            this.lifecycleOwner = viewLifecycleOwner
            // Set the viewmodel so layout can display data
            this.model = viewModel
        }

        /** Observer for social adapter */
        viewLifecycleOwner.collectFlow(viewModel.appInfo) { data ->
            appInfoAdapter.submitList(data)
        }

        /** Observer for close button */
        viewLifecycleOwner.collectFlow(viewModel.closeButtonOnClick) { status ->
            if(status)
            {
                this.dismiss()
            }
        }

        return binding.root
    }
}
