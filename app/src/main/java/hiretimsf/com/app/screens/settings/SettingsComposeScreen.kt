package hiretimsf.com.app.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hiretimsf.com.app.R
import hiretimsf.com.app.screens.settings.components.SettingsPreferenceItem
import hiretimsf.com.app.screens.settings.components.SettingsSection
import hiretimsf.com.app.screens.settings.components.ThemeOption
import hiretimsf.com.app.screens.settings.components.ThemePickerDialog
import hiretimsf.com.app.utils.constants.Constants

@Composable
fun SettingsComposeScreen(
    appVersion: String,
    selectedThemeValue: String,
    themeOptions: List<ThemeOption>,
    onThemeSelected: (String) -> Unit,
    onAppVersionClick: () -> Unit,
    onSourceCodeClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onDataDeletionClick: () -> Unit,
    onRateClick: () -> Unit,
    onTwitterClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showThemeDialog by remember { mutableStateOf(false) }
    val selectedThemeLabel = themeOptions.firstOrNull { it.value == selectedThemeValue }?.label
        ?: themeOptions.firstOrNull { it.value == Constants.THEME_DEFAULT }?.label.orEmpty()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.colorSurface))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        SettingsSection(title = stringResource(R.string.category_theme_options)) {
            SettingsPreferenceItem(
                icon = R.drawable.ic_theme,
                title = stringResource(R.string.title_choose_theme),
                summary = selectedThemeLabel,
                onClick = { showThemeDialog = true },
            )
        }

        SettingsSection(title = stringResource(R.string.category_app_info)) {
            SettingsPreferenceItem(
                icon = R.drawable.ic_app_version,
                title = stringResource(R.string.title_app_version),
                summary = appVersion,
                onClick = onAppVersionClick,
                showDivider = true,
            )
            SettingsPreferenceItem(
                icon = R.drawable.ic_github,
                title = stringResource(R.string.title_source_code),
                summary = stringResource(R.string.summary_source_code),
                onClick = onSourceCodeClick,
                showDivider = true,
            )
            SettingsPreferenceItem(
                icon = R.drawable.ic_privacy,
                title = stringResource(R.string.title_privacy),
                summary = stringResource(R.string.summary_external_link),
                onClick = onPrivacyClick,
                showDivider = true,
            )
            SettingsPreferenceItem(
                icon = R.drawable.ic_privacy,
                title = stringResource(R.string.title_data_deletion),
                summary = stringResource(R.string.summary_data_deletion),
                onClick = onDataDeletionClick,
                showDivider = true,
            )
            SettingsPreferenceItem(
                icon = R.drawable.ic_rate,
                title = stringResource(R.string.title_rate),
                summary = stringResource(R.string.summary_external_link),
                onClick = onRateClick,
            )
        }

        SettingsSection(title = stringResource(R.string.category_made)) {
            SettingsPreferenceItem(
                icon = R.drawable.profile,
                title = stringResource(R.string.title_tumur),
                summary = stringResource(R.string.summary_tumur),
                iconSize = 44.dp,
                iconTint = null,
                showIconContainer = false,
                onClick = onTwitterClick,
            )
        }
    }

    if (showThemeDialog) {
        ThemePickerDialog(
            title = stringResource(R.string.title_choose_theme),
            closeLabel = stringResource(R.string.button_dismiss),
            options = themeOptions,
            selectedValue = selectedThemeValue,
            onDismiss = { showThemeDialog = false },
            onSelect = { value ->
                showThemeDialog = false
                onThemeSelected(value)
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsComposeScreenPreview() {
    SettingsComposeScreen(
        appVersion = "1.5.2",
        selectedThemeValue = Constants.THEME_DEFAULT,
        themeOptions = listOf(
            ThemeOption("Light", Constants.THEME_LIGHT),
            ThemeOption("Dark", Constants.THEME_DARK),
            ThemeOption("Default", Constants.THEME_DEFAULT),
        ),
        onThemeSelected = {},
        onAppVersionClick = {},
        onSourceCodeClick = {},
        onPrivacyClick = {},
        onDataDeletionClick = {},
        onRateClick = {},
        onTwitterClick = {},
    )
}
