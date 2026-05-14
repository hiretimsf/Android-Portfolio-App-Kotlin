package hiretimsf.com.app.navigation

import android.net.Uri

sealed class AppRoute(val route: String) {
    data object Welcome : AppRoute("welcome")
    data object Profile : AppRoute("profile")
    data object Portfolio : AppRoute("portfolio")
    data object Experience : AppRoute("experience")
    data object Settings : AppRoute("settings")
    data object AppInfo : AppRoute("app-info")

    data object PortfolioDetail : AppRoute("portfolio/{id}?title={title}") {
        fun create(id: String, title: String): String {
            return "portfolio/${Uri.encode(id)}?title=${Uri.encode(title)}"
        }
    }

    data object ExperienceDetail : AppRoute("experience/{id}?company={company}") {
        fun create(id: String, company: String): String {
            return "experience/${Uri.encode(id)}?company=${Uri.encode(company)}"
        }
    }
}

val topLevelRoutes = setOf(
    AppRoute.Profile.route,
    AppRoute.Portfolio.route,
    AppRoute.Experience.route,
    AppRoute.Settings.route,
)
