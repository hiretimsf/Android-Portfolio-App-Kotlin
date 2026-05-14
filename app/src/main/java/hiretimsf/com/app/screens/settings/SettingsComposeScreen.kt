package hiretimsf.com.app.screens.settings

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hiretimsf.com.app.R
import hiretimsf.com.app.utils.constants.Constants

private val questrial = FontFamily(Font(R.font.questrial))

data class ThemeOption(
    val label: String,
    val value: String,
)

@Composable
fun SettingsComposeScreen(
    appVersion: String,
    selectedThemeValue: String,
    themeOptions: List<ThemeOption>,
    onThemeSelected: (String) -> Unit,
    onAppVersionClick: () -> Unit,
    onSourceCodeClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onRateClick: () -> Unit,
    onTwitterClick: () -> Unit,
    onEmailClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showThemeDialog by remember { mutableStateOf(false) }
    val selectedThemeLabel = themeOptions.firstOrNull { it.value == selectedThemeValue }?.label
        ?: themeOptions.firstOrNull { it.value == Constants.THEME_DEFAULT }?.label.orEmpty()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.colorSurface))
            .verticalScroll(rememberScrollState()),
    ) {
        SettingsCategoryTitle(text = "Display Options")
        SettingsPreferenceItem(
            icon = R.drawable.ic_theme,
            title = "Choose theme",
            summary = selectedThemeLabel,
            onClick = { showThemeDialog = true },
        )

        SettingsCategoryTitle(text = "App Info")
        SettingsPreferenceItem(
            icon = R.drawable.ic_app_version,
            title = "App Version",
            summary = appVersion,
            onClick = onAppVersionClick,
        )
        SettingsPreferenceItem(
            icon = R.drawable.ic_github,
            title = "Source code",
            summary = "Licensed under the Apache License",
            onClick = onSourceCodeClick,
        )
        SettingsPreferenceItem(
            icon = R.drawable.ic_privacy,
            title = "Protecting Your Privacy",
            summary = "External link",
            onClick = onPrivacyClick,
        )
        SettingsPreferenceItem(
            icon = R.drawable.ic_rate,
            title = "Rate This App",
            summary = "External link",
            showDivider = false,
            onClick = onRateClick,
        )

        SettingsCategoryTitle(text = "HireTimSF is Made By")
        SettingsPreferenceItem(
            icon = R.drawable.profile,
            title = "tumur_alex",
            summary = "You can find me on Twitter",
            iconSize = 25.dp,
            iconTint = null,
            showDivider = false,
            onClick = onTwitterClick,
        )

        SettingsFooterTitle(text = "Thanks for visiting HireTimSF")
        SettingsEmailItem(
            title = "You can get in touch with me",
            summary = "✉️ hiretimsf@gmail.com",
            onClick = onEmailClick,
        )
    }

    if (showThemeDialog) {
        ThemePickerDialog(
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

@Composable
private fun SettingsCategoryTitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text.uppercase(),
        color = colorResource(R.color.colorOnPrimarySurface),
        fontFamily = questrial,
        fontSize = 11.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
            .fillMaxWidth()
            .background(colorResource(R.color.colorHeaderBackground))
            .padding(start = 20.dp, top = 15.dp, bottom = 5.dp),
    )
}

@Composable
private fun SettingsPreferenceItem(
    @DrawableRes icon: Int,
    title: String,
    summary: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showDivider: Boolean = true,
    iconSize: androidx.compose.ui.unit.Dp = 15.dp,
    iconTint: androidx.compose.ui.graphics.Color? = colorResource(R.color.colorHeaderTitle),
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 16.dp, top = 5.dp, bottom = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (iconTint == null) {
                Image(
                    painter = painterResource(icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(iconSize)
                        .clip(CircleShape),
                )
            } else {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(iconSize),
                )
            }
            Spacer(Modifier.size(30.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = title,
                    color = colorResource(R.color.colorOnPrimarySurface),
                    fontFamily = questrial,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = summary,
                    color = colorResource(R.color.colorHeaderTitle),
                    fontFamily = questrial,
                    fontSize = 10.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        if (showDivider) {
            HorizontalDivider(
                color = colorResource(R.color.colorHeaderBackground),
                thickness = 1.dp,
            )
        }
    }
}

@Composable
private fun SettingsFooterTitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        color = colorResource(R.color.colorOnPrimarySurface),
        fontFamily = questrial,
        fontSize = 12.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
            .background(colorResource(R.color.colorHeaderBackground))
            .padding(vertical = 10.dp),
    )
}

@Composable
private fun SettingsEmailItem(
    title: String,
    summary: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title,
            color = colorResource(R.color.colorOnPrimarySurface),
            fontFamily = questrial,
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 5.dp),
        )
        Text(
            text = summary,
            color = colorResource(R.color.colorHeaderTitle),
            fontFamily = questrial,
            fontSize = 10.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 2.dp, bottom = 5.dp),
        )
        HorizontalDivider(
            color = colorResource(R.color.colorHeaderBackground),
            thickness = 1.dp,
        )
    }
}

@Composable
private fun ThemePickerDialog(
    options: List<ThemeOption>,
    selectedValue: String,
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Choose theme",
                fontFamily = questrial,
                fontWeight = FontWeight.Bold,
            )
        },
        text = {
            Column {
                options.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(option.value) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            selected = selectedValue == option.value,
                            onClick = { onSelect(option.value) },
                        )
                        Text(
                            text = option.label,
                            fontFamily = questrial,
                            modifier = Modifier.padding(start = 8.dp),
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Close", fontFamily = questrial)
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun SettingsComposeScreenPreview() {
    SettingsComposeScreen(
        appVersion = "1.0.0-beta01",
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
        onRateClick = {},
        onTwitterClick = {},
        onEmailClick = {},
    )
}
