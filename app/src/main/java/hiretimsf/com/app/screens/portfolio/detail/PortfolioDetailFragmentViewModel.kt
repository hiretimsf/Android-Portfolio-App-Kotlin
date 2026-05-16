package hiretimsf.com.app.screens.portfolio.detail

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
import hiretimsf.com.app.repository.database.model.button.ButtonModel
import hiretimsf.com.app.repository.database.model.category.CategoryModel
import hiretimsf.com.app.repository.database.model.portfolio.PortfolioModel
import hiretimsf.com.app.repository.database.model.screenshot.ScreenShotModel
import hiretimsf.com.app.repository.repo.Repository
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class PortfolioDetailFragmentViewModel @Inject constructor(
    private val repo: Repository,
) : ViewModel() {

    /** VARIABLES * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /** Portfolio id */
    private val _id = MutableStateFlow<String?>(null)
    val idFlow: StateFlow<String?> = _id.asStateFlow()
    val id: String? get() = _id.value

    /** Button -> clicked */
    private val _buttonUrl = MutableStateFlow<String?>(null)
    val buttonUrlFlow: StateFlow<String?> = _buttonUrl.asStateFlow()
    val buttonUrl: String? get() = _buttonUrl.value

    /** Screenshot -> clicked */
    private val _clickedScreenShot = MutableStateFlow<ScreenShotModel?>(null)
    val clickedScreenShotFlow: StateFlow<ScreenShotModel?> = _clickedScreenShot.asStateFlow()
    val clickedScreenShot: ScreenShotModel? get() = _clickedScreenShot.value

    /** Video Url */
    private val _videoUrl = MutableStateFlow<String?>(null)
    val videoUrlFlow: StateFlow<String?> = _videoUrl.asStateFlow()
    val videoUrl: String? get() = _videoUrl.value

    /** Portfolio item data */
    val portfolioFlow: StateFlow<PortfolioModel?> = idFlow.filterNotNull()
        .flatMapLatest { id -> repo.observePortfolioItem(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
    val portfolio: PortfolioModel? get() = portfolioFlow.value

    val buttons: StateFlow<List<ButtonModel>> = idFlow.filterNotNull()
        .flatMapLatest { id -> repo.observePortfolioButtons(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** Category data */
    val categories: StateFlow<List<CategoryModel>> = portfolioFlow.filterNotNull()
        .flatMapLatest { portfolio -> repo.observePortfolioCategories(portfolio.categoryType) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val screenshots: StateFlow<List<ScreenShotModel>> = idFlow.filterNotNull()
        .flatMapLatest { id -> repo.observePortfolioScreenshots(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** FUNCTIONS * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Set portfolio id
     * */
    fun setPortfolioId(id: String) {
        _id.value = id
    }

    /**
     * Set clicked button
     * */
    fun setButtonUrl(url: String?) {
        _buttonUrl.value = url
    }

    /**
     * Set clicked screenshot
     * */
    fun setClickedScreenShot(model: ScreenShotModel?) {
        _clickedScreenShot.value = model
    }

    /**
     * Set video url
     * */
    fun setVideoUrl(url: String?) {
        _videoUrl.value = url
    }

}
