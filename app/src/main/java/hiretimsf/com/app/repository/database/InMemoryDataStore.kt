package hiretimsf.com.app.repository.database

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import hiretimsf.com.app.repository.database.model.LocationModel
import hiretimsf.com.app.repository.database.model.button.ButtonModel
import hiretimsf.com.app.repository.database.model.category.CategoryModel
import hiretimsf.com.app.repository.database.model.experience.ExperienceModel
import hiretimsf.com.app.repository.database.model.portfolio.PortfolioModel
import hiretimsf.com.app.repository.database.model.profile.AboutModel
import hiretimsf.com.app.repository.database.model.profile.ProfileModel
import hiretimsf.com.app.repository.database.model.profile.SocialModel
import hiretimsf.com.app.repository.database.model.resource.ResourceModel
import hiretimsf.com.app.repository.database.model.screenshot.ScreenShotModel
import hiretimsf.com.app.repository.database.model.settings.AppModel
import hiretimsf.com.app.repository.database.model.task.TaskModel
import hiretimsf.com.app.repository.database.model.welcome.WelcomeModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InMemoryDataStore @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val all = LocalPortfolioStrings.all

    val welcome = MutableStateFlow(context.welcomeItems().sortedBy { it.order })
    val profiles = MutableStateFlow(context.profileItems().sortedBy { it.order })
    val socials = MutableStateFlow(context.profileSocialItems().sortedBy { it.order })
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
}
