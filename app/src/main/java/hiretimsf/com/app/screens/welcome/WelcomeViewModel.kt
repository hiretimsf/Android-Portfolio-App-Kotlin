package hiretimsf.com.app.screens.welcome

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import hiretimsf.com.app.repository.database.dao.welcome.WelcomeDao
import hiretimsf.com.app.repository.database.model.welcome.WelcomeModel
import hiretimsf.com.app.utils.constants.Constants
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val welcomeDao: WelcomeDao,
    @ApplicationContext context: Context,
) : ViewModel() {

    private val sharedPref: SharedPreferences = context.getSharedPreferences(Constants.APP, Context.MODE_PRIVATE)

    val welcomeScreenFlow: StateFlow<List<WelcomeModel>> = welcomeDao.getListItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _currentItem = MutableStateFlow(0)
    val currentItemFlow: StateFlow<Int> = _currentItem.asStateFlow()

    private val _scrollToItem = MutableStateFlow<Int?>(null)
    val scrollToItemFlow: StateFlow<Int?> = _scrollToItem.asStateFlow()

    fun setFirstRunAs(value: Boolean) {
        sharedPref.edit {
            putBoolean(Constants.FIRST, value)
        }
    }

    fun setCurrentItem(position: Int) {
        _currentItem.value = position
    }

    fun setScrollToItem(position: Int) {
        _scrollToItem.value = position
    }
}
