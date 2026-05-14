package hiretimsf.com.app.screens.portfolio

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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import hiretimsf.com.app.repository.database.dao.portfolio.PortfolioDao
import hiretimsf.com.app.repository.database.model.portfolio.PortfolioModel
import hiretimsf.com.app.repository.network.Failed
import hiretimsf.com.app.repository.network.Success
import hiretimsf.com.app.repository.repo.Repository
import hiretimsf.com.app.utils.constants.DbConstants
import hiretimsf.com.app.utils.state.ToastEmpty
import hiretimsf.com.app.utils.state.ToastShow
import hiretimsf.com.app.utils.state.ToastState
import javax.inject.Inject

@HiltViewModel
class PortfolioViewModel @Inject constructor(
    private val dao: PortfolioDao,
    private val repo: Repository
) : ViewModel() {

    /** VARIABLES * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /** Selected item id */
    private val _selectedItem = MutableStateFlow<PortfolioModel?>(null)
    val selectedItemFlow: StateFlow<PortfolioModel?> = _selectedItem.asStateFlow()
    val selectedItem: PortfolioModel? get() = _selectedItem.value

    /** Pull to refresh status  */
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshingFlow: StateFlow<Boolean> = _isRefreshing.asStateFlow()
    val isRefreshing: Boolean get() = _isRefreshing.value

    /** Show toast message from activity  */
    private val _showToast = MutableStateFlow<ToastState>(ToastEmpty)
    val showToastFlow: StateFlow<ToastState> = _showToast.asStateFlow()

    /** Portfolio pager data */
    private val config = PagingConfig(pageSize = 10, enablePlaceholders = true, initialLoadSize = 5)

    val data: Flow<PagingData<PortfolioModel>> = Pager(config) {
        dao.getListItems(DbConstants.PERSON_ID)
    }.flow.cachedIn(viewModelScope)

    /** FUNCTIONS * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Set selected item id and title
     * */
    fun setSelectedItem(item: PortfolioModel?) {
        _selectedItem.value = item
    }

    /** Fetch network data and update the database */
    fun fetch() = viewModelScope.launch {
        when (withContext(Dispatchers.IO) { repo.fetchAll() }) {
            is Failed -> {
                setShowToast(ToastShow)
                setRefreshStatus(false)
            }
            is Success -> {
                setRefreshStatus(false)
            }
        }
    }

    /** Set show toast message */
    fun setShowToast(state: ToastState) {
        _showToast.value = state
    }

    /** Set refresh status */
    fun setRefreshStatus(status: Boolean) {
        _isRefreshing.value = status
    }
}
