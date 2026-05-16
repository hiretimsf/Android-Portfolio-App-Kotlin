package hiretimsf.com.app.screens.portfolio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
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
    private val repo: Repository
) : ViewModel() {

    /** VARIABLES * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /** Selected item id */
    private val _selectedItem = MutableStateFlow<PortfolioModel?>(null)
    val selectedItemFlow: StateFlow<PortfolioModel?> = _selectedItem.asStateFlow()
    val selectedItem: PortfolioModel? get() = _selectedItem.value

    /** Pull to refresh status  */
    private val _isRefreshing = MutableStateFlow(false)

    private val _query = MutableStateFlow("")
    val queryFlow: StateFlow<String> = _query.asStateFlow()

    /** Show toast message from activity  */
    private val _showToast = MutableStateFlow<ToastState>(ToastEmpty)
    val showToastFlow: StateFlow<ToastState> = _showToast.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)

    val state: StateFlow<PortfolioScreenState> = combine(
        repo.observePortfolio(DbConstants.PERSON_ID),
        _isRefreshing,
        _query,
        _errorMessage,
    ) { items, isRefreshing, query, errorMessage ->
        val trimmedQuery = query.trim()
        val filteredItems = if (trimmedQuery.isBlank()) {
            items
        } else {
            items.filter { item ->
                item.title.contains(trimmedQuery, ignoreCase = true) ||
                    item.subTitle.contains(trimmedQuery, ignoreCase = true) ||
                    item.text.contains(trimmedQuery, ignoreCase = true) ||
                    item.info.contains(trimmedQuery, ignoreCase = true) ||
                    item.header.contains(trimmedQuery, ignoreCase = true)
            }
        }

        PortfolioScreenState(
            items = filteredItems,
            isRefreshing = isRefreshing,
            query = query,
            errorMessage = errorMessage,
            showNoInternet = errorMessage != null && items.isEmpty(),
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), PortfolioScreenState())

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
                _errorMessage.value = "Unable to refresh projects. Showing saved projects when available."
                setShowToast(ToastShow)
                setRefreshStatus(false)
            }
            is Success -> {
                _errorMessage.value = null
                setRefreshStatus(false)
            }
        }
    }

    fun setQuery(query: String) {
        _query.value = query
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

data class PortfolioScreenState(
    val items: List<PortfolioModel> = emptyList(),
    val isRefreshing: Boolean = false,
    val query: String = "",
    val errorMessage: String? = null,
    val showNoInternet: Boolean = false,
)
