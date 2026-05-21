package hiretimsf.com.app.screens.settings

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hiretimsf.com.app.R
import hiretimsf.com.app.screens.settings.components.settingsFontFamily
import hiretimsf.com.app.screens.shared.components.PrimaryGradientButton
import hiretimsf.com.app.utils.privacy.DataDeletionIdentifier

@Composable
fun DataDeletionComposeScreen(
    deletionId: String,
    onEmailClick: () -> Unit,
    onInstructionsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.colorSurface))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = stringResource(R.string.data_deletion_title),
                color = colorResource(R.color.colorOnPrimarySurface),
                fontFamily = settingsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
            )
            Text(
                text = stringResource(R.string.data_deletion_intro),
                color = colorResource(R.color.colorOnSurface),
                fontFamily = settingsFontFamily,
                fontSize = 16.sp,
                lineHeight = 23.sp,
            )
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = colorResource(R.color.colorHeaderBackground),
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = stringResource(R.string.data_deletion_identifier_label),
                    color = colorResource(R.color.colorOnPrimarySurface),
                    fontFamily = settingsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )
                SelectionContainer {
                    Text(
                        text = deletionId,
                        color = colorResource(R.color.colorOnPrimarySurface),
                        fontFamily = settingsFontFamily,
                        fontSize = 17.sp,
                        lineHeight = 24.sp,
                    )
                }
                Text(
                    text = stringResource(R.string.data_deletion_identifier_help),
                    color = colorResource(R.color.colorOnSurface),
                    fontFamily = settingsFontFamily,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                )
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            PrimaryGradientButton(
                onClick = onEmailClick,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = stringResource(R.string.data_deletion_email_button),
                    fontFamily = settingsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                )
            }

            OutlinedButton(
                onClick = onInstructionsClick,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = stringResource(R.string.data_deletion_open_instructions),
                    fontFamily = settingsFontFamily,
                    textAlign = TextAlign.Center,
                )
            }
            OutlinedButton(
                onClick = {
                    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboardManager.setPrimaryClip(
                        ClipData.newPlainText(
                            context.getString(R.string.data_deletion_identifier_label),
                            deletionId,
                        ),
                    )
                    Toast.makeText(
                        context,
                        context.getString(R.string.data_deletion_id_copied),
                        Toast.LENGTH_SHORT,
                    ).show()
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = stringResource(R.string.data_deletion_copy_id),
                    fontFamily = settingsFontFamily,
                    textAlign = TextAlign.Center,
                )
            }
        }

        Spacer(Modifier.height(8.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun DataDeletionComposeScreenPreview() {
    DataDeletionComposeScreen(
        deletionId = "htsf-00000000-0000-0000-0000-000000000000",
        onEmailClick = {},
        onInstructionsClick = {},
    )
}
