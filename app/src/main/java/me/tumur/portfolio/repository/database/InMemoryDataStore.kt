package me.tumur.portfolio.repository.database

import kotlinx.coroutines.flow.MutableStateFlow
import me.tumur.portfolio.repository.database.model.LocationModel
import me.tumur.portfolio.repository.database.model.button.ButtonModel
import me.tumur.portfolio.repository.database.model.category.CategoryModel
import me.tumur.portfolio.repository.database.model.experience.ExperienceModel
import me.tumur.portfolio.repository.database.model.favorite.FavoriteModel
import me.tumur.portfolio.repository.database.model.portfolio.PortfolioModel
import me.tumur.portfolio.repository.database.model.profile.AboutModel
import me.tumur.portfolio.repository.database.model.profile.ProfileModel
import me.tumur.portfolio.repository.database.model.profile.SocialModel
import me.tumur.portfolio.repository.database.model.resource.ResourceModel
import me.tumur.portfolio.repository.database.model.screenshot.ScreenShotModel
import me.tumur.portfolio.repository.database.model.settings.AppModel
import me.tumur.portfolio.repository.database.model.task.TaskModel
import me.tumur.portfolio.repository.database.model.welcome.WelcomeModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InMemoryDataStore @Inject constructor() {
    private val all = LocalPortfolioStrings.all

    val welcome = MutableStateFlow(all.welcome.sortedBy { it.order })
    val profiles = MutableStateFlow(all.profile.sortedBy { it.order })
    val socials = MutableStateFlow(all.social.sortedBy { it.order })
    val about = MutableStateFlow(all.about.sortedBy { it.order })
    val app = MutableStateFlow(all.app.sortedBy { it.order })
    val portfolio = MutableStateFlow(all.portfolio.sortedBy { it.order })
    val experiences = MutableStateFlow(all.experience.sortedBy { it.order })
    val buttons = MutableStateFlow(all.button.sortedBy { it.order })
    val tasks = MutableStateFlow(all.task.sortedBy { it.order })
    val categories = MutableStateFlow(all.category.sortedBy { it.order })
    val screenshots = MutableStateFlow(all.screenshot.sortedBy { it.order })
    val locations = MutableStateFlow(all.location)
    val resources = MutableStateFlow(all.resource.sortedBy { it.order })
    val favorites = MutableStateFlow<List<FavoriteModel>>(emptyList())
}
