package hiretimsf.com.app.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hiretimsf.com.app.R
import hiretimsf.com.app.screens.GlobalSearchResult
import hiretimsf.com.app.screens.GlobalSearchState

private val searchFontFamily = FontFamily(Font(R.font.questrial))

@Composable
fun GlobalSearchDialog(
    state: GlobalSearchState,
    onQueryChange: (String) -> Unit,
    onResultClick: (GlobalSearchResult) -> Unit,
    onDismiss: () -> Unit,
) {
    val focusRequester = FocusRequester()

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.search_title),
                fontFamily = searchFontFamily,
                fontWeight = FontWeight.Bold,
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = state.query,
                    onValueChange = onQueryChange,
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                )
                Spacer(Modifier.height(16.dp))
                SearchResultsContent(
                    state = state,
                    onResultClick = onResultClick,
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.button_dismiss),
                    fontFamily = searchFontFamily,
                )
            }
        },
    )
}

@Composable
private fun SearchResultsContent(
    state: GlobalSearchState,
    onResultClick: (GlobalSearchResult) -> Unit,
) {
    when {
        state.query.isBlank() -> SearchMessage(text = stringResource(R.string.search_empty_prompt))
        state.results.isEmpty() -> SearchMessage(text = stringResource(R.string.search_no_results))
        else -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 360.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp),
            ) {
                items(
                    items = state.results,
                    key = { "${it.type.name}-${it.id}" },
                ) { result ->
                    SearchResultRow(
                        result = result,
                        onClick = { onResultClick(result) },
                    )
                    HorizontalDivider(color = colorResource(R.color.colorBorder))
                }
            }
        }
    }
}

@Composable
private fun SearchMessage(text: String) {
    Text(
        text = text,
        color = colorResource(R.color.colorOnSurface),
        fontFamily = searchFontFamily,
        fontSize = 15.sp,
        lineHeight = 22.sp,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun SearchResultRow(
    result: GlobalSearchResult,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = result.type.label,
                color = colorResource(R.color.colorHeaderTitle),
                fontFamily = searchFontFamily,
                fontSize = 12.sp,
                maxLines = 1,
                modifier = Modifier
                    .background(colorResource(R.color.colorHeaderBackground), RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 3.dp),
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = result.title,
                color = colorResource(R.color.colorOnPrimarySurface),
                fontFamily = searchFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
        }
        Text(
            text = result.subtitle,
            color = colorResource(R.color.colorOnSurface),
            fontFamily = searchFontFamily,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 6.dp),
        )
    }
}
