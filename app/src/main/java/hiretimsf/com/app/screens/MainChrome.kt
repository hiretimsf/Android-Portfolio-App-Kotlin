package hiretimsf.com.app.screens

import android.graphics.drawable.Animatable
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import hiretimsf.com.app.R
import hiretimsf.com.app.navigation.AppRoute

data class MainChromeState(
    val title: String = "",
    val selectedRoute: String? = null,
    val canNavigateBack: Boolean = false,
)

data class MainChromeDestination(
    val route: String,
    @param:StringRes val titleRes: Int,
    @param:DrawableRes val iconRes: Int,
    @param:DrawableRes val animatedIconRes: Int,
)

val bottomDestinations = listOf(
    MainChromeDestination(
        route = AppRoute.Profile.route,
        titleRes = R.string.menu_profile,
        iconRes = R.drawable.ic_menu_profile,
        animatedIconRes = R.drawable.ic_menu_profile_avd,
    ),
    MainChromeDestination(
        route = AppRoute.Portfolio.route,
        titleRes = R.string.menu_portfolio,
        iconRes = R.drawable.ic_menu_portfolio,
        animatedIconRes = R.drawable.ic_menu_portfolio_avd,
    ),
    MainChromeDestination(
        route = AppRoute.Blog.route,
        titleRes = R.string.menu_blog,
        iconRes = R.drawable.ic_menu_blog,
        animatedIconRes = R.drawable.ic_menu_blog_avd,
    ),
    MainChromeDestination(
        route = AppRoute.Settings.route,
        titleRes = R.string.menu_settings,
        iconRes = R.drawable.ic_menu_settings,
        animatedIconRes = R.drawable.ic_menu_settings_avd,
    ),
)

val drawerDestinations = bottomDestinations + MainChromeDestination(
    route = AppRoute.Contact.route,
    titleRes = R.string.menu_contact,
    iconRes = R.drawable.ic_email,
    animatedIconRes = R.drawable.ic_email,
)

private val questrial = FontFamily(Font(R.font.questrial))

@Composable
fun MainTopAppBar(
    state: MainChromeState,
    onNavigationClick: () -> Unit,
    onContactClick: () -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(dimensionResource(R.dimen.elevation_16dp))
            .background(colorResource(R.color.colorChromeBackground)),
    ) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clickable(onClick = onNavigationClick),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(if (state.canNavigateBack) R.drawable.ic_nav_back else R.drawable.ic_nav_menu),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                )
            }
            Text(
                text = state.title,
                color = colorResource(R.color.colorOnChrome),
                fontFamily = questrial,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 4.dp),
            )
            IconButton(
                onClick = onSearchClick,
                modifier = Modifier.size(56.dp),
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = stringResource(R.string.cd_search),
                    modifier = Modifier.size(24.dp),
                )
            }
            IconButton(
                onClick = onContactClick,
                modifier = Modifier.size(56.dp),
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_email_top),
                    contentDescription = stringResource(R.string.cd_contact),
                    modifier = Modifier.size(24.dp),
                )
            }
        }
    }
}

@Composable
fun MainBottomNavigationBar(
    selectedRoute: String?,
    onDestinationClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(dimensionResource(R.dimen.elevation_16dp)),
        color = colorResource(R.color.colorSurface),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                bottomDestinations.forEach { destination ->
                    val selected = selectedRoute == destination.route
                    NavigationBarItemContent(
                        destination = destination,
                        selected = selected,
                        onClick = { onDestinationClick(destination.route) },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable { onDestinationClick(destination.route) },
                    )
                }
            }
            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
        }
    }
}

@Composable
fun MainDrawerContent(
    selectedRoute: String?,
    onDestinationClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.colorSurface)),
    ) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
        DrawerHeader()
        Spacer(Modifier.height(12.dp))
        drawerDestinations.forEach { destination ->
            DrawerDestinationRow(
                destination = destination,
                selected = selectedRoute == destination.route,
                onClick = { onDestinationClick(destination.route) },
            )
        }
    }
}

@Composable
private fun DrawerHeader(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(176.dp)
            .background(colorResource(R.color.colorChromeBackground)),
    ) {
        Image(
            painter = painterResource(R.drawable.ic_header_bg),
            contentDescription = stringResource(R.string.cd_background),
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.12f),
            contentScale = ContentScale.Crop,
        )
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 22.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(R.drawable.profile),
                contentDescription = stringResource(R.string.cd_avatar),
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .border(2.dp, colorResource(R.color.colorOnChrome), CircleShape),
                contentScale = ContentScale.Crop,
            )
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.name),
                    color = colorResource(R.color.colorOnChrome),
                    fontFamily = questrial,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = stringResource(R.string.title),
                    color = colorResource(R.color.colorOnChrome),
                    fontFamily = questrial,
                    fontSize = 17.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 6.dp),
                )
            }
        }
    }
}

@Composable
private fun DrawerDestinationRow(
    destination: MainChromeDestination,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .height(58.dp)
            .background(
                color = if (selected) colorResource(R.color.colorSelectedItemBackground) else Color.Transparent,
                shape = RoundedCornerShape(8.dp),
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(30.dp)
                .background(
                    color = if (selected) colorResource(R.color.colorSelectedItemContent) else Color.Transparent,
                    shape = RoundedCornerShape(50),
                ),
        )
        Spacer(Modifier.width(14.dp))
        AnimatedDrawableIcon(
            iconRes = destination.iconRes,
            animatedIconRes = destination.animatedIconRes,
            selected = selected,
            onClick = onClick,
            modifier = Modifier.size(24.dp),
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = stringResource(destination.titleRes),
            color = if (selected) colorResource(R.color.colorSelectedItemContent) else colorResource(R.color.colorOnPrimarySurface),
            fontFamily = questrial,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun NavigationBarItemContent(
    destination: MainChromeDestination,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(vertical = 6.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AnimatedDrawableIcon(
            iconRes = destination.iconRes,
            animatedIconRes = destination.animatedIconRes,
            selected = selected,
            onClick = onClick,
            modifier = Modifier.size(24.dp),
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = stringResource(destination.titleRes),
            color = colorResource(if (selected) R.color.colorSelectedItemContent else R.color.colorOnSurface),
            fontFamily = questrial,
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun AnimatedDrawableIcon(
    @DrawableRes iconRes: Int,
    @DrawableRes animatedIconRes: Int,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    val context = LocalContext.current
    AndroidView(
        modifier = modifier,
        factory = {
            ImageView(it).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
                scaleType = ImageView.ScaleType.FIT_CENTER
                setOnClickListener { onClick?.invoke() }
                isClickable = onClick != null
            }
        },
        update = { imageView ->
            imageView.setOnClickListener { onClick?.invoke() }
            imageView.isClickable = onClick != null
            if (imageView.tag != selected) {
                val drawableRes = if (selected) animatedIconRes else iconRes
                val drawable = AppCompatResources.getDrawable(context, drawableRes)?.mutate()
                imageView.setImageDrawable(drawable)
                if (selected) {
                    (drawable as? Animatable)?.start()
                }
                imageView.tag = selected
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun MainTopAppBarPreview() {
    MainTopAppBar(
        state = MainChromeState(title = "Profile"),
        onNavigationClick = {},
        onContactClick = {},
        onSearchClick = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun MainBottomNavigationBarPreview() {
    MainBottomNavigationBar(
        selectedRoute = AppRoute.Profile.route,
        onDestinationClick = {},
    )
}

@Preview(showBackground = true, widthDp = 280, heightDp = 520)
@Composable
private fun MainDrawerContentPreview() {
    MainDrawerContent(
        selectedRoute = AppRoute.Profile.route,
        onDestinationClick = {},
    )
}
