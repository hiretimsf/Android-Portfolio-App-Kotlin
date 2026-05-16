package hiretimsf.com.app.screens.blog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hiretimsf.com.app.R
import hiretimsf.com.app.screens.blog.components.BlogHeader
import hiretimsf.com.app.screens.blog.components.BlogPostCard
import hiretimsf.com.app.screens.shared.components.InlineError
import hiretimsf.com.app.screens.shared.components.LoadingState
import hiretimsf.com.app.screens.shared.components.NoInternetState

@Composable
fun BlogComposeScreen(
    state: BlogScreenState,
    onRefresh: () -> Unit,
    onBlogPostClick: (BlogPost) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (state.isLoading && state.posts.isEmpty()) {
        LoadingState(modifier = modifier)
        return
    }

    if (state.showNoInternet) {
        NoInternetState(
            message = state.errorMessage.orEmpty(),
            onRetry = onRefresh,
            modifier = modifier,
        )
        return
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.colorSurface)),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
    ) {
        item { BlogHeader() }
        state.errorMessage?.let { message ->
            item { InlineError(message = message) }
        }
        items(
            items = state.posts,
            key = { it.slug },
        ) { post ->
            BlogPostCard(
                post = post,
                onClick = { onBlogPostClick(post) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BlogComposeScreenPreview() {
    BlogComposeScreen(
        state = BlogScreenState(posts = placeholderBlogPosts),
        onRefresh = {},
        onBlogPostClick = {},
    )
}
