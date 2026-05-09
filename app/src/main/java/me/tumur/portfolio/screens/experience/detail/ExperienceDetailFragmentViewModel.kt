package me.tumur.portfolio.screens.experience.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import me.tumur.portfolio.repository.database.dao.button.ButtonDao
import me.tumur.portfolio.repository.database.dao.experience.ExperienceDao
import me.tumur.portfolio.repository.database.dao.location.LocationDao
import me.tumur.portfolio.repository.database.dao.resource.ResourceDao
import me.tumur.portfolio.repository.database.dao.task.TaskDao
import me.tumur.portfolio.repository.database.model.LocationModel
import me.tumur.portfolio.repository.database.model.button.ButtonModel
import me.tumur.portfolio.repository.database.model.experience.ExperienceModel
import me.tumur.portfolio.repository.database.model.resource.ResourceModel
import me.tumur.portfolio.repository.database.model.task.TaskModel
import me.tumur.portfolio.utils.state.FavoriteState
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class ExperienceDetailFragmentViewModel @Inject constructor(
    private val experienceDao: ExperienceDao,
    private val buttonDao: ButtonDao,
    private val taskDao: TaskDao,
    private val locationDao: LocationDao,
    private val resourceDao: ResourceDao
) : ViewModel() {

    /** VARIABLES * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /** Experience item id */
    private val _id = MutableStateFlow<String?>(null)
    val idFlow: StateFlow<String?> = _id.asStateFlow()
    val id: String? get() = _id.value

    /** Experience item data */
    val dataFlow: StateFlow<ExperienceModel?> = idFlow.filterNotNull()
        .flatMapLatest { id -> experienceDao.getSingleItem(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
    val data: ExperienceModel? get() = dataFlow.value

    /** Button data */
    private val configButton = PagingConfig(pageSize = 3, enablePlaceholders = true, initialLoadSize = 3)

    val button: Flow<PagingData<ButtonModel>> = idFlow.filterNotNull()
        .flatMapLatest { id -> Pager(configButton) { buttonDao.getListItems(id) }.flow }
        .cachedIn(viewModelScope)

    /** Task data */
    private val configTask = PagingConfig(pageSize = 10, enablePlaceholders = true, initialLoadSize = 10)

    val task: Flow<PagingData<TaskModel>> = idFlow.filterNotNull()
        .flatMapLatest { id -> Pager(configTask) { taskDao.getListItems(id) }.flow }
        .cachedIn(viewModelScope)

    /** Resource data */
    private val configResource = PagingConfig(pageSize = 5, enablePlaceholders = true, initialLoadSize = 5)

    val resource: Flow<PagingData<ResourceModel>> = idFlow.filterNotNull()
        .flatMapLatest { id -> Pager(configResource) { resourceDao.getListItems(id) }.flow }
        .cachedIn(viewModelScope)

    /** Resource state */
    private val _resourceState = MutableStateFlow<FavoriteState?>(null)
    val resourceStateFlow: StateFlow<FavoriteState?> = _resourceState.asStateFlow()
    val resourceState: FavoriteState? get() = _resourceState.value

    /** Experience item data */
    val checkResourceTable: StateFlow<Int> = idFlow.filterNotNull()
        .flatMapLatest { id -> resourceDao.check(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    /** Location data */
    val locationFlow: StateFlow<LocationModel?> = idFlow.filterNotNull()
        .flatMapLatest { id -> locationDao.getSingleItem(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    /** Url -> clicked */
    private val _url = MutableStateFlow<String?>(null)
    val urlFlow: StateFlow<String?> = _url.asStateFlow()

    /** FUNCTIONS * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Set experience item id
     * */
    fun setExperienceItemId(id: String) {
        _id.value = id
    }

    /**
     * Set clicked url
     * */
    fun setUrl(url: String?) {
        _url.value = url
    }

    /**
     * Set resource state
     * */
    fun setResourceState(state: FavoriteState) {
        _resourceState.value = state
    }
}
