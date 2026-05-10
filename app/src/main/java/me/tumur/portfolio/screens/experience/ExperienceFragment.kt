package me.tumur.portfolio.screens.experience

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import me.tumur.portfolio.R
import me.tumur.portfolio.databinding.FragmentExperienceBinding
import me.tumur.portfolio.repository.database.model.experience.ExperienceModel
import me.tumur.portfolio.screens.MainViewModel
import me.tumur.portfolio.utils.adapters.listItemAdapters.experience.ExperienceAdapter
import me.tumur.portfolio.utils.adapters.listItemAdapters.experience.ExperienceClickListener
import me.tumur.portfolio.utils.constants.Constants
import me.tumur.portfolio.utils.extensions.collectFlow

/**
 * An fragment that inflates a portfolio layout.
 */
@AndroidEntryPoint
class ExperienceFragment : Fragment() {

    /** VARIABLES * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /** Create a new instance */
    companion object {
        fun newInstance() = ExperienceFragment()
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
    private val sharedViewModel: MainViewModel by activityViewModels()

    /**
     * Lazily create a ViewModel the first time the system calls an activity's onCreate() method.
     * Re-created fragments receive the same ViewModel instance created by the parent fragment.
     * */
    private val viewModel: ExperienceViewModel by viewModels()

    /**
     * Databinding
     */
    private lateinit var binding: FragmentExperienceBinding

    private lateinit var experienceMenu: Menu

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
        binding = FragmentExperienceBinding.inflate(inflater, container, false)

        /** Set fragment state in shared view model */
        sharedViewModel.setFragmentStateHolder(Constants.FRAGMENT_EXPERIENCE)

        /** Experience items */
        val experienceAdapter = ExperienceAdapter(ExperienceClickListener(viewModel::setSelectedItem))
        val layoutManagerExperience = LinearLayoutManager(context)
        layoutManagerExperience.orientation = LinearLayoutManager.VERTICAL
        val portfolioList = binding.experienceScreenList

        portfolioList.apply {
            this.layoutManager = layoutManagerExperience
            this.hasFixedSize()
            this.adapter = experienceAdapter
        }

        /** Set observers */
        setObservers(experienceAdapter)

        return binding.root
    }

    /** FUNCTIONS * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /** Set observers */
    private fun setObservers(experienceAdapter: ExperienceAdapter) {

        /**
         * Observer for portfolio adapters data
         * */
        viewLifecycleOwner.collectFlow(viewModel.data) { data ->
            experienceAdapter.submitData(viewLifecycleOwner.lifecycle, data)
        }

        /**
         * Click listener for experience item
         * */
        viewLifecycleOwner.collectFlow(viewModel.selectedItemFlow) {
            it?.let {
                val action =
                    ExperienceFragmentDirections.actionToExperienceDetailScreen(it.id, it.company)
                findNavController().navigate(action)
                viewModel.setSelectedItem(null)
            }
        }
    }
}
