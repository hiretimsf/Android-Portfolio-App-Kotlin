package me.tumur.portfolio.screens.profile

import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import me.tumur.portfolio.repository.database.dao.profile.AboutDao
import me.tumur.portfolio.repository.database.dao.profile.ProfileDao
import me.tumur.portfolio.repository.database.dao.profile.SocialDao
import me.tumur.portfolio.repository.database.model.profile.AboutModel
import me.tumur.portfolio.repository.database.model.profile.ProfileModel
import me.tumur.portfolio.repository.database.model.profile.SocialModel
import me.tumur.portfolio.utils.constants.DbConstants
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileDao: ProfileDao,
    private val aboutDao: AboutDao,
    private val socialDao: SocialDao
) : ViewModel() {

    /** VARIABLES * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /** Profile */
    val profileFlow: StateFlow<ProfileModel?> = profileDao.getSingleItem(DbConstants.PERSON_ID)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
    val profile: ProfileModel? get() = profileFlow.value

    /** About */
    val aboutFlow: StateFlow<List<AboutModel>> = aboutDao.getListItems(DbConstants.PERSON_ID)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val about: List<AboutModel> get() = aboutFlow.value

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
