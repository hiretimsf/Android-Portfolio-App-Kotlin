package hiretimsf.com.app.screens.profile.bottomsheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.intuit.sdp.R as SdpR
import hiretimsf.com.app.R
import hiretimsf.com.app.repository.database.model.profile.ProfileModel
import hiretimsf.com.app.repository.database.model.profile.SocialModel
import hiretimsf.com.app.utils.constants.BsConstants
import hiretimsf.com.app.utils.constants.DbConstants

@Composable
fun ProfileBottomSheetContent(
    profile: ProfileModel?,
    socialItems: List<SocialModel>,
    onSocialClick: (SocialModel) -> Unit,
    onContactClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(R.color.colorSurface)),
    ) {
        BottomSheetIndicator()
        ProfileSummary(profile = profile)
        BottomSheetDivider(top = SdpR.dimen._10sdp, bottom = SdpR.dimen._5sdp)
        socialItems.forEach { item ->
            SocialRow(
                item = item,
                onClick = { onSocialClick(item) },
            )
        }
        ContactRow(onClick = onContactClick)
    }
}

@Composable
private fun BottomSheetIndicator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = dimensionResource(SdpR.dimen._10sdp),
                bottom = dimensionResource(SdpR.dimen._10sdp),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Spacer(
            modifier = Modifier
                .width(dimensionResource(SdpR.dimen._30sdp))
                .height(dimensionResource(SdpR.dimen._3sdp))
                .clip(RoundedCornerShape(dimensionResource(SdpR.dimen._3sdp)))
                .background(colorResource(R.color.colorPageIndicatorBackground)),
        )
    }
}

@Composable
private fun ProfileSummary(profile: ProfileModel?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = dimensionResource(SdpR.dimen._15sdp)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(R.drawable.profile),
            contentDescription = profile?.imageDescription,
            modifier = Modifier
                .size(dimensionResource(SdpR.dimen._30sdp))
                .clip(CircleShape)
                .border(
                    width = dimensionResource(SdpR.dimen._1sdp),
                    color = colorResource(R.color.colorOnPrimary),
                    shape = CircleShape,
                ),
        )

        Column(
            modifier = Modifier
                .padding(start = dimensionResource(SdpR.dimen._10sdp))
                .weight(1f),
        ) {
            Text(
                text = stringResource(R.string.name),
                color = colorResource(R.color.colorOnPrimarySurface),
                fontFamily = bottomSheetFontFamily(),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = stringResource(R.string.contact_email),
                color = colorResource(R.color.colorOnSurface),
                fontFamily = bottomSheetFontFamily(),
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun SocialRow(
    item: SocialModel,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = dimensionResource(SdpR.dimen._5sdp),
                bottom = dimensionResource(SdpR.dimen._5sdp),
            )
            .clickable(onClick = onClick)
            .padding(dimensionResource(SdpR.dimen._5sdp)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(item.name.socialIcon()),
            contentDescription = item.name,
            colorFilter = ColorFilter.tint(colorResource(R.color.colorHeaderTitle)),
            modifier = Modifier
                .padding(start = dimensionResource(SdpR.dimen._20sdp))
                .size(dimensionResource(SdpR.dimen._15sdp)),
        )
        Text(
            text = item.name,
            color = colorResource(R.color.colorHeaderTitle),
            fontFamily = bottomSheetFontFamily(),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(
                    start = dimensionResource(SdpR.dimen._20sdp),
                    end = dimensionResource(SdpR.dimen._5sdp),
                )
                .weight(1f),
        )
    }
}

@Composable
private fun ContactRow(onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        BottomSheetDivider(top = R.dimen.dimen_0dp, bottom = SdpR.dimen._10sdp)
        Text(
            text = stringResource(R.string.copy_right),
            color = colorResource(R.color.colorOnSurface),
            fontFamily = bottomSheetFontFamily(),
            fontSize = 14.sp,
            lineHeight = 19.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(SdpR.dimen._10sdp)),
        )
    }
}

@Composable
private fun BottomSheetDivider(
    top: Int,
    bottom: Int,
) {
    HorizontalDivider(
        color = colorResource(R.color.colorBorder),
        thickness = dimensionResource(SdpR.dimen._1sdp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = dimensionResource(top),
                bottom = dimensionResource(bottom),
            ),
    )
}

@Composable
private fun bottomSheetFontFamily(): FontFamily = FontFamily(Font(R.font.questrial))

private fun String.socialIcon(): Int {
    return when (this) {
        BsConstants.GITHUB -> R.drawable.ic_github
        BsConstants.LINKEDIN -> R.drawable.ic_linkedin
        BsConstants.TWITTER -> R.drawable.ic_twitter
        BsConstants.PDF -> R.drawable.ic_pdf
        else -> R.drawable.ic_globe
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileBottomSheetContentPreview() {
    ProfileBottomSheetContent(
        profile = ProfileModel(
            id = DbConstants.PERSON_ID,
            greeting = "Hello",
            name = "Tim Baz",
            title = "Design Engineer",
            image = "",
            imageDescription = "Profile picture",
            email = "hiretimsf@gmail.com",
            order = 1,
        ),
        socialItems = listOf(
            SocialModel(
                id = "github",
                ownerId = DbConstants.PERSON_ID,
                name = BsConstants.GITHUB,
                url = "https://github.com",
                order = 1,
            ),
            SocialModel(
                id = "linkedin",
                ownerId = DbConstants.PERSON_ID,
                name = BsConstants.LINKEDIN,
                url = "https://linkedin.com",
                order = 2,
            ),
        ),
        onSocialClick = {},
        onContactClick = {},
    )
}
