package me.tumur.portfolio.screens.favorite

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import me.tumur.portfolio.R
import me.tumur.portfolio.databinding.FragmentFavoriteBinding
import me.tumur.portfolio.repository.database.model.favorite.FavoriteModel
import me.tumur.portfolio.screens.MainViewModel
import me.tumur.portfolio.utils.adapters.listItemAdapters.favorite.FavoriteAdapter
import me.tumur.portfolio.utils.adapters.listItemAdapters.favorite.FavoriteClickListener
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
     * Databinding
     */
    private lateinit var binding: FragmentFavoriteBinding

    /** Action bar menu */
    private var favoriteMenu: Menu? = null

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite, container, false)

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

        binding.apply {
            this.lifecycleOwner = viewLifecycleOwner
            this.model = viewModel
        }

        /** Options menu */
        setHasOptionsMenu(true)

        /** Set observers */
        setObservers(favoriteAdapter)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        favoriteMenu = menu
        inflater.inflate(R.menu.favorite_list_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete_all -> {
                val favoriteItem = viewModel.selectedItem
                if (favoriteItem != null) {
                    val action =
                        FavoriteFragmentDirections.actionToPortfolioDetailScreen(favoriteItem.id, favoriteItem.title)
                    findNavController().navigate(action)
                    viewModel.setSelectedItem(null, false)
                } else {
                    viewModel.deleteAllItems()
                }
            }
        }
        return true
    }

    /** FUNCTIONS * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

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
                favoriteMenu?.let { menu ->
                    val menuAction = menu.findItem(R.id.menu_delete_all)
                    menuAction?.let { action ->
                        onOptionsItemSelected(action)
                    }

                }

            }
        }

        /**
         * Observer for table
         * */
        viewLifecycleOwner.collectFlow(viewModel.table) {
            if (it > 0) viewModel.setState(NotEmpty) else viewModel.setState(Empty)
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
