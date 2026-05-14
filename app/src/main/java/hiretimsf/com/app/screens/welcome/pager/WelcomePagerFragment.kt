package hiretimsf.com.app.screens.welcome.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import hiretimsf.com.app.R
import hiretimsf.com.app.databinding.PagerItemWelcomeScreenBinding
import hiretimsf.com.app.repository.database.model.welcome.WelcomeModel
import hiretimsf.com.app.screens.welcome.WelcomeViewModel
import hiretimsf.com.app.utils.adapters.bindingAdapters.setPagerIcon
import hiretimsf.com.app.utils.constants.Constants
import hiretimsf.com.app.utils.extensions.collectFlow

/**
 * An fragment that inflates a welcome layout.
 */
@AndroidEntryPoint
class WelcomePagerFragment : Fragment() {

    /** VARIABLES * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /** ViewModels */

    /**
     * Returns a property delegate to access ViewModel
     * by default scoped to this Fragment:
     * Default scope may be overridden with parameter ownerProducer:
     * This property can be accessed only after
     * this Fragment is attached i.e.,after Fragment.onAttach,
     * and access prior to that will result in IllegalArgumentException.*/

    private val viewModel: WelcomePagerViewModel by viewModels()

    private val sharedViewModel: WelcomeViewModel by viewModels({requireParentFragment()})

    /** View binding */
    private lateinit var binding: PagerItemWelcomeScreenBinding

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = PagerItemWelcomeScreenBinding.inflate(inflater, container, false)
        /** Set data for ViewPager adapter */
        arguments?.takeIf { it.containsKey(Constants.POSITION) }?.apply {
            viewModel.setPosition(this.getInt(Constants.POSITION))
            updateScreen()
        }

        /** Observer for welcome screen's data -----------------------------------------------------------------------*/
        viewLifecycleOwner.collectFlow(sharedViewModel.welcomeScreenFlow) {
            sharedViewModel.getWelcomeScreenData(viewModel.position)?.let { model -> viewModel.setData(model) }
            updateScreen()
        }
        viewLifecycleOwner.collectFlow(sharedViewModel.currentItemFlow) {
            updateScreen()
        }

        return binding.root
    }

    private fun updateScreen() {
        val data = viewModel.data ?: return
        binding.welcomeScreenItemTitle.text = data.title
        binding.welcomeScreenItemIcon.contentDescription = data.imageDescription
        setPagerIcon(binding.welcomeScreenItemIcon, data.order, sharedViewModel.currentItem, viewModel.position)
        binding.welcomeScreenItemSubTitle.text = data.subTitle
        binding.welcomeScreenItemText.text = data.text
    }
}
