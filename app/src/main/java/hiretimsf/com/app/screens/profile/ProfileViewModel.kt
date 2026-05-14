package hiretimsf.com.app.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import hiretimsf.com.app.repository.database.LocalPortfolioStrings
import hiretimsf.com.app.repository.database.dao.profile.ProfileDao
import hiretimsf.com.app.repository.database.dao.profile.SocialDao
import hiretimsf.com.app.repository.database.model.profile.ProfileModel
import hiretimsf.com.app.repository.database.model.profile.SocialModel
import hiretimsf.com.app.repository.network.model.AboutSection
import hiretimsf.com.app.utils.constants.DbConstants
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileDao: ProfileDao,
    private val socialDao: SocialDao,
) : ViewModel() {

    /** VARIABLES * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /** Profile */
    val profileFlow: StateFlow<ProfileModel?> = profileDao.getSingleItem(DbConstants.PERSON_ID)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
    val profile: ProfileModel? get() = profileFlow.value

    private val _aboutScreenFlow = MutableStateFlow(AboutScreenState())
    val aboutScreenFlow: StateFlow<AboutScreenState> = _aboutScreenFlow.asStateFlow()

    init {
        val about = LocalPortfolioStrings.about.data
        _aboutScreenFlow.value = AboutScreenState(
            sections = about.sections,
            introductionImages = about.photos.map { photo ->
                CarouselImage(
                    url = photo.url,
                    description = photo.alt,
                )
            },
        )
    }

    /** Social*/
    val socialFlow: StateFlow<List<SocialModel>> = socialDao.getListItems(DbConstants.PERSON_ID)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val social: List<SocialModel> get() = socialFlow.value


    /** Profile bottom sheet dialog */
    private val _showProfileBottomSheet = MutableStateFlow(false)
    val showProfileBottomSheetFlow: StateFlow<Boolean> = _showProfileBottomSheet.asStateFlow()

    /** Social item on click */
    private val _socialItemOnClick = MutableStateFlow<SocialModel?>(null)
    val socialItemOnClickFlow: StateFlow<SocialModel?> = _socialItemOnClick.asStateFlow()

    /** Email item on click */
    private val _emailItemOnClick = MutableStateFlow(false)
    val emailItemOnClickFlow: StateFlow<Boolean> = _emailItemOnClick.asStateFlow()

    /** FUNCTIONS * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Set show profile bottom sheet
     * */
    fun setShowProfileBottomsheet(status: Boolean){
        _showProfileBottomSheet.value = status
    }
    // Set back to @false after it showed
    fun resetShowProfileBottomsheet(){
        _showProfileBottomSheet.value = false
    }

    /**
     * Set social item onClick event
     * */
    fun socialItemOnClick(item: SocialModel){
        _socialItemOnClick.value = item
    }
    // Set back to @null after it navigated
    fun socialItemClicked(){
        _socialItemOnClick.value = null
    }

    /**
     * Set email item onClick event
     * */
    fun emailItemOnClick(status: Boolean){
        _emailItemOnClick.value = status
    }
    // Set back to @null after it navigated
    fun emailItemClicked(){
        _emailItemOnClick.value = false
    }

}

data class AboutScreenState(
    val sections: List<AboutSection> = emptyList(),
    val introductionImages: List<CarouselImage> = emptyList(),
)
