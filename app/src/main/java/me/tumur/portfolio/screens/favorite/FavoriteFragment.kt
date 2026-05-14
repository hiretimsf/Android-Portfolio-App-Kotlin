package me.tumur.portfolio.screens.favorite

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
import me.tumur.portfolio.databinding.FragmentFavoriteBinding
import me.tumur.portfolio.screens.MainViewModel
import me.tumur.portfolio.utils.adapters.listItemAdapters.favorite.FavoriteAdapter
import me.tumur.portfolio.utils.adapters.listItemAdapters.favorite.FavoriteClickListener
import me.tumur.portfolio.utils.adapters.bindingAdapters.setScreenFavoriteEmpty
import me.tumur.portfolio.utils.adapters.bindingAdapters.setScreenFavoriteNotEmpty
import me.tumur.portfolio.utils.constants.Constants
import me.tumur.portfolio.utils.extensions.collectFlow
import me.tumur.portfolio.utils.state.Empty
import me.tumur.portfolio.utils.state.NotEmpty

/**
 * An fragment that inflates a portfolio layout.
 */
@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    /** VARIABLES * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /** Create a new instance */
    companion object {
        fun newInstance() = FavoriteFragment()
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
    private val viewModel: FavoriteViewModel by viewModels()

    /**
     * View binding
     */
    private lateinit var binding: FragmentFavoriteBinding

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
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        /** Set fragment state in shared view model */
        sharedViewModel.setFragmentStateHolder(Constants.FRAGMENT_FAVORITE)

        /** Favorite items */
        val favoriteAdapter = FavoriteAdapter(FavoriteClickListener(viewModel::setSelectedItem))
        val layoutManagerFavorite = LinearLayoutManager(context)
        layoutManagerFavorite.orientation = LinearLayoutManager.VERTICAL
        val favoriteList = binding.favoriteScreenList

        favoriteList.apply {
            this.layoutManager = layoutManagerFavorite
            this.hasFixedSize()
            this.adapter = favoriteAdapter
        }

        setupOptionsMenu()

        /** Set observers */
        setObservers(favoriteAdapter)

        return binding.root
    }

    /** FUNCTIONS * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    private fun setupOptionsMenu() {
        (requireActivity() as MenuHost).addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.favorite_list_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.menu_delete_all -> {
                            handleDeleteAllOrOpenSelected()
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

    private fun handleDeleteAllOrOpenSelected() {
        val favoriteItem = viewModel.selectedItem
        if (favoriteItem != null) {
            val action = FavoriteFragmentDirections.actionToPortfolioDetailScreen(favoriteItem.id, favoriteItem.title)
            findNavController().navigate(action)
            viewModel.setSelectedItem(null, false)
        } else {
            viewModel.deleteAllItems()
        }
    }

    private fun setObservers(favoriteAdapter: FavoriteAdapter) {
        /**
         * Observer for favorite list items data
         * */
        viewLifecycleOwner.collectFlow(viewModel.data) { data ->
            favoriteAdapter.submitData(viewLifecycleOwner.lifecycle, data)
        }

        /**
         * Observer for click listener
         * */
        viewLifecycleOwner.collectFlow(viewModel.selectedItemFlow) {
            it?.let {
                handleDeleteAllOrOpenSelected()
            }
        }

        /**
         * Observer for table
         * */
        viewLifecycleOwner.collectFlow(viewModel.table) {
            if (it > 0) viewModel.setState(NotEmpty) else viewModel.setState(Empty)
        }

        viewLifecycleOwner.collectFlow(viewModel.stateFlow) { state ->
            setScreenFavoriteEmpty(binding.favoriteScreenEmpty, state)
            setScreenFavoriteNotEmpty(binding.favoriteScreenList, state)
        }

        /**
         * Observer for delete item id
         * */
        viewLifecycleOwner.collectFlow(viewModel.deleteItemIdFlow) {
            it?.let {
                viewModel.deleteSingleItem(it)
                viewModel.setDeleteItemId(null)
            }
        }
    }
}
