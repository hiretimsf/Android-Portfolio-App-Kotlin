package me.tumur.portfolio.screens.portfolio

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import me.tumur.portfolio.R
import me.tumur.portfolio.databinding.FragmentPortfolioBinding
import me.tumur.portfolio.screens.MainViewModel
import me.tumur.portfolio.utils.adapters.listItemAdapters.portfolio.PortfolioAdapter
import me.tumur.portfolio.utils.adapters.listItemAdapters.portfolio.PortfolioClickListener
import me.tumur.portfolio.utils.constants.Constants
import me.tumur.portfolio.utils.extensions.collectFlow
import me.tumur.portfolio.utils.state.ToastEmpty
import me.tumur.portfolio.utils.state.ToastShow


/**
 * An fragment that inflates a portfolio layout.
 */
@AndroidEntryPoint
class PortfolioFragment : Fragment() {

    /** VARIABLES * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /** Create a new instance */
    companion object {
        fun newInstance() = PortfolioFragment()
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
    private val viewModel: PortfolioViewModel by viewModels()

    /**
     * View binding
     */
    private lateinit var binding: FragmentPortfolioBinding

    /**
     * Pull to refresh layout
     */
    private val pullToRefresh by lazy { binding.portfolioScreenRefresh }

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
        binding = FragmentPortfolioBinding.inflate(inflater, container, false)

        /** Set fragment state in shared view model */
        sharedViewModel.setFragmentStateHolder(Constants.FRAGMENT_PORTFOLIO)

        /** Portfolio items */
        val portfolioAdapter = PortfolioAdapter(PortfolioClickListener { viewModel.setSelectedItem(it) })
        val layoutManagerPortfolio = LinearLayoutManager(context)
        layoutManagerPortfolio.orientation = LinearLayoutManager.VERTICAL
        val portfolioList = binding.portfolioScreenList

        portfolioList.apply {
            this.layoutManager = layoutManagerPortfolio
            this.hasFixedSize()
            this.adapter = portfolioAdapter
        }

        setupOptionsMenu()

        /** Set listeners */
        setPullToRefreshListener()

        /** Set observers */
        setObservers(portfolioAdapter)

        return binding.root
    }

    /** FUNCTIONS * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    private fun setupOptionsMenu() {
        (requireActivity() as MenuHost).addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.portfolio_list_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.menu_refresh -> {
                            viewModel.setRefreshStatus(true)
                            viewModel.fetch()
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

    /** Set observers */
    private fun setObservers(portfolioAdapter: PortfolioAdapter) {
        /**
         * Observer for portfolio adapters data
         * */
        viewLifecycleOwner.collectFlow(viewModel.data) { data ->
            portfolioAdapter.submitData(viewLifecycleOwner.lifecycle, data)
        }

        /**
         * Click listener for portfolio item
         * */
        viewLifecycleOwner.collectFlow(viewModel.selectedItemFlow) {
            it?.let {
                val action =
                    PortfolioFragmentDirections.actionToPortfolioDetailScreen(it.id, it.title)
                findNavController().navigate(action)
                viewModel.setSelectedItem(null)
            }
        }

        /**
         * Set observer for refresh status
         * */
        viewLifecycleOwner.collectFlow(viewModel.isRefreshingFlow) { status ->
            if (!pullToRefresh.isRefreshing && status) {
                pullToRefresh.isRefreshing = status
                viewModel.fetch()
            } else if (pullToRefresh.isRefreshing && !status)
                pullToRefresh.isRefreshing = status
        }

        /**
         * Observer for show toast message from activity
         * */
        viewLifecycleOwner.collectFlow(viewModel.showToastFlow) { state ->
            when (state) {
                ToastShow -> {
                    sharedViewModel.setShowToast(state)
                    viewModel.setShowToast(ToastEmpty)
                }
                else -> Unit
            }
        }
    }


    /**
     * Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
     * performs a swipe-to-refresh gesture.
     * */
    private fun setPullToRefreshListener() {
        pullToRefresh.setOnRefreshListener {
            viewModel.fetch()
        }
    }

}
