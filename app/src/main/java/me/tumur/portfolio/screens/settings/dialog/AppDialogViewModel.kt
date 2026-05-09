package me.tumur.portfolio.screens.settings.dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import me.tumur.portfolio.repository.database.dao.settings.AppDao
import me.tumur.portfolio.repository.database.model.settings.AppModel
import javax.inject.Inject

@HiltViewModel
class AppDialogViewModel @Inject constructor(
    private val appDao: AppDao
) : ViewModel() {

    /** VARIABLES * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /** Profile data */
    val appInfo: StateFlow<List<AppModel>> = appDao.getListItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** Close button on click */
    private val _closeButtonOnClick = MutableStateFlow(false)
    val closeButtonOnClick: StateFlow<Boolean> = _closeButtonOnClick.asStateFlow()

    /** FUNCTIONS * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Set close button onClick event
     * */
    fun setCloseButtonOnClick(status: Boolean){
        _closeButtonOnClick.value = status
    }
}
