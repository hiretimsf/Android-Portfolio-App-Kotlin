package me.tumur.portfolio.screens.welcome

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.view.Gravity
import android.widget.ImageView
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.flow.collectLatest
import me.tumur.portfolio.R
import me.tumur.portfolio.repository.database.model.welcome.WelcomeModel
import me.tumur.portfolio.utils.adapters.bindingAdapters.setPagerIcon
import com.intuit.sdp.R as SdpR

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WelcomeComposeScreen(
    viewModel: WelcomeViewModel,
    onFinished: () -> Unit,
) {
    val pages by viewModel.welcomeScreenFlow.collectAsStateWithLifecycle()
    val currentItem by viewModel.currentItemFlow.collectAsStateWithLifecycle()
    val scrollToItem by viewModel.scrollToItemFlow.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(pageCount = { pages.size })

    LaunchedEffect(pagerState, pages.size) {
        snapshotFlow { pagerState.currentPage }
            .collectLatest(viewModel::setCurrentItem)
    }

    LaunchedEffect(scrollToItem, pages.size) {
        val target = scrollToItem ?: return@LaunchedEffect
        if (pages.isNotEmpty()) {
            pagerState.animateScrollToPage(target.coerceIn(0, pages.lastIndex))
        }
    }

    WelcomeContent(
        pages = pages,
        currentItem = currentItem,
        onCurrentItemChanged = viewModel::setCurrentItem,
        onNextClick = {
            if (pages.isEmpty()) return@WelcomeContent

            if (currentItem == pages.lastIndex) {
                viewModel.setFirstRunAs(false)
                onFinished()
            } else {
                viewModel.setScrollToItem(currentItem + 1)
            }
        },
        pager = { modifier ->
            HorizontalPager(
                state = pagerState,
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) { page ->
                pages.getOrNull(page)?.let { model ->
                    WelcomePage(
                        model = model,
                        currentItem = currentItem,
                        page = page,
                    )
                }
            }
        },
    )
}

@Composable
private fun WelcomeContent(
    pages: List<WelcomeModel>,
    currentItem: Int,
    onCurrentItemChanged: (Int) -> Unit,
    onNextClick: () -> Unit,
    pager: @Composable (Modifier) -> Unit,
) {
    val buttonText = stringResource(
        if (pages.isNotEmpty() && currentItem == pages.lastIndex) {
            R.string.button_finish
        } else {
            R.string.button_next
        },
    )

    LaunchedEffect(currentItem) {
        onCurrentItemChanged(currentItem)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        pager(
            Modifier
                .fillMaxWidth()
                .weight(4f),
        )

        PageIndicator(
            count = pages.size,
            selected = currentItem,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(SdpR.dimen._10sdp)),
        )

        WelcomeButton(
            text = buttonText,
            onClick = onNextClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = dimensionResource(SdpR.dimen._20sdp),
                    top = dimensionResource(SdpR.dimen._10sdp),
                    end = dimensionResource(SdpR.dimen._20sdp),
                    bottom = dimensionResource(SdpR.dimen._10sdp),
                ),
        )
    }
}

@Composable
private fun WelcomePage(
    model: WelcomeModel,
    currentItem: Int,
    page: Int,
) {
    val fontFamily = portfolioFontFamily()
    val titleStyle = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
        color = colorResource(R.color.colorOnPrimarySurface),
        textAlign = TextAlign.Center,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(SdpR.dimen._40sdp)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = model.title.uppercase(),
            style = titleStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(bottom = dimensionResource(SdpR.dimen._5sdp)),
        )

        WelcomeIcon(
            model = model,
            currentItem = currentItem,
            page = page,
            modifier = Modifier.size(dimensionResource(SdpR.dimen._160sdp)),
        )

        Text(
            text = model.subTitle.uppercase(),
            style = titleStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth(),
        )

        Text(
            text = model.text,
            color = colorResource(R.color.colorOnSurface),
            fontFamily = fontFamily,
            fontSize = 18.sp,
            lineHeight = 23.sp,
            textAlign = TextAlign.Center,
            maxLines = 10,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(SdpR.dimen._10sdp)),
        )
    }
}

@Composable
private fun WelcomeIcon(
    model: WelcomeModel,
    currentItem: Int,
    page: Int,
    modifier: Modifier = Modifier,
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            ImageView(context).apply {
                scaleType = ImageView.ScaleType.FIT_CENTER
                contentDescription = model.imageDescription
            }
        },
        update = { imageView ->
            imageView.contentDescription = model.imageDescription
            setPagerIcon(imageView, model.order, currentItem, page)
        },
    )
}

@Composable
private fun PageIndicator(
    count: Int,
    selected: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(count) { index ->
            Box(
                modifier = Modifier
                    .padding(horizontal = dimensionResource(SdpR.dimen._4sdp))
                    .size(dimensionResource(SdpR.dimen._8sdp))
                    .background(
                        color = colorResource(
                            if (index == selected) {
                                R.color.colorPageIndicatorActive
                            } else {
                                R.color.colorPageIndicatorBackground
                            },
                        ),
                        shape = CircleShape,
                    ),
            )
        }
    }
}

@Composable
private fun WelcomeButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val buttonHeightPadding = dimensionResource(SdpR.dimen._10sdp)
    val buttonCornerRadius = dimensionResource(SdpR.dimen._4sdp)
    val density = LocalDensity.current
    val buttonHeightPaddingPx = with(density) { buttonHeightPadding.roundToPx() }
    val buttonCornerRadiusPx = with(density) { buttonCornerRadius.roundToPx() }
    val primaryColor = androidx.core.content.ContextCompat.getColor(context, R.color.colorPrimary)
    val onPrimaryColor = androidx.core.content.ContextCompat.getColor(context, R.color.colorOnPrimary)

    AndroidView(
        modifier = modifier,
        factory = {
            MaterialButton(context, null, com.google.android.material.R.attr.materialButtonStyle).apply {
                gravity = Gravity.CENTER
                isAllCaps = true
                textAlignment = android.view.View.TEXT_ALIGNMENT_CENTER
                setTextAppearance(R.style.TextAppearance_Headline)
                textSize = 22f
                setTextColor(onPrimaryColor)
                backgroundTintList = ColorStateList.valueOf(primaryColor)
                cornerRadius = buttonCornerRadiusPx
                setPadding(paddingLeft, buttonHeightPaddingPx, paddingRight, buttonHeightPaddingPx)
                setOnClickListener { onClick() }
            }
        },
        update = { button ->
            button.text = text
            button.setOnClickListener { onClick() }
        },
    )
}

@Composable
private fun portfolioFontFamily(): FontFamily {
    return remember {
        FontFamily(Font(R.font.questrial))
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun WelcomeComposeScreenPreview() {
    val pages = listOf(
        WelcomeModel(
            id = "1",
            title = "Hello",
            subTitle = "I'm Tim,",
            text = "born and raised in Mongolia, studied CS in Germany, and moved to the US three and a half years ago.",
            imageDescription = "Man waves a hand",
            order = 1,
        ),
        WelcomeModel(
            id = "2",
            title = "Background",
            subTitle = "Android Developer,",
            text = "specializing in user interfaces, and published two apps on the Google Play Store.",
            imageDescription = "Man texts on the phone",
            order = 2,
        ),
    )

    WelcomeContent(
        pages = pages,
        currentItem = 0,
        onCurrentItemChanged = {},
        onNextClick = {},
        pager = { modifier ->
            Box(modifier = modifier.fillMaxHeight()) {
                WelcomePage(
                    model = pages.first(),
                    currentItem = 0,
                    page = 0,
                )
            }
        },
    )
}
