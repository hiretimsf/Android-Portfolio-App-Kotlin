package me.tumur.portfolio.screens

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.tumur.portfolio.repository.database.dao.welcome.WelcomeDao
import me.tumur.portfolio.repository.network.Failed
import me.tumur.portfolio.repository.network.Success
import me.tumur.portfolio.repository.repo.Repository
import me.tumur.portfolio.utils.constants.Constants
import me.tumur.portfolio.utils.extensions.isNetworkAvailable
import me.tumur.portfolio.utils.state.*
import javax.inject.Inject


/**
 * MainViewModel designed to store and manage UI-related data in a lifecycle conscious way. This
 * allows data to survive configuration changes such as screen rotations. In addition, background
 * work such as fetching network results can continue through configuration changes and deliver
 * results after the new Fragment or Activity is available.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    @param:ApplicationContext private val context: Context,
    private val repo: Repository,
    private val welcomeDao: WelcomeDao
) : ViewModel() {

    /** VARIABLES * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /** Shared preferences */
    private val sharedPref: SharedPreferences = context.getSharedPreferences(Constants.APP, Context.MODE_PRIVATE)
    private val isFirstRun by lazy {
        sharedPref.getBoolean(Constants.FIRST, true)
    }

    /** Check network and cache conditions */
    private val network = (isNetworkAvailable(context))

    /** Screen state  */
    private val _screenState = MutableStateFlow<ScreenState>(SplashScreen)
    val screenStateFlow: StateFlow<ScreenState> = _screenState.asStateFlow()
    val screenState: ScreenState get() = _screenState.value

    /** Fragment state  */
    private val _fragmentState = MutableStateFlow<String?>(null)
    val fragmentStateFlow: StateFlow<String?> = _fragmentState.asStateFlow()
    val fragmentState: String? get() = _fragmentState.value

    private val _fragmentStateHolder = MutableStateFlow<String?>(null)

    /** Routed to saved Fragment state */
    private val _routed = MutableStateFlow(false)
    val routedFlow: StateFlow<Boolean> = _routed.asStateFlow()
    val routed: Boolean get() = _routed.value

    /** Navigation state  */
    private val _navigation =
        MutableStateFlow<NavigationState>(if (isFirstRun) HideNavigation else ShowNavigation)
    val navigationFlow: StateFlow<NavigationState> = _navigation.asStateFlow()
    val navigation: NavigationState get() = _navigation.value

    /** Show a toast message */
    private val _showToast = MutableStateFlow<ToastState>(ToastEmpty)
    val showToastFlow: StateFlow<ToastState> = _showToast.asStateFlow()
    val showToast: ToastState get() = _showToast.value

    /** INITIALIZATION  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * init{} is called immediately when this ViewModel is created.
     */
    init {
        /** Check first run */
        if (isFirstRun) {
            setScreenState(SplashScreen)
            viewModelScope.launch {
                populateDb()
                setScreenStateWithDelay(WelcomeScreen)
                if (network) refreshData() else setShowToast(ToastShow)
            }
        } else {
            when (val savedState = getSavedStateHandle()) {
                Constants.FRAGMENT_EMPTY -> {
                    setScreenState(SplashScreen)
                    viewModelScope.launch {
                        setScreenStateWithDelay(MainScreen)
                        if (network) refreshData() else setShowToast(ToastShow)
                    }
                }
                else -> {
                    setFragmentState(savedState)
                    setScreenState(MainScreen)
                }
            }
        }
    }

    override fun onCleared() {
        setSavedStateHandle()
        super.onCleared()
    }

    /** FUNCTIONS  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /** Set saved state handle */
    private fun setSavedStateHandle() {
        // Sets a new value for the object associated to the key.
        _fragmentStateHolder.value?.let { state ->
            savedStateHandle.set(Constants.FRAGMENT_STATE, state)
        }
    }

    /** Get saved state handle  */
    private fun getSavedStateHandle(): String {
        // Gets the current value of the user id from the saved state handle
        return savedStateHandle.get(Constants.FRAGMENT_STATE) ?: Constants.FRAGMENT_EMPTY
    }

    /** Database population at very first run */
    private suspend fun populateDb() {
        /** Fake dao is required to create and populate database from local resource  */
        withContext(Dispatchers.IO) { welcomeDao.check() }
    }

    private suspend fun refreshData() {
        when(withContext(Dispatchers.IO) { repo.fetchAll() }){
            is Failed -> setShowToast(ToastShow)
            is Success -> Unit
        }
    }

    /** Set navigation state */
    fun setNavigationState(state: NavigationState){
        _navigation.value = state
    }

    /** Set saved state handle for screen state */
    private fun setScreenState(state: ScreenState) {
        _screenState.value = state
    }

    /** Set saved state handle for screen state */
    private suspend fun setScreenStateWithDelay(state: ScreenState) {
        delay(1000L)
        _screenState.value = state
    }

    /** Set saved state handle for fragment state */
    private fun setFragmentState(state: String) {
        // Sets a new value for the object associated to the key.
        _fragmentState.value = state
    }

    fun clearFragmentState() {
        _fragmentState.value = null
    }

    /** Set saved state handle for fragment state holder */
    fun setFragmentStateHolder(stateHolder: String) {
        // Sets a new value for the object associated to the key.
        _fragmentStateHolder.value = stateHolder
    }

    /** Set routed fragment state for saved state handle */
    fun setRouted(state: Boolean) {
        // Sets a new value for the object associated to the key.
        _routed.value = state
    }

    /**
     * Set show toast
     * */
    fun setShowToast(state: ToastState) {
        _showToast.value = state
    }
}
