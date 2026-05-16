package hiretimsf.com.app.screens.blog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hiretimsf.com.app.repository.network.Failed
import hiretimsf.com.app.repository.network.Success
import hiretimsf.com.app.repository.repo.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BlogViewModel @Inject constructor(
    private val repo: Repository,
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    private val _query = MutableStateFlow("")
    private val _errorMessage = MutableStateFlow<String?>(null)

    val state: StateFlow<BlogScreenState> = combine(
        repo.observeBlogPosts(),
        _isLoading,
        _query,
        _errorMessage,
    ) { posts, isLoading, query, errorMessage ->
        val trimmedQuery = query.trim()
        val filteredPosts = if (trimmedQuery.isBlank()) {
            posts
        } else {
            posts.filter { post ->
                post.title.contains(trimmedQuery, ignoreCase = true) ||
                    post.excerpt.contains(trimmedQuery, ignoreCase = true) ||
                    post.category.contains(trimmedQuery, ignoreCase = true) ||
                    post.readTime.contains(trimmedQuery, ignoreCase = true)
            }
        }

        BlogScreenState(
            posts = filteredPosts,
            isLoading = isLoading && posts.isEmpty(),
            query = query,
            errorMessage = errorMessage,
            showNoInternet = errorMessage != null && posts.isEmpty(),
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), BlogScreenState(isLoading = true))

    init {
        fetchBlogPosts()
    }

    fun fetchBlogPosts() = viewModelScope.launch {
        _isLoading.value = true
        _errorMessage.value = null

        when (withContext(Dispatchers.IO) { repo.fetchBlogPosts() }) {
            is Failed -> {
                _errorMessage.value = "Unable to refresh blog posts. Showing saved posts when available."
                _isLoading.value = false
            }
            is Success -> {
                _errorMessage.value = null
                _isLoading.value = false
            }
        }
    }

    fun setQuery(query: String) {
        _query.value = query
    }
}

data class BlogScreenState(
    val posts: List<BlogPost> = emptyList(),
    val isLoading: Boolean = false,
    val query: String = "",
    val errorMessage: String? = null,
    val showNoInternet: Boolean = false,
)
