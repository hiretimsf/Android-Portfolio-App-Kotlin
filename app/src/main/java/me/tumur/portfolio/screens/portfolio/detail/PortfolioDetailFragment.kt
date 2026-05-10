package me.tumur.portfolio.screens.portfolio.detail

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import me.tumur.portfolio.R
import me.tumur.portfolio.databinding.FragmentPortfolioDetailBinding
import me.tumur.portfolio.utils.adapters.listItemAdapters.portfolio.button.ButtonAdapter
import me.tumur.portfolio.utils.adapters.listItemAdapters.portfolio.button.ButtonClickListener
import me.tumur.portfolio.utils.adapters.listItemAdapters.portfolio.category.CategoryAdapter
import me.tumur.portfolio.utils.adapters.listItemAdapters.portfolio.screenshot.ScreenShotAdapter
import me.tumur.portfolio.utils.adapters.listItemAdapters.portfolio.screenshot.ScreenShotClickListener
import me.tumur.portfolio.utils.adapters.bindingAdapters.loadImage
import me.tumur.portfolio.utils.adapters.bindingAdapters.setDateFromTo
import me.tumur.portfolio.utils.extensions.collectFlow
import me.tumur.portfolio.utils.extensions.launchCustomTab


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
        binding = FragmentPortfolioDetailBinding.inflate(inflater, container, false)

        /** Set portfolio id for detail screen */
        args.id?.let {
            viewModel.setPortfolioId(it)
            id = it
        }

        setupOptionsMenu()

        /** Set observers and adapters */
        setAdapters()
        setObservers()

        return binding.root
    }

    /** FUNCTIONS * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    private fun setupOptionsMenu() {
        (requireActivity() as MenuHost).addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.portfolio_detail_menu, menu)
                    topMenu = menu
                    updateFavoriteMenu(menu)
                }

                override fun onPrepareMenu(menu: Menu) {
                    updateFavoriteMenu(menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.menu_save -> {
                            viewModel.portfolio?.let(viewModel::saveToFavorite)
                            updateFavoriteMenu(saved = true)
                            true
                        }
                        R.id.menu_saved -> {
                            viewModel.removeFromFavorite(id)
                            updateFavoriteMenu(saved = false)
                            true
                        }
                        R.id.menu_route -> {
                            viewModel.clickedScreenShot?.let { routeToPreview(it.ownerId, it.order) }
                            true
                        }
                        R.id.menu_share -> {
                            getShareIntent()
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

    private fun updateFavoriteMenu(menu: Menu? = topMenu, saved: Boolean = viewModel.favorite == 1) {
        menu?.findItem(R.id.menu_saved)?.isVisible = saved
        menu?.findItem(R.id.menu_save)?.isVisible = !saved
    }

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
        viewLifecycleOwner.collectFlow(viewModel.portfolioFlow) { portfolio ->
            portfolio ?: return@collectFlow
            binding.portfolioItemDetailImage.contentDescription = portfolio.imageDescription
            loadImage(binding.portfolioItemDetailImage, portfolio.coverImage)
            binding.portfolioItemDetailLogo.contentDescription = portfolio.logoDescription
            loadImage(binding.portfolioItemDetailLogo, portfolio.logo)
            binding.portfolioItemDetailImageOverlayForeground.setOnClickListener {
                viewModel.setVideoUrl(portfolio.videoUrl)
            }
            binding.portfolioItemDetailSubTitle.text = portfolio.subTitle
            binding.portfolioItemDate.setDateFromTo(portfolio.dateFrom, portfolio.dateTo)
            binding.portfolioItemText.text = portfolio.text
            binding.portfolioItemInfo.text = portfolio.info
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
                requireContext().launchCustomTab(it)
                viewModel.setButtonUrl(null)
            }
        }

        /** Set observer for screenshot click listener */
        viewLifecycleOwner.collectFlow(viewModel.clickedScreenShotFlow) {
            it?.let {
                routeToPreview(it.ownerId, it.order)
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
            updateFavoriteMenu(saved = it > 0)
        }
    }

    /**
     * Starts custom tab
     * as an new intent
     * */
    private fun startCustomTab(url: String?) {

        url?.let {
            requireContext().launchCustomTab(it)
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
