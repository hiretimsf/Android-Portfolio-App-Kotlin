package me.tumur.portfolio.screens.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.tumur.portfolio.repository.database.dao.favorite.FavoriteDao
import me.tumur.portfolio.repository.database.model.favorite.FavoriteModel
import me.tumur.portfolio.utils.state.FavoriteState
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteDao: FavoriteDao
) : ViewModel() {

    /** VARIABLES * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /** State */
    private val _state = MutableStateFlow<FavoriteState?>(null)
    val stateFlow: StateFlow<FavoriteState?> = _state.asStateFlow()
    val state: FavoriteState? get() = _state.value

    /** Selected item id */
    private val _selectedItem = MutableStateFlow<FavoriteModel?>(null)
    val selectedItemFlow: StateFlow<FavoriteModel?> = _selectedItem.asStateFlow()
    val selectedItem: FavoriteModel? get() = _selectedItem.value

    /** Delete item id */
    private val _deleteItemId = MutableStateFlow<String?>(null)
    val deleteItemIdFlow: StateFlow<String?> = _deleteItemId.asStateFlow()

    /** Favorite pager data */
    private val config = PagingConfig(pageSize = 10, enablePlaceholders = true, initialLoadSize = 5)

    val data: Flow<PagingData<FavoriteModel>> = Pager(config) {
        favoriteDao.getListItems()
    }.flow.cachedIn(viewModelScope)

    /** Check table */
    val table: StateFlow<Int> = favoriteDao.check()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    /** FUNCTIONS * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Set selected item
     * */
    fun setSelectedItem(item: FavoriteModel?, delete: Boolean) {
        item?.let {
            if (delete) _deleteItemId.value = it.id else _selectedItem.value = it
        }
    }

    /**
     * Set delete item id
     * */
    fun setDeleteItemId(id: String?) {
        _deleteItemId.value = id
    }

    /**
     * Set state
     * */
    fun setState(state: FavoriteState) {
        _state.value = state
    }

    /**
     * Delete all
     * */
    fun deleteAllItems() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                favoriteDao.delete()
            }
        }
    }

    /**
     * Delete single item
     * */
    fun deleteSingleItem(id: String?) {
        id?.let {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    favoriteDao.deleteSingleItem(it)
                }
            }
        }
    }
}
