package me.tumur.portfolio.screens.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import me.tumur.portfolio.R
import me.tumur.portfolio.databinding.FragmentWelcomeBinding
import me.tumur.portfolio.screens.welcome.pager.WelcomePagerAdapter
import me.tumur.portfolio.utils.adapters.bindingAdapters.setPagerDots
import me.tumur.portfolio.utils.adapters.bindingAdapters.setViewPagerCurrentItem
import me.tumur.portfolio.utils.adapters.bindingAdapters.setViewPagerPageChangeListener
import me.tumur.portfolio.utils.extensions.collectFlow

/**
 * An fragment that inflates a welcome layout.
 */
@AndroidEntryPoint
class WelcomeFragment : Fragment() {

    /** VARIABLES * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /** Create a new instance */
    companion object {
        fun newInstance() = WelcomeFragment()
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

    /**
     * Lazily create a ViewModel the first time the system calls an activity's onCreate() method.
     * Re-created fragments receive the same ViewModel instance created by the parent fragment.
     * */
    private val viewModel by lazy { ViewModelProvider(this).get(WelcomeViewModel::class.java) }

    /** Databinding */
    private lateinit var binding: FragmentWelcomeBinding

    /** View pager' adapter */
    private lateinit var welcomePagerAdapter: WelcomePagerAdapter


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
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        binding.root.findViewById<View>(R.id.welcome_screen_btn).setOnClickListener {
            viewModel.setOnClicked(true)
        }

        /** Set observer */
        setObservers()

        /** Set view pager */
        setWelcomeScreenViewPager()

        return binding.root
    }

    /** FUNCTIONS * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    private fun setObservers() {
        /** Observer for skip and next button onClick ---------------------------------------------------------------*/
        viewLifecycleOwner.collectFlow(viewModel.buttonTextFlow) {
            binding.root.findViewById<com.google.android.material.button.MaterialButton>(R.id.welcome_screen_btn).text = it
        }

        viewLifecycleOwner.collectFlow(viewModel.scrollToItemFlow) {
            setViewPagerCurrentItem(
                binding.root.findViewById(R.id.welcome_screen_view_pager),
                it,
                true,
            )
        }

        viewLifecycleOwner.collectFlow(viewModel.onClickedFlow) { state ->
            if(state){

                val currentPage = viewModel.currentItem
                val lastPage = welcomePagerAdapter.itemCount - 1

                when( currentPage == lastPage){
                    true -> {
                        this.findNavController().navigate(WelcomeFragmentDirections.actionToProfileScreen())
                        viewModel.setFirstRunAs(false)
                    }
                    else -> {
                        viewModel.setScrollToItem(currentPage + 1)
                    }
                }
                viewModel.setOnClicked(false)
            }
        }

        /** Observer for skip and next button text ---------------------------------------------------------------*/
        viewLifecycleOwner.collectFlow(viewModel.currentItemFlow) { current ->
            setPagerDots(binding.root.findViewById(R.id.welcome_screen_page_indicator), welcomePagerAdapter.itemCount, current)
            val textFinish = this.getString(R.string.button_finish)
            val textNext = this.getString(R.string.button_next)
            if (current == welcomePagerAdapter.itemCount - 1) {
                viewModel.setButtonText(textFinish)
            } else if (viewModel.buttonText != textNext) {
                viewModel.setButtonText(textNext)
            }
        }
    }

    /**
     * Setup welcome screen's view pager with adapter
     */
    private fun setWelcomeScreenViewPager() {

        /** Set view pager' adapter */
        welcomePagerAdapter = WelcomePagerAdapter(this)
        val viewPager = binding.root.findViewById<ViewPager2>(R.id.welcome_screen_view_pager)
        viewPager.adapter = welcomePagerAdapter
        setViewPagerPageChangeListener(viewPager, viewModel)
        setPagerDots(binding.root.findViewById(R.id.welcome_screen_page_indicator), welcomePagerAdapter.itemCount, viewModel.currentItem)

        /**
         * Setup view pager's back button
         * Handling back button from Fragment for ViewPager2 scrolling. This callback
         * will only be called when MyFragment is at least Started.
         * */
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            val currentItem = viewModel.currentItem
            if(currentItem > 0) {
                this.isEnabled = true
                viewModel.setScrollToItem(currentItem - 1)
            } else this.isEnabled = false
        }

    }
}
