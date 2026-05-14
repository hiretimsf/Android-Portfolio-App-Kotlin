package hiretimsf.com.app.screens.portfolio.detail.preview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import hiretimsf.com.app.repository.database.dao.screenshot.ScreenShotDao
import hiretimsf.com.app.repository.database.model.screenshot.ScreenShotModel
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class PreviewFragmentViewModel @Inject constructor(
    private val screenShotDao: ScreenShotDao
) : ViewModel() {

    /** VARIABLES * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /** Id */
    private val _id = MutableStateFlow<String?>(null)
    val idFlow: StateFlow<String?> = _id.asStateFlow()

    /** Screenshots */
    val dataFlow: StateFlow<List<ScreenShotModel>> = idFlow.filterNotNull()
        .flatMapLatest { id -> screenShotDao.getListItems(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val data: List<ScreenShotModel> get() = dataFlow.value

    /** Current item of view pager  */
    private val _currentItem = MutableStateFlow(0)
    val currentItemFlow: StateFlow<Int> = _currentItem.asStateFlow()
    val currentItem: Int get() = _currentItem.value

    /** ScrollTo item of view pager  */
    private val _scrollToItem = MutableStateFlow<Int?>(null)
    val scrollToItemFlow: StateFlow<Int?> = _scrollToItem.asStateFlow()
    val scrollToItem: Int? get() = _scrollToItem.value

    /** FUNCTIONS * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Set id
     * */
    fun setId(id: String?) {
        id?.let {
            _id.value = it
        }
    }

    /**
     * Set viewpager's current item for icon animation
     */
    fun setCurrentItem(position: Int) {
        _currentItem.value = position
    }

    /**
     * Set viewpager's scroll to item
     */
    fun setScrollToItem(position: Int) {
        _scrollToItem.value = position
    }

    /**
     * Set viewpager's scroll to item
     */
    fun getSingleScreenShot(position: Int): ScreenShotModel? {
        return data.getOrNull(position)
    }
}
