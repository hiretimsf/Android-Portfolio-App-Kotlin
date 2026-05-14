package hiretimsf.com.app.screens.profile

import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.imageLoader
import coil.load
import com.intuit.sdp.R as SdpR
import hiretimsf.com.app.R
import hiretimsf.com.app.repository.database.model.profile.ProfileModel
import hiretimsf.com.app.repository.database.model.profile.SocialModel
import hiretimsf.com.app.screens.profile.bottomsheet.ProfileBottomSheetContent

private val profileHeaderFontFamily = FontFamily(Font(R.font.questrial))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileComposeScreen(
    profile: ProfileModel?,
    aboutState: AboutScreenState,
    socialItems: List<SocialModel>,
    onSocialClick: (SocialModel) -> Unit,
    onEmailClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showContactSheet by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val showFab by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset < 40
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(bottom = 96.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.colorSurface)),
        ) {
            item {
                ProfileHeader(profile = profile)
            }
            item {
                ProfileAboutContent(
                    sections = aboutState.sections,
                    introductionImages = aboutState.introductionImages,
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = dimensionResource(SdpR.dimen._15sdp), bottom = dimensionResource(SdpR.dimen._20sdp)),
        ) {
            ProfileContactButton(
                visible = showFab,
                onClick = { showContactSheet = true },
            )
        }
    }

    if (showContactSheet) {
        ModalBottomSheet(onDismissRequest = { showContactSheet = false }) {
            ProfileBottomSheetContent(
                profile = profile,
                socialItems = socialItems,
                onSocialClick = {
                    showContactSheet = false
                    onSocialClick(it)
                },
                onContactClick = {
                    showContactSheet = false
                    onEmailClick()
                },
            )
        }
    }
}

@Composable
private fun ProfileHeader(
    profile: ProfileModel?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(SdpR.dimen._240sdp))
            .background(colorResource(R.color.colorPrimary)),
    ) {
        Image(
            painter = painterResource(R.drawable.ic_header_bg),
            contentDescription = stringResource(R.string.cd_background),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.1f),
        )
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = dimensionResource(SdpR.dimen._10sdp)),
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_hand_waving),
                    contentDescription = null,
                    modifier = Modifier.size(dimensionResource(SdpR.dimen._17sdp)),
                )
                Spacer(Modifier.width(dimensionResource(SdpR.dimen._2sdp)))
                androidx.compose.material3.Text(
                    text = profile?.greeting.orEmpty(),
                    color = colorResource(R.color.colorOnPrimary),
                    fontFamily = profileHeaderFontFamily,
                    fontSize = 22.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                )
            }
            ProfileAvatar(profile = profile, modifier = Modifier.size(dimensionResource(SdpR.dimen._80sdp)))
            androidx.compose.material3.Text(
                text = stringResource(R.string.name),
                color = colorResource(R.color.colorOnPrimary),
                fontFamily = profileHeaderFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = dimensionResource(SdpR.dimen._10sdp)),
            )
            androidx.compose.material3.Text(
                text = stringResource(R.string.title),
                color = colorResource(R.color.colorOnPrimary),
                fontFamily = profileHeaderFontFamily,
                fontSize = 18.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun ProfileAvatar(
    profile: ProfileModel?,
    modifier: Modifier = Modifier,
) {
    AndroidView(
        modifier = modifier
            .clip(CircleShape)
            .border(1.dp, colorResource(R.color.colorOnPrimary), CircleShape),
        factory = { context ->
            ImageView(context).apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
            }
        },
        update = { imageView ->
            imageView.contentDescription = profile?.imageDescription ?: imageView.context.getString(R.string.cd_avatar)
            imageView.load(profile?.image, imageLoader = imageView.context.imageLoader) {
                placeholder(R.drawable.profile)
                error(R.drawable.profile)
            }
        },
    )
}
