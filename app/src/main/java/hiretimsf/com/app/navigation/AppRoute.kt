package hiretimsf.com.app.navigation

import android.net.Uri

sealed class AppRoute(val route: String) {
    data object Welcome : AppRoute("welcome")
    data object Profile : AppRoute("profile")
    data object Portfolio : AppRoute("portfolio")
    data object Blog : AppRoute("blog")
    data object Settings : AppRoute("settings")
    data object Contact : AppRoute("contact")
    data object AppInfo : AppRoute("app-info")

    data object PortfolioDetail : AppRoute("portfolio/{id}?title={title}") {
        fun create(id: String, title: String): String {
            return "portfolio/${Uri.encode(id)}?title=${Uri.encode(title)}"
        }
    }

    data object BlogDetail : AppRoute("blog/{id}?title={title}") {
        fun create(id: String, title: String): String {
            return "blog/${Uri.encode(id)}?title=${Uri.encode(title)}"
        }
    }
}

val topLevelRoutes = setOf(
    AppRoute.Profile.route,
    AppRoute.Portfolio.route,
    AppRoute.Blog.route,
    AppRoute.Settings.route,
    AppRoute.Contact.route,
)
