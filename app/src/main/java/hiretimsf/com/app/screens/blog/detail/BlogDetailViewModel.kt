package hiretimsf.com.app.screens.blog.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hiretimsf.com.app.repository.network.Failed
import hiretimsf.com.app.repository.network.Success
import hiretimsf.com.app.repository.repo.Repository
import hiretimsf.com.app.screens.blog.BlogPost
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class BlogDetailViewModel @Inject constructor(
    private val repo: Repository,
) : ViewModel() {

    private val _slug = MutableStateFlow("")
    private val _isLoading = MutableStateFlow(true)
    private val _errorMessage = MutableStateFlow<String?>(null)
    private var fetchJob: Job? = null

    val state: StateFlow<BlogDetailScreenState> = combine(
        _slug.flatMapLatest { slug ->
            if (slug.isBlank()) flowOf(null) else repo.observeBlogPost(slug)
        },
        _isLoading,
        _errorMessage,
    ) { post, isLoading, errorMessage ->
        BlogDetailScreenState(
            post = post,
            isLoading = isLoading && post == null,
            errorMessage = errorMessage,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), BlogDetailScreenState(isLoading = true))

    fun fetchBlogPost(slug: String) = viewModelScope.launch {
        if (slug.isBlank()) return@launch
        _slug.value = slug
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            when (withContext(Dispatchers.IO) { repo.fetchBlogPost(slug) }) {
                is Failed -> {
                    _errorMessage.value = "Unable to refresh this blog post. Showing saved content when available."
                    _isLoading.value = false
                }
                is Success -> {
                    _errorMessage.value = null
                    _isLoading.value = false
                }
            }
        }
    }
}

data class BlogDetailScreenState(
    val post: BlogPost? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
