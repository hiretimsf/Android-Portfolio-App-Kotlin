package hiretimsf.com.app.screens.portfolio.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import hiretimsf.com.app.repository.database.dao.button.ButtonDao
import hiretimsf.com.app.repository.database.dao.category.CategoryDao
import hiretimsf.com.app.repository.database.dao.favorite.FavoriteDao
import hiretimsf.com.app.repository.database.dao.portfolio.PortfolioDao
import hiretimsf.com.app.repository.database.dao.screenshot.ScreenShotDao
import hiretimsf.com.app.repository.database.model.button.ButtonModel
import hiretimsf.com.app.repository.database.model.category.CategoryModel
import hiretimsf.com.app.repository.database.model.favorite.FavoriteModel
import hiretimsf.com.app.repository.database.model.portfolio.PortfolioModel
import hiretimsf.com.app.repository.database.model.screenshot.ScreenShotModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class PortfolioDetailFragmentViewModel @Inject constructor(
    private val portfolioDao: PortfolioDao,
    private val buttonDao: ButtonDao,
    private val categoryDao: CategoryDao,
    private val screenshotDao: ScreenShotDao,
    private val favoriteDao: FavoriteDao
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
        .flatMapLatest { id -> portfolioDao.getSingleItem(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
    val portfolio: PortfolioModel? get() = portfolioFlow.value

    /** Is it favorite? */
    val favoriteFlow: StateFlow<Int> = idFlow.filterNotNull()
        .flatMapLatest { id -> favoriteDao.existSingleItem(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)
    val favorite: Int get() = favoriteFlow.value

    /** Button data */
    private val configButton = PagingConfig(pageSize = 3, enablePlaceholders = true, initialLoadSize = 3)

    val button: Flow<PagingData<ButtonModel>> = idFlow.filterNotNull()
        .flatMapLatest { id -> Pager(configButton) { buttonDao.getListItems(id) }.flow }
        .cachedIn(viewModelScope)

    /** Category data */
    val category: Flow<PagingData<CategoryModel>> = portfolioFlow.filterNotNull()
        .flatMapLatest { portfolio -> Pager(configButton) { categoryDao.getListItems(portfolio.categoryType) }.flow }
        .cachedIn(viewModelScope)

    /** Screenshot data */
    private val configScreenShot = PagingConfig(pageSize = 5, enablePlaceholders = true, initialLoadSize = 5)

    val screenshot: Flow<PagingData<ScreenShotModel>> = idFlow.filterNotNull()
        .flatMapLatest { id -> Pager(configScreenShot) { screenshotDao.getPagedItems(id) }.flow }
        .cachedIn(viewModelScope)

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

    /**
     * Save as favorite
     * */
    fun saveToFavorite(item: PortfolioModel) {
        val favorite = FavoriteModel(
            id = item.id,
            ownerId = item.ownerId,
            title = item.title,
            subTitle = item.subTitle,
            logo = item.logo,
            logoDescription = item.logoDescription,
            coverImage = item.coverImage,
            imageDescription = item.imageDescription,
            text = item.text,
            info = item.info,
            dateFrom = item.dateFrom,
            dateTo = item.dateTo,
            header = item.header,
            categoryType = item.categoryType,
            videoUrl = item.videoUrl,
            order = item.order,
            date = Calendar.getInstance().time
        )

        viewModelScope.launch(Dispatchers.IO) {
            /** Insert a portfolio item in to favorite table */
            favoriteDao.insert(favorite)
        }
    }

    /**
     * Remove from favorite
     * */
    fun removeFromFavorite(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            /** Remove a favorite item */
            favoriteDao.deleteSingleItem(id)
        }
    }
}
