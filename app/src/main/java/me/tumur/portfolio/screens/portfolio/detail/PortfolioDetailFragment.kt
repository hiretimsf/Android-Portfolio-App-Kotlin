package me.tumur.portfolio.screens.portfolio.detail

import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import me.tumur.portfolio.R
import me.tumur.portfolio.databinding.FragmentPortfolioDetailBinding
import me.tumur.portfolio.repository.database.model.button.ButtonModel
import me.tumur.portfolio.repository.database.model.category.CategoryModel
import me.tumur.portfolio.repository.database.model.screenshot.ScreenShotModel
import me.tumur.portfolio.utils.adapters.listItemAdapters.portfolio.button.ButtonAdapter
import me.tumur.portfolio.utils.adapters.listItemAdapters.portfolio.button.ButtonClickListener
import me.tumur.portfolio.utils.adapters.listItemAdapters.portfolio.category.CategoryAdapter
import me.tumur.portfolio.utils.adapters.listItemAdapters.portfolio.screenshot.ScreenShotAdapter
import me.tumur.portfolio.utils.adapters.listItemAdapters.portfolio.screenshot.ScreenShotClickListener
import me.tumur.portfolio.utils.extensions.collectFlow


/**
 * An fragment that inflates a portfolio detail layout.
 */
@AndroidEntryPoint
class PortfolioDetailFragment : Fragment() {

    /** VARIABLES * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /** Create a new instance */
    companion object {
        fun newInstance() = PortfolioDetailFragment()
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
    val viewModel: PortfolioDetailFragmentViewModel by viewModels()

    /**
     * Databinding
     */
    private lateinit var binding: FragmentPortfolioDetailBinding

    /** Adapter */
    private lateinit var buttonAdapter: ButtonAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var screenShotAdapter: ScreenShotAdapter

    /** Safe args */
    private val args: PortfolioDetailFragmentArgs by navArgs()

    /** Coroutines */
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main.immediate + job)

    /** Portfolio id */
    private lateinit var id: String

    /** Menu */
    private var topMenu: Menu? = null


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

        /** Lock fragment in portrait screen orientation */
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        /** Data binding */
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_portfolio_detail, container, false)

        /** Set portfolio id for detail screen */
        args.id?.let {
            viewModel.setPortfolioId(it)
            id = it
        }

        /** Set options menu */
        setHasOptionsMenu(true)

        /** Set observers and adapters */
        setAdapters()
        setObservers()

        binding.apply {
            this.lifecycleOwner = viewLifecycleOwner
            this.model = viewModel
            this.clickListener = VideoClickListener(viewModel::setVideoUrl)
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Inflate menu resource file.
        inflater.inflate(R.menu.portfolio_detail_menu, menu)
        topMenu = menu
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        uiScope.launch {
            if (viewModel.favorite == 1) {
                menu.findItem(R.id.menu_saved).isVisible = true
                menu.findItem(R.id.menu_save).isVisible = false
            } else {
                menu.findItem(R.id.menu_saved).isVisible = false
                menu.findItem(R.id.menu_save).isVisible = true
            }
        }

        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> {
                viewModel.portfolio?.let {
                    viewModel.saveToFavorite(it)
                }
                topMenu?.findItem(R.id.menu_saved)?.isVisible = true
                topMenu?.findItem(R.id.menu_save)?.isVisible = false
            }
            R.id.menu_saved -> {
                viewModel.removeFromFavorite(id)
                topMenu?.findItem(R.id.menu_saved)?.isVisible = false
                topMenu?.findItem(R.id.menu_save)?.isVisible = true
            }
            R.id.menu_route -> {
                viewModel.clickedScreenShot?.let { routeToPreview(it.ownerId, it.order) }
            }
            R.id.menu_share -> {
                getShareIntent()
            }
        }
        return true
    }

    override fun onPause() {
        job.cancel()
        super.onPause()
    }

    /** FUNCTIONS * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /** Adapters */
    private fun setAdapters() {
        /** Set Button Adapter */
        buttonAdapter = ButtonAdapter(ButtonClickListener(viewModel::setButtonUrl))
        val layoutManager = GridLayoutManager(activity, 2)
        val buttonList = binding.portfolioItemDetailButton

        buttonList.apply {
            this.layoutManager = layoutManager
            this.hasFixedSize()
            this.adapter = buttonAdapter
        }

        /** Set Category Adapter */
        categoryAdapter = CategoryAdapter()
        val layoutManagerCategory = GridLayoutManager(activity, 3)
        val categoryList = binding.portfolioItemDetailCategory

        categoryList.apply {
            this.layoutManager = layoutManagerCategory
            this.hasFixedSize()
            this.adapter = categoryAdapter
        }

        /** Set Screenshot Adapter */
        screenShotAdapter = ScreenShotAdapter(ScreenShotClickListener(viewModel::setClickedScreenShot))
        val layoutManagerScreenShot = LinearLayoutManager(context)
        layoutManagerScreenShot.orientation = LinearLayoutManager.HORIZONTAL
        val screenShotList = binding.portfolioItemDetailScreenshot

        screenShotList.apply {
            this.layoutManager = layoutManagerScreenShot
            this.hasFixedSize()
            this.adapter = screenShotAdapter
        }
    }

    /** Observers */
    private fun setObservers() {

        /** Set observer for button */
        viewLifecycleOwner.collectFlow(viewModel.portfolioFlow) {
            binding.invalidateAll()
        }

        viewLifecycleOwner.collectFlow(viewModel.button) { data ->
            buttonAdapter.submitData(viewLifecycleOwner.lifecycle, data)
        }

        /** Set observer for category */
        viewLifecycleOwner.collectFlow(viewModel.category) { data ->
            categoryAdapter.submitData(viewLifecycleOwner.lifecycle, data)
        }

        /** Set observer for screenshot */
        viewLifecycleOwner.collectFlow(viewModel.screenshot) { data ->
            screenShotAdapter.submitData(viewLifecycleOwner.lifecycle, data)
        }

        /** Set observer for button click listener */
        viewLifecycleOwner.collectFlow(viewModel.buttonUrlFlow) {
            it?.let {
                // Set chrome custom tab
                val builder = CustomTabsIntent.Builder()
                builder.setToolbarColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                builder.setShowTitle(true)
                val intent = builder.build()
                intent.launchUrl(requireContext(), Uri.parse(it))
                viewModel.setButtonUrl(null)
            }
        }

        /** Set observer for screenshot click listener */
        viewLifecycleOwner.collectFlow(viewModel.clickedScreenShotFlow) {
            it?.let {
                topMenu?.let { menu ->
                    val menuAction = menu.findItem(R.id.menu_route)
                    menuAction?.let { item ->
                        onOptionsItemSelected(item)
                    }
                }
            }
        }

        /** Set observer for open video click listener */
        viewLifecycleOwner.collectFlow(viewModel.videoUrlFlow) {
            it?.let {
                startCustomTab(it)
                viewModel.setVideoUrl(null)
            }
        }

        /** Set observer for favorite */
        viewLifecycleOwner.collectFlow(viewModel.favoriteFlow) {
            topMenu?.let { menu ->
                val menuSaved = menu.findItem(R.id.menu_saved)
                val menuSave = menu.findItem(R.id.menu_save)
                val condition1 = it > 0 && menuSave != null && menuSave.isVisible
                val condition2 = it == 0 && menuSaved != null && menuSaved.isVisible
                if (condition1) {
                    menuSave.isVisible = false
                    menuSaved.isVisible = true
                } else if (condition2) {
                    menuSaved.isVisible = false
                    menuSave.isVisible = true
                }
            }
        }
    }

    /**
     * Starts custom tab
     * as an new intent
     * */
    private fun startCustomTab(url: String?) {

        url?.let {
            /** Chrome custom tab  */
            val builder = CustomTabsIntent.Builder().apply {
                this.setToolbarColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                this.setShowTitle(true)
            }
            builder.build().launchUrl(requireContext(), (Uri.parse(url)))
        }
    }

    /** Get a share intent */
    private fun getShareIntent(): Intent {
        /** Share intent */
        val intent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_SUBJECT, viewModel.portfolio?.title)
            putExtra(Intent.EXTRA_TITLE, viewModel.portfolio?.subTitle)
            putExtra(Intent.EXTRA_TEXT, viewModel.portfolio?.linkToShare)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(intent, resources.getText(R.string.app_share_message)))
        return intent
    }

    /** Route to preview */

    private fun routeToPreview(ownerId: String, order: Int) {
        // Navigate
        val action = PortfolioDetailFragmentDirections.actionToPortfolioDetailScreenPreview(ownerId, order)
        findNavController().navigate(action)
        // Reset clicked screen shot
        viewModel.setClickedScreenShot(null)
    }
}
