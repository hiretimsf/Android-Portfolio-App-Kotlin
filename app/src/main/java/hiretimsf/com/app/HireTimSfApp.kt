package hiretimsf.com.app

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.edit
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.paging.compose.collectAsLazyPagingItems
import hiretimsf.com.app.navigation.AppRoute
import hiretimsf.com.app.navigation.topLevelRoutes
import hiretimsf.com.app.screens.MainBottomNavigationBar
import hiretimsf.com.app.screens.MainChromeState
import hiretimsf.com.app.screens.MainDrawerContent
import hiretimsf.com.app.screens.MainTopAppBar
import hiretimsf.com.app.screens.MainViewModel
import hiretimsf.com.app.screens.experience.ExperienceComposeScreen
import hiretimsf.com.app.screens.experience.ExperienceViewModel
import hiretimsf.com.app.screens.experience.detail.ExperienceDetailComposeScreen
import hiretimsf.com.app.screens.experience.detail.ExperienceDetailFragmentViewModel
import hiretimsf.com.app.screens.favorite.FavoriteComposeScreen
import hiretimsf.com.app.screens.favorite.FavoriteViewModel
import hiretimsf.com.app.screens.portfolio.PortfolioComposeScreen
import hiretimsf.com.app.screens.portfolio.PortfolioViewModel
import hiretimsf.com.app.screens.portfolio.detail.PortfolioDetailComposeScreen
import hiretimsf.com.app.screens.portfolio.detail.PortfolioDetailFragmentViewModel
import hiretimsf.com.app.screens.profile.ProfileComposeScreen
import hiretimsf.com.app.screens.profile.ProfileViewModel
import hiretimsf.com.app.screens.settings.SettingsComposeScreen
import hiretimsf.com.app.screens.settings.ThemeOption
import hiretimsf.com.app.screens.welcome.WelcomeComposeScreen
import hiretimsf.com.app.screens.welcome.WelcomeViewModel
import hiretimsf.com.app.utils.constants.Constants
import hiretimsf.com.app.utils.extensions.launchCustomTab
import hiretimsf.com.app.utils.state.HideNavigation
import hiretimsf.com.app.utils.state.ShowNavigation
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
    val screenState by mainViewModel.screenStateFlow.collectAsStateWithLifecycle()
    val navigationState by mainViewModel.navigationFlow.collectAsStateWithLifecycle()
    val savedFragmentState by mainViewModel.fragmentStateFlow.collectAsStateWithLifecycle()
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

    LaunchedEffect(screenState) {
        if (screenState is WelcomeScreen) {
            mainViewModel.setNavigationState(HideNavigation)
            navController.navigate(AppRoute.Welcome.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    inclusive = false
                }
                launchSingleTop = true
            }
        }
    }

    LaunchedEffect(savedFragmentState) {
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
            ModalDrawerSheet {
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
                contentPadding = innerPadding,
                onShowChrome = { mainViewModel.setNavigationState(ShowNavigation) },
            )
        }
    }
}

@Composable
private fun AppNavHost(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    contentPadding: PaddingValues,
    onShowChrome: () -> Unit,
) {
    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = AppRoute.Profile.route,
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
            val isRefreshing by viewModel.isRefreshingFlow.collectAsStateWithLifecycle()
            PortfolioComposeScreen(
                items = viewModel.data.collectAsLazyPagingItems(),
                isRefreshing = isRefreshing,
                onRefresh = {
                    viewModel.setRefreshStatus(true)
                    viewModel.fetch()
                },
                onPortfolioClick = { item ->
                    navController.navigate(AppRoute.PortfolioDetail.create(item.id, item.title))
                },
            )
        }
        composable(AppRoute.Experience.route) {
            val viewModel: ExperienceViewModel = hiltViewModel()
            ExperienceComposeScreen(
                items = viewModel.data.collectAsLazyPagingItems(),
                onExperienceClick = { item ->
                    navController.navigate(AppRoute.ExperienceDetail.create(item.id, item.company))
                },
            )
        }
        composable(AppRoute.Settings.route) {
            SettingsRoute(navController = navController)
        }
        composable(AppRoute.Favorite.route) {
            val viewModel: FavoriteViewModel = hiltViewModel()
            val count by viewModel.table.collectAsStateWithLifecycle()
            FavoriteComposeScreen(
                items = viewModel.data.collectAsLazyPagingItems(),
                isEmpty = count == 0,
                onFavoriteClick = { item ->
                    navController.navigate(AppRoute.PortfolioDetail.create(item.id, item.title))
                },
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
            PortfolioDetailComposeScreen(
                portfolio = portfolio,
                buttons = viewModel.button.collectAsLazyPagingItems(),
                categories = viewModel.category.collectAsLazyPagingItems(),
                screenshots = viewModel.screenshot.collectAsLazyPagingItems(),
                onButtonClick = context::launchCustomTab,
                onVideoClick = context::launchCustomTab,
                onScreenshotClick = {},
            )
        }
        composable(
            route = AppRoute.ExperienceDetail.route,
            arguments = listOf(
                navArgument("id") { type = NavType.StringType },
                navArgument("company") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
            ),
        ) { entry ->
            val id = entry.arguments?.getString("id").orEmpty()
            val viewModel: ExperienceDetailFragmentViewModel = hiltViewModel()
            LaunchedEffect(id) {
                if (id.isNotBlank()) viewModel.setExperienceItemId(id)
            }
            val item by viewModel.dataFlow.collectAsStateWithLifecycle()
            ExperienceDetailComposeScreen(
                item = item,
                buttons = viewModel.button.collectAsLazyPagingItems(),
                tasks = viewModel.task.collectAsLazyPagingItems(),
                resources = viewModel.resource.collectAsLazyPagingItems(),
                onUrlClick = context::launchCustomTab,
            )
        }
        dialog(AppRoute.AppInfo.route) {
            AppInfoDialog(onDismiss = { navController.navigateUp() })
        }
    }
}

@Composable
private fun SettingsRoute(navController: NavHostController) {
    val context = LocalContext.current
    val sharedPreferences = remember { context.getSharedPreferences(Constants.APP, Context.MODE_PRIVATE) }
    val themeKey = androidx.compose.ui.res.stringResource(R.string.preference_key_theme_option)
    var selectedTheme by remember {
        androidx.compose.runtime.mutableStateOf(
            sharedPreferences.getString(themeKey, Constants.THEME_DEFAULT) ?: Constants.THEME_DEFAULT,
        )
    }
    val themeOptions = remember {
        context.resources.getStringArray(R.array.pref_theme_labels)
            .zip(context.resources.getStringArray(R.array.pref_theme_values))
            .map { (label, value) -> ThemeOption(label, value) }
    }
    SettingsComposeScreen(
        appVersion = context.packageManager.getPackageInfo(context.packageName, 0).versionName.orEmpty(),
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
        onRateClick = { context.openUri("market://details?id=${context.packageName}") },
        onTwitterClick = { context.launchCustomTab(Constants.TWITTER_URL) },
        onEmailClick = { context.openUri("${Constants.MAILTO}${Constants.EMAIL}?subject=${Constants.SUBJECT}") },
    )
}

@Composable
private fun AppInfoDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("App Info") },
        text = { Text("HireTimSF") },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        },
    )
}

private fun NavHostController.navigateTopLevel(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

private fun resolveTitle(context: Context, route: String?, arguments: android.os.Bundle?): String {
    return when (route) {
        AppRoute.Welcome.route -> ""
        AppRoute.Profile.route -> context.getString(R.string.menu_profile)
        AppRoute.Portfolio.route -> context.getString(R.string.menu_portfolio)
        AppRoute.Experience.route -> context.getString(R.string.menu_experience)
        AppRoute.Settings.route -> context.getString(R.string.menu_settings)
        AppRoute.Favorite.route -> context.getString(R.string.menu_favorite)
        AppRoute.PortfolioDetail.route -> arguments?.getString("title").orEmpty()
        AppRoute.ExperienceDetail.route -> arguments?.getString("company").orEmpty()
        else -> context.getString(R.string.app_name)
    }
}

private fun selectedTopRoute(route: String?): String? {
    return when (route) {
        AppRoute.PortfolioDetail.route -> AppRoute.Portfolio.route
        AppRoute.ExperienceDetail.route -> AppRoute.Experience.route
        in topLevelRoutes -> route
        else -> null
    }
}

private fun String.toRoute(): String? {
    return when (this) {
        Constants.FRAGMENT_PROFILE -> AppRoute.Profile.route
        Constants.FRAGMENT_PORTFOLIO -> AppRoute.Portfolio.route
        Constants.FRAGMENT_EXPERIENCE -> AppRoute.Experience.route
        Constants.FRAGMENT_SETTINGS -> AppRoute.Settings.route
        Constants.FRAGMENT_FAVORITE -> AppRoute.Favorite.route
        else -> null
    }
}

private fun String.toSavedStateHolder(): String? {
    return when (this) {
        AppRoute.Profile.route -> Constants.FRAGMENT_PROFILE
        AppRoute.Portfolio.route -> Constants.FRAGMENT_PORTFOLIO
        AppRoute.Experience.route -> Constants.FRAGMENT_EXPERIENCE
        AppRoute.Settings.route -> Constants.FRAGMENT_SETTINGS
        AppRoute.Favorite.route -> Constants.FRAGMENT_FAVORITE
        else -> null
    }
}

private fun Context.openEmail() {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Constants.MAILTO.toUri()
        putExtra(Intent.EXTRA_EMAIL, Constants.EMAIL)
        putExtra(Intent.EXTRA_SUBJECT, Constants.SUBJECT)
    }
    openIntent(intent)
}

private fun Context.openUri(uri: String) {
    openIntent(Intent(Intent.ACTION_VIEW, uri.toUri()))
}

private fun Context.openIntent(intent: Intent) {
    try {
        startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        Unit
    }
}
