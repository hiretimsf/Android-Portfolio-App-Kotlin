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
import hiretimsf.com.app.R
import hiretimsf.com.app.repository.database.dao.welcome.WelcomeDao
import hiretimsf.com.app.repository.database.model.welcome.WelcomeModel
import hiretimsf.com.app.utils.constants.Constants
import javax.inject.Inject

/**
 * WelcomeViewModel designed to store and manage UI-related data in a lifecycle conscious way. This
 * allows data to survive configuration changes such as screen rotations. In addition, background
 * work such as fetching network results can continue through configuration changes and deliver
 * results after the new Fragment or Activity is available.
 */

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val welcomeDao: WelcomeDao,
    @param:ApplicationContext private val context: Context
) : ViewModel() {

    /** VARIABLES * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /** Shared preferences */
    private val sharedPref: SharedPreferences = context.getSharedPreferences(Constants.APP, Context.MODE_PRIVATE)

    /** Welcome data */
    val welcomeScreenFlow: StateFlow<List<WelcomeModel>> = welcomeDao.getListItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val welcomeScreen: List<WelcomeModel> get() = welcomeScreenFlow.value

    /** Current item of view pager  */
    private val _currentItem = MutableStateFlow(0)
    val currentItemFlow: StateFlow<Int> = _currentItem.asStateFlow()
    val currentItem: Int get() = _currentItem.value

    /** ScrollTo item of view pager  */
    private val _scrollToItem = MutableStateFlow<Int?>(null)
    val scrollToItemFlow: StateFlow<Int?> = _scrollToItem.asStateFlow()
    val scrollToItem: Int? get() = _scrollToItem.value

    /** Skip and next button clicked  */
    private val _onClicked = MutableStateFlow(false)
    val onClickedFlow: StateFlow<Boolean> = _onClicked.asStateFlow()

    /** Skip and get started button text  */
    private val _buttonText = MutableStateFlow(context.getString(R.string.button_next))
    val buttonTextFlow: StateFlow<String> = _buttonText.asStateFlow()
    val buttonText: String get() = _buttonText.value

    /** FUNCTIONS * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Set shared preferences
     * First run as {@Boolean as parameter}
     */
    fun setFirstRunAs(value: Boolean) {
        sharedPref.edit {
            putBoolean(Constants.FIRST, value)
        }
    }

    /**
     * Get welcome screen data
     * */
    fun getWelcomeScreenData(position: Int): WelcomeModel?{
        return welcomeScreen.getOrNull(position)
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
    fun setOnClicked(status: Boolean) {
        _onClicked.value = status
    }

    /**
     * Set skip and get started button text
     */
    fun setButtonText(text: String) {
        _buttonText.value = text
    }
}
