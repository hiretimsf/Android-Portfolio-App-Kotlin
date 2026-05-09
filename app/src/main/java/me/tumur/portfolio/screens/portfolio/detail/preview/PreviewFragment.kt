package me.tumur.portfolio.screens.portfolio.detail.preview

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import me.tumur.portfolio.R
import me.tumur.portfolio.databinding.FragmentPreviewBinding
import me.tumur.portfolio.screens.portfolio.detail.preview.pager.PreviewPagerAdapter
import me.tumur.portfolio.utils.extensions.collectFlow

@AndroidEntryPoint
class PreviewFragment : Fragment() {

    /** VARIABLES * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /** Create a new instance */
    companion object {
        fun newInstance() = PreviewFragment()
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
    private val viewModel by lazy { ViewModelProvider(this).get(PreviewFragmentViewModel::class.java) }

    /** Databinding */
    private lateinit var binding: FragmentPreviewBinding

    /** Safe args */
    private val args: PreviewFragmentArgs by navArgs()

    /** View pager' adapter */
    private lateinit var previewPagerAdapter: PreviewPagerAdapter

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_preview, container, false)

        /** Set portfolio owner id and order for preview screen */
        args.id?.let {
            viewModel.setId(it)
        }

        args.order.let {
            viewModel.setCurrentItem(it)
            viewModel.setScrollToItem(it - 1)
        }

        binding.apply {
            this.lifecycleOwner = viewLifecycleOwner
            this.model = viewModel
        }

        setObservers()

        /** Set view pager */
        setPreviewScreenViewPager()

        return binding.root

    }

    /** FUNCTIONS * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Observe flow-backed properties used by data binding.
     */
    private fun setObservers() {
        viewLifecycleOwner.collectFlow(viewModel.scrollToItemFlow) {
            binding.invalidateAll()
        }

        viewLifecycleOwner.collectFlow(viewModel.currentItemFlow) {
            binding.invalidateAll()
        }
    }

    /**
     * Setup preview screen's view pager with adapter
     */
    private fun setPreviewScreenViewPager() {

        /** View pager */
        val viewPager = binding.previewViewPager
        /** View pager's indicator */
        val viewPagerIndicator = binding.previewPageIndicator

        /** Set view pager' adapter */
        previewPagerAdapter = PreviewPagerAdapter(childFragmentManager)
        viewPager.adapter = previewPagerAdapter
        /** Set view pager' indicator */
        viewPagerIndicator.setViewPager(viewPager)

        /**
         * Setup view pager's back button
         * Handling back button from Fragment for ViewPager scrolling. This callback
         * will only be called when MyFragment is at least Started.
         * */
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            val currentItem = viewModel.currentItem
            if (currentItem > 0) {
                this.isEnabled = true
                viewModel.setScrollToItem(currentItem - 1)
                if (currentItem - 1 == 0) this.isEnabled = false
            }
        }
    }
}
