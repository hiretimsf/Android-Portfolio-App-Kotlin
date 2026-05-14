package hiretimsf.com.app.screens.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.intuit.sdp.R as SdpR
import hiretimsf.com.app.R

@Composable
fun ProfileContactButton(
    visible: Boolean,
    onClick: () -> Unit,
) {
    val description = stringResource(R.string.cd_more_profile_options)
    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(),
        exit = scaleOut(),
    ) {
        FloatingActionButton(
            onClick = onClick,
            containerColor = colorResource(R.color.colorPrimary),
            contentColor = colorResource(R.color.colorOnPrimary),
            modifier = Modifier.semantics {
                contentDescription = description
            },
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_read_more),
                contentDescription = null,
                modifier = Modifier.size(dimensionResource(SdpR.dimen._16sdp)),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileContactButtonPreview() {
    ProfileContactButton(
        visible = true,
        onClick = {},
    )
}
