package hiretimsf.com.app.screens.contact

import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
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
import hiretimsf.com.app.R
import hiretimsf.com.app.repository.database.model.profile.ProfileModel
import hiretimsf.com.app.repository.database.model.profile.SocialModel
import hiretimsf.com.app.utils.constants.BsConstants

private val contactFontFamily = FontFamily(Font(R.font.questrial))

@Composable
fun ContactComposeScreen(
    profile: ProfileModel?,
    socialItems: List<SocialModel>,
    onEmailClick: () -> Unit,
    onSocialClick: (SocialModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.colorSurface))
            .padding(horizontal = 20.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ContactAvatar(profile = profile)
        Text(
            text = profile?.name ?: stringResource(R.string.name),
            color = colorResource(R.color.colorOnPrimarySurface),
            fontFamily = contactFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp),
        )
        Text(
            text = profile?.title ?: stringResource(R.string.title),
            color = colorResource(R.color.colorOnSurface),
            fontFamily = contactFontFamily,
            fontSize = 18.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
        )

        Spacer(Modifier.height(28.dp))
        ContactSection(title = stringResource(R.string.contact_email_label)) {
            ContactRow(
                icon = R.drawable.ic_email,
                title = profile?.email ?: stringResource(R.string.contact_email),
                onClick = onEmailClick,
            )
        }

        Spacer(Modifier.height(18.dp))
        ContactSection(title = stringResource(R.string.contact_social_label)) {
            socialItems.forEachIndexed { index, item ->
                ContactRow(
                    icon = item.name.socialIcon(),
                    title = item.name,
                    onClick = { onSocialClick(item) },
                )
                if (index < socialItems.lastIndex) {
                    HorizontalDivider(color = colorResource(R.color.colorBorder))
                }
            }
        }
    }
}

@Composable
private fun ContactAvatar(profile: ProfileModel?) {
    AndroidView(
        modifier = Modifier
            .size(132.dp)
            .clip(CircleShape)
            .border(2.dp, colorResource(R.color.colorPrimary), CircleShape),
        factory = { context ->
            ImageView(context).apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
            }
        },
        update = { imageView ->
            imageView.contentDescription = profile?.imageDescription ?: imageView.context.getString(R.string.cd_avatar)
            val image = profile?.image
            if (image.isNullOrBlank()) {
                imageView.setImageResource(R.drawable.profile)
            } else {
                imageView.load(image, imageLoader = imageView.context.imageLoader) {
                    placeholder(R.drawable.profile)
                    error(R.drawable.profile)
                }
            }
        },
    )
}

@Composable
private fun ContactSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            color = colorResource(R.color.colorOnPrimarySurface),
            fontFamily = contactFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(colorResource(R.color.colorHeaderBackground), RoundedCornerShape(8.dp))
        ) {
            content()
        }
    }
}

@Composable
private fun ContactRow(
    icon: Int,
    title: String,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(colorResource(R.color.colorSurface), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(icon),
                    contentDescription = title,
                    colorFilter = ColorFilter.tint(colorResource(R.color.colorHeaderTitle)),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(20.dp),
                )
            }
            Text(
                text = title,
                color = colorResource(R.color.colorOnPrimarySurface),
                fontFamily = contactFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

private fun String.socialIcon(): Int {
    return when (this) {
        BsConstants.GITHUB, BsConstants.GITHUB_DISPLAY -> R.drawable.ic_github
        BsConstants.LINKEDIN -> R.drawable.ic_linkedin
        BsConstants.TWITTER, BsConstants.X_TWITTER -> R.drawable.ic_twitter
        else -> R.drawable.ic_globe
    }
}
