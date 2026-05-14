package hiretimsf.com.app.repository.database.model.all

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
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
import hiretimsf.com.app.utils.constants.DbConstants

@Serializable
data class RequestAll (

    @SerialName(DbConstants.WELCOME)
    val welcome: List<WelcomeModel>,

    @SerialName(DbConstants.PROFILE)
    val profile: List<ProfileModel>,

    @SerialName(DbConstants.SOCIAL)
    val social: List<SocialModel>,

    @SerialName(DbConstants.ABOUT)
    val about: List<AboutModel>,

    @SerialName(DbConstants.APP)
    val app: List<AppModel>,

    @SerialName(DbConstants.PORTFOLIO)
    val portfolio: List<PortfolioModel>,

    @SerialName(DbConstants.EXPERIENCE)
    val experience: List<ExperienceModel>,

    @SerialName(DbConstants.BUTTON)
    val button: List<ButtonModel>,

    @SerialName(DbConstants.TASK)
    val task: List<TaskModel>,

    @SerialName(DbConstants.CATEGORY)
    val category: List<CategoryModel>,

    @SerialName(DbConstants.SCREENSHOT)
    val screenshot: List<ScreenShotModel>,

    @SerialName(DbConstants.LOCATION)
    val location: List<LocationModel>,

    @SerialName(DbConstants.RESOURCE)
    val resource: List<ResourceModel>
)
