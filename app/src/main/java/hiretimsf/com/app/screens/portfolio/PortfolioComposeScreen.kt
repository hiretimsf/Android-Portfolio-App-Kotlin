package hiretimsf.com.app.screens.portfolio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hiretimsf.com.app.R
import hiretimsf.com.app.repository.database.model.portfolio.PortfolioModel
import hiretimsf.com.app.screens.portfolio.components.PortfolioCard
import hiretimsf.com.app.screens.portfolio.components.PortfolioHeader
import hiretimsf.com.app.screens.shared.components.InlineError
import hiretimsf.com.app.screens.shared.components.NoInternetState
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioComposeScreen(
    state: PortfolioScreenState,
    onRefresh: () -> Unit,
    onPortfolioClick: (PortfolioModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (state.showNoInternet) {
        NoInternetState(
            message = state.errorMessage.orEmpty(),
            onRetry = onRefresh,
            modifier = modifier,
        )
        return
    }

    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.colorSurface)),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.colorSurface)),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        ) {
            item { PortfolioHeader() }
            state.errorMessage?.let { message ->
                item { InlineError(message = message) }
            }
            items(
                items = state.items,
                key = { it.id },
            ) { item ->
                PortfolioCard(
                    item = item,
                    onClick = { onPortfolioClick(item) },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PortfolioComposeScreenPreview() {
    PortfolioComposeScreen(
        state = PortfolioScreenState(
            items = listOf(
                PortfolioModel(
                    id = "preview",
                    ownerId = "owner",
                    title = "Portfolio App",
                    subTitle = "Portfolio App 2.0",
                    logo = "",
                    logoDescription = "Logo",
                    coverImage = "",
                    imageDescription = "Cover",
                    text = "Applied best practices from the Android community and shared knowledge through a working portfolio app.",
                    info = "Kotlin, Compose, Room",
                    dateFrom = Date(1541113200000),
                    dateTo = Date(1565161200000),
                    header = "Android Apps",
                    categoryType = 1,
                    videoUrl = null,
                    linkToShare = null,
                    order = 1,
                ),
            ),
        ),
        onRefresh = {},
        onPortfolioClick = {},
    )
}
