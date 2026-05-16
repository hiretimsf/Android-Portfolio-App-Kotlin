package hiretimsf.com.app

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.core.content.edit
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import hiretimsf.com.app.navigation.AppRoute
import hiretimsf.com.app.navigation.topLevelRoutes
import hiretimsf.com.app.screens.MainBottomNavigationBar
import hiretimsf.com.app.screens.MainChromeState
import hiretimsf.com.app.screens.MainDrawerContent
import hiretimsf.com.app.screens.MainTopAppBar
import hiretimsf.com.app.screens.MainViewModel
import hiretimsf.com.app.screens.GlobalSearchResult
import hiretimsf.com.app.screens.GlobalSearchResultType
import hiretimsf.com.app.screens.blog.BlogComposeScreen
import hiretimsf.com.app.screens.blog.BlogViewModel
import hiretimsf.com.app.screens.blog.detail.BlogDetailComposeScreen
import hiretimsf.com.app.screens.blog.detail.BlogDetailViewModel
import hiretimsf.com.app.screens.contact.ContactComposeScreen
import hiretimsf.com.app.screens.portfolio.PortfolioComposeScreen
import hiretimsf.com.app.screens.portfolio.PortfolioViewModel
import hiretimsf.com.app.screens.portfolio.detail.PortfolioDetailComposeScreen
import hiretimsf.com.app.screens.portfolio.detail.PortfolioDetailFragmentViewModel
import hiretimsf.com.app.screens.profile.ProfileComposeScreen
import hiretimsf.com.app.screens.profile.ProfileViewModel
import hiretimsf.com.app.screens.search.GlobalSearchDialog
import hiretimsf.com.app.screens.settings.SettingsComposeScreen
import hiretimsf.com.app.screens.settings.components.ThemeOption
import hiretimsf.com.app.screens.welcome.WelcomeComposeScreen
import hiretimsf.com.app.screens.welcome.WelcomeViewModel
import hiretimsf.com.app.utils.constants.Constants
import hiretimsf.com.app.utils.extensions.launchCustomTab
import hiretimsf.com.app.utils.state.HideNavigation
import hiretimsf.com.app.utils.state.ShowNavigation
import hiretimsf.com.app.utils.state.SplashScreen
import hiretimsf.com.app.utils.state.WelcomeScreen
import hiretimsf.com.app.utils.theme.ThemeHelper
import kotlinx.coroutines.launch

@Composable
fun HireTimSfApp(
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var showSearchDialog by remember { mutableStateOf(false) }
    val screenState by mainViewModel.screenStateFlow.collectAsStateWithLifecycle()
    val navigationState by mainViewModel.navigationFlow.collectAsStateWithLifecycle()
    val savedFragmentState by mainViewModel.fragmentStateFlow.collectAsStateWithLifecycle()
    val searchState by mainViewModel.searchState.collectAsStateWithLifecycle()
    val startDestination = when (screenState) {
        is WelcomeScreen -> AppRoute.Welcome.route
        else -> AppRoute.Profile.route
    }

    if (screenState is SplashScreen) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(colorResource(R.color.colorSurface)),
        )
        return
    }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showChrome = navigationState !is HideNavigation && currentRoute != AppRoute.Welcome.route
    val chromeState = remember(currentRoute, backStackEntry?.arguments) {
        MainChromeState(
            title = resolveTitle(context, currentRoute, backStackEntry?.arguments),
            selectedRoute = selectedTopRoute(currentRoute),
            canNavigateBack = currentRoute !in topLevelRoutes && currentRoute != AppRoute.Welcome.route,
        )
    }

    LaunchedEffect(screenState, currentRoute) {
        if (screenState is WelcomeScreen && currentRoute != null && currentRoute != AppRoute.Welcome.route) {
            mainViewModel.setNavigationState(HideNavigation)
            navController.navigate(AppRoute.Welcome.route) {
                popUpTo(AppRoute.Profile.route) {
                    inclusive = false
                }
                launchSingleTop = true
            }
        }
    }

    LaunchedEffect(savedFragmentState, currentRoute) {
        if (currentRoute == null) return@LaunchedEffect
        val route = savedFragmentState?.toRoute() ?: return@LaunchedEffect
        navController.navigateTopLevel(route)
        mainViewModel.clearFragmentState()
    }

    LaunchedEffect(currentRoute) {
        currentRoute?.toSavedStateHolder()?.let(mainViewModel::setFragmentStateHolder)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = showChrome,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = colorResource(R.color.colorSurface),
            ) {
                MainDrawerContent(
                    selectedRoute = chromeState.selectedRoute,
                    onDestinationClick = { route ->
                        scope.launch { drawerState.close() }
                        navController.navigateTopLevel(route)
                    },
                )
            }
        },
        modifier = modifier,
    ) {
        Scaffold(
            containerColor = colorResource(R.color.colorSurface),
            topBar = {
                if (showChrome) {
                    MainTopAppBar(
                        state = chromeState,
                        onNavigationClick = {
                            if (chromeState.canNavigateBack) {
                                navController.navigateUp()
                            } else {
                                scope.launch { drawerState.open() }
                            }
                        },
                        onContactClick = { navController.navigateTopLevel(AppRoute.Contact.route) },
                        onSearchClick = {
                            mainViewModel.clearSearchQuery()
                            showSearchDialog = true
                        },
                    )
                }
            },
            bottomBar = {
                if (showChrome) {
                    MainBottomNavigationBar(
                        selectedRoute = chromeState.selectedRoute,
                        onDestinationClick = navController::navigateTopLevel,
                    )
                }
            },
        ) { innerPadding ->
            AppNavHost(
                navController = navController,
                mainViewModel = mainViewModel,
                startDestination = startDestination,
                contentPadding = innerPadding,
                onShowChrome = mainViewModel::finishWelcome,
            )
        }

        if (showChrome && showSearchDialog) {
            GlobalSearchDialog(
                state = searchState,
                onQueryChange = mainViewModel::setSearchQuery,
                onResultClick = { result ->
                    showSearchDialog = false
                    mainViewModel.clearSearchQuery()
                    navController.navigateSearchResult(result)
                },
                onDismiss = {
                    showSearchDialog = false
                    mainViewModel.clearSearchQuery()
                },
            )
        }
    }
}

@Composable
private fun AppNavHost(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    startDestination: String,
    contentPadding: PaddingValues,
    onShowChrome: () -> Unit,
) {
    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding),
    ) {
        composable(AppRoute.Welcome.route) {
            val viewModel: WelcomeViewModel = hiltViewModel()
            WelcomeComposeScreen(
                viewModel = viewModel,
                onFinished = {
                    onShowChrome()
                    navController.navigate(AppRoute.Profile.route) {
                        popUpTo(AppRoute.Welcome.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
            )
        }
        composable(AppRoute.Profile.route) {
            val viewModel: ProfileViewModel = hiltViewModel()
            val profile by viewModel.profileFlow.collectAsStateWithLifecycle()
            val aboutState by viewModel.aboutScreenFlow.collectAsStateWithLifecycle()
            val socialItems by viewModel.socialFlow.collectAsStateWithLifecycle()
            ProfileComposeScreen(
                profile = profile,
                aboutState = aboutState,
                socialItems = socialItems,
                onSocialClick = { context.launchCustomTab(it.url) },
                onEmailClick = { context.openEmail() },
            )
        }
        composable(AppRoute.Portfolio.route) {
            val viewModel: PortfolioViewModel = hiltViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()
            PortfolioComposeScreen(
                state = state,
                onRefresh = {
                    viewModel.setRefreshStatus(true)
                    viewModel.fetch()
                },
                onPortfolioClick = { item ->
                    navController.navigate(AppRoute.PortfolioDetail.create(item.id, item.title))
                },
            )
        }
        composable(AppRoute.Blog.route) {
            val viewModel: BlogViewModel = hiltViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()
            BlogComposeScreen(
                state = state,
                onRefresh = viewModel::fetchBlogPosts,
                onBlogPostClick = { post ->
                    navController.navigate(AppRoute.BlogDetail.create(post.slug, post.title))
                },
            )
        }
        composable(AppRoute.Settings.route) {
            SettingsRoute(navController = navController)
        }
        composable(AppRoute.Contact.route) {
            val viewModel: ProfileViewModel = hiltViewModel()
            val profile by viewModel.profileFlow.collectAsStateWithLifecycle()
            val socialItems by viewModel.socialFlow.collectAsStateWithLifecycle()
            ContactComposeScreen(
                profile = profile,
                socialItems = socialItems,
                onEmailClick = { context.openEmail() },
                onSocialClick = { context.launchCustomTab(it.url) },
            )
        }
        composable(
            route = AppRoute.PortfolioDetail.route,
            arguments = listOf(
                navArgument("id") { type = NavType.StringType },
                navArgument("title") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
            ),
        ) { entry ->
            val id = entry.arguments?.getString("id").orEmpty()
            val viewModel: PortfolioDetailFragmentViewModel = hiltViewModel()
            LaunchedEffect(id) {
                if (id.isNotBlank()) viewModel.setPortfolioId(id)
            }
            val portfolio by viewModel.portfolioFlow.collectAsStateWithLifecycle()
            val buttons by viewModel.buttons.collectAsStateWithLifecycle()
            val categories by viewModel.categories.collectAsStateWithLifecycle()
            val screenshots by viewModel.screenshots.collectAsStateWithLifecycle()
            PortfolioDetailComposeScreen(
                portfolio = portfolio,
                buttons = buttons,
                categories = categories,
                screenshots = screenshots,
                onButtonClick = context::launchCustomTab,
                onVideoClick = context::launchCustomTab,
                onScreenshotClick = {},
            )
        }
        composable(
            route = AppRoute.BlogDetail.route,
            arguments = listOf(
                navArgument("id") { type = NavType.StringType },
                navArgument("title") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
            ),
        ) { entry ->
            val id = entry.arguments?.getString("id").orEmpty()
            val viewModel: BlogDetailViewModel = hiltViewModel()
            LaunchedEffect(id) {
                viewModel.fetchBlogPost(id)
            }
            val state by viewModel.state.collectAsStateWithLifecycle()
            BlogDetailComposeScreen(
                state = state,
            )
        }
        dialog(AppRoute.AppInfo.route) {
            AppInfoDialog(
                appVersion = androidx.compose.ui.res.stringResource(R.string.summary_app_version),
                onDismiss = { navController.navigateUp() },
            )
        }
    }
}

@Composable
private fun SettingsRoute(navController: NavHostController) {
    val context = LocalContext.current
    val sharedPreferences = remember { context.getSharedPreferences(Constants.APP, Context.MODE_PRIVATE) }
    val themeKey = androidx.compose.ui.res.stringResource(R.string.preference_key_theme_option)
    val twitterUrl = androidx.compose.ui.res.stringResource(R.string.profile_social_x_url)
    val themeLabels = androidx.compose.ui.res.stringArrayResource(R.array.pref_theme_labels)
    val themeValues = androidx.compose.ui.res.stringArrayResource(R.array.pref_theme_values)
    var selectedTheme by remember {
        androidx.compose.runtime.mutableStateOf(
            sharedPreferences.getString(themeKey, Constants.THEME_DEFAULT) ?: Constants.THEME_DEFAULT,
        )
    }
    val themeOptions = remember(themeLabels, themeValues) {
        themeLabels
            .zip(themeValues)
            .map { (label, value) -> ThemeOption(label, value) }
    }
    SettingsComposeScreen(
        appVersion = androidx.compose.ui.res.stringResource(R.string.summary_app_version),
        selectedThemeValue = selectedTheme,
        themeOptions = themeOptions,
        onThemeSelected = { themeOption ->
            selectedTheme = themeOption
            sharedPreferences.edit {
                putString(themeKey, themeOption)
            }
            ThemeHelper.applyTheme(themeOption)
        },
        onAppVersionClick = { navController.navigate(AppRoute.AppInfo.route) },
        onSourceCodeClick = { context.launchCustomTab(Constants.SOURCE_CODE_URL) },
        onPrivacyClick = { context.launchCustomTab(Constants.PRIVACY_URL) },
        onRateClick = { context.openPlayStoreListing() },
        onTwitterClick = { context.launchCustomTab(twitterUrl) },
    )
}

@Composable
private fun AppInfoDialog(
    appVersion: String,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(androidx.compose.ui.res.stringResource(R.string.category_app_info)) },
        text = { Text("${androidx.compose.ui.res.stringResource(R.string.title_app_version)}: $appVersion") },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(androidx.compose.ui.res.stringResource(R.string.button_dismiss))
            }
        },
    )
}

private fun NavHostController.navigateTopLevel(route: String) {
    navigate(route) {
        popUpTo(AppRoute.Profile.route) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

private fun NavHostController.navigateSearchResult(result: GlobalSearchResult) {
    when (result.type) {
        GlobalSearchResultType.Project -> navigate(AppRoute.PortfolioDetail.create(result.id, result.title))
        GlobalSearchResultType.BlogPost -> navigate(AppRoute.BlogDetail.create(result.id, result.title))
        GlobalSearchResultType.About -> navigateTopLevel(AppRoute.Profile.route)
    }
}

private fun resolveTitle(context: Context, route: String?, arguments: android.os.Bundle?): String {
    return when (route) {
        AppRoute.Welcome.route -> ""
        AppRoute.Profile.route -> context.getString(R.string.menu_profile)
        AppRoute.Portfolio.route -> context.getString(R.string.menu_portfolio)
        AppRoute.Blog.route -> context.getString(R.string.menu_blog)
        AppRoute.Settings.route -> context.getString(R.string.menu_settings)
        AppRoute.Contact.route -> context.getString(R.string.menu_contact)
        AppRoute.PortfolioDetail.route -> context.getString(R.string.title_back)
        AppRoute.BlogDetail.route -> context.getString(R.string.title_back)
        else -> context.getString(R.string.app_name)
    }
}

private fun selectedTopRoute(route: String?): String? {
    return when (route) {
        AppRoute.PortfolioDetail.route -> AppRoute.Portfolio.route
        AppRoute.BlogDetail.route -> AppRoute.Blog.route
        in topLevelRoutes -> route
        else -> null
    }
}

private fun String.toRoute(): String? {
    return when (this) {
        Constants.FRAGMENT_PROFILE -> AppRoute.Profile.route
        Constants.FRAGMENT_PORTFOLIO -> AppRoute.Portfolio.route
        Constants.FRAGMENT_BLOG -> AppRoute.Blog.route
        Constants.FRAGMENT_SETTINGS -> AppRoute.Settings.route
        Constants.FRAGMENT_CONTACT -> AppRoute.Contact.route
        else -> null
    }
}

private fun String.toSavedStateHolder(): String? {
    return when (this) {
        AppRoute.Profile.route -> Constants.FRAGMENT_PROFILE
        AppRoute.Portfolio.route -> Constants.FRAGMENT_PORTFOLIO
        AppRoute.Blog.route -> Constants.FRAGMENT_BLOG
        AppRoute.Settings.route -> Constants.FRAGMENT_SETTINGS
        AppRoute.Contact.route -> Constants.FRAGMENT_CONTACT
        else -> null
    }
}

private fun Context.openEmail() {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Constants.MAILTO.toUri()
        putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.contact_email)))
        putExtra(Intent.EXTRA_SUBJECT, Constants.SUBJECT)
    }
    openIntent(intent)
}

private fun Context.openUri(uri: String) {
    openIntent(Intent(Intent.ACTION_VIEW, uri.toUri()))
}

private fun Context.openPlayStoreListing() {
    val packageName = packageName
    val openedInPlayStore = openIntentCatching(Intent(Intent.ACTION_VIEW, "market://details?id=$packageName".toUri()))
    if (!openedInPlayStore) {
        openUri("https://play.google.com/store/apps/details?id=$packageName")
    }
}

private fun Context.openIntent(intent: Intent) {
    openIntentCatching(intent)
}

private fun Context.openIntentCatching(intent: Intent): Boolean {
    try {
        startActivity(intent)
        return true
    } catch (_: ActivityNotFoundException) {
        return false
    } catch (_: RuntimeException) {
        return false
    }
}
