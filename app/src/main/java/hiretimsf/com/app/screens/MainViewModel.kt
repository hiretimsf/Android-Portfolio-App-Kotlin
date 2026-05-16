package hiretimsf.com.app.screens

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import hiretimsf.com.app.repository.database.model.portfolio.PortfolioModel
import hiretimsf.com.app.repository.network.Failed
import hiretimsf.com.app.repository.network.model.AboutSection
import hiretimsf.com.app.repository.network.Success
import hiretimsf.com.app.repository.repo.Repository
import hiretimsf.com.app.screens.blog.BlogPost
import hiretimsf.com.app.utils.constants.Constants
import hiretimsf.com.app.utils.constants.DbConstants
import hiretimsf.com.app.utils.state.*
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
) : ViewModel() {

    /** VARIABLES * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /** Shared preferences */
    private val sharedPref: SharedPreferences = context.getSharedPreferences(Constants.APP, Context.MODE_PRIVATE)
    private val isFirstRun by lazy {
        sharedPref.getBoolean(Constants.FIRST, true)
    }

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

    private val _searchQuery = MutableStateFlow("")
    val searchQueryFlow: StateFlow<String> = _searchQuery.asStateFlow()

    val searchState: StateFlow<GlobalSearchState> = combine(
        _searchQuery,
        repo.observePortfolio(DbConstants.PERSON_ID),
        repo.observeBlogPosts(),
        repo.observeAbout(),
    ) { query, projects, blogPosts, about ->
        val trimmedQuery = query.trim()
        GlobalSearchState(
            query = query,
            results = if (trimmedQuery.isBlank()) {
                emptyList()
            } else {
                buildSearchResults(
                    query = trimmedQuery,
                    projects = projects,
                    blogPosts = blogPosts,
                    aboutSections = about.sections,
                )
            },
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), GlobalSearchState())

    /** INITIALIZATION  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * init{} is called immediately when this ViewModel is created.
     */
    init {
        /** Check first run */
        if (isFirstRun) {
            setScreenState(SplashScreen)
            viewModelScope.launch {
                setScreenStateWithDelay(WelcomeScreen)
                refreshData()
            }
        } else {
            when (val savedState = getSavedStateHandle()) {
                Constants.FRAGMENT_EMPTY -> {
                    setScreenState(SplashScreen)
                    viewModelScope.launch {
                        setScreenStateWithDelay(MainScreen)
                        refreshData()
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

    private suspend fun refreshData() {
        when(withContext(Dispatchers.IO) { repo.fetchAll() }){
            is Failed -> setShowToast(ToastShow)
            is Success -> Unit
        }
        withContext(Dispatchers.IO) {
            repo.fetchAbout()
            repo.fetchBlogPosts()
        }
    }

    /** Set navigation state */
    fun setNavigationState(state: NavigationState){
        _navigation.value = state
    }

    fun finishWelcome() {
        _screenState.value = MainScreen
        _navigation.value = ShowNavigation
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

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun clearSearchQuery() {
        _searchQuery.value = ""
    }
}

data class GlobalSearchState(
    val query: String = "",
    val results: List<GlobalSearchResult> = emptyList(),
)

data class GlobalSearchResult(
    val id: String,
    val title: String,
    val subtitle: String,
    val type: GlobalSearchResultType,
)

enum class GlobalSearchResultType(val label: String) {
    Project("Project"),
    BlogPost("Blog"),
    About("About"),
}

private fun buildSearchResults(
    query: String,
    projects: List<PortfolioModel>,
    blogPosts: List<BlogPost>,
    aboutSections: List<AboutSection>,
): List<GlobalSearchResult> {
    val projectResults = projects
        .filter { project ->
            listOf(project.title, project.subTitle, project.text, project.info, project.header)
                .any { it.contains(query, ignoreCase = true) }
        }
        .map { project ->
            GlobalSearchResult(
                id = project.id,
                title = project.title,
                subtitle = project.text.searchSnippet(query),
                type = GlobalSearchResultType.Project,
            )
        }

    val blogResults = blogPosts
        .filter { post ->
            listOf(
                post.title,
                post.excerpt,
                post.category,
                post.readTime,
                post.content,
                post.sections.joinToString(" ") { "${it.title} ${it.content}" },
            ).any { it.contains(query, ignoreCase = true) }
        }
        .map { post ->
            GlobalSearchResult(
                id = post.slug,
                title = post.title,
                subtitle = post.excerpt.searchSnippet(query),
                type = GlobalSearchResultType.BlogPost,
            )
        }

    val aboutResults = aboutSections
        .filter { section ->
            section.title.contains(query, ignoreCase = true) ||
                section.content.contains(query, ignoreCase = true)
        }
        .mapIndexed { index, section ->
            GlobalSearchResult(
                id = "about-$index",
                title = section.title,
                subtitle = section.content.searchSnippet(query),
                type = GlobalSearchResultType.About,
            )
        }

    return projectResults + blogResults + aboutResults
}

private fun String.searchSnippet(query: String, maxLength: Int = 120): String {
    val cleanedText = replace('\n', ' ').replace(Regex("\\s+"), " ").trim()
    if (cleanedText.length <= maxLength) return cleanedText

    val matchIndex = cleanedText.indexOf(query, ignoreCase = true)
    val start = if (matchIndex == -1) 0 else (matchIndex - 32).coerceAtLeast(0)
    val end = (start + maxLength).coerceAtMost(cleanedText.length)
    val prefix = if (start > 0) "... " else ""
    val suffix = if (end < cleanedText.length) " ..." else ""
    return prefix + cleanedText.substring(start, end).trim() + suffix
}
