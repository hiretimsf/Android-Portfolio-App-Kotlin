package hiretimsf.com.app.screens.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.intuit.sdp.R as SdpR
import hiretimsf.com.app.R
import hiretimsf.com.app.screens.shared.components.primaryActionBrush
import hiretimsf.com.app.screens.shared.components.primaryActionContentColor

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
        Box(
            modifier = Modifier
                .size(56.dp)
                .shadow(6.dp, CircleShape)
                .clip(CircleShape)
                .background(primaryActionBrush())
                .clickable(
                    role = Role.Button,
                    onClick = onClick,
                )
                .semantics {
                    contentDescription = description
                },
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_read_more),
                contentDescription = null,
                tint = primaryActionContentColor(),
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
