package me.tumur.portfolio.repository.repo

import me.tumur.portfolio.repository.database.dao.button.ButtonDao
import me.tumur.portfolio.repository.database.dao.category.CategoryDao
import me.tumur.portfolio.repository.database.dao.experience.ExperienceDao
import me.tumur.portfolio.repository.database.dao.location.LocationDao
import me.tumur.portfolio.repository.database.dao.portfolio.PortfolioDao
import me.tumur.portfolio.repository.database.dao.profile.AboutDao
import me.tumur.portfolio.repository.database.dao.profile.ProfileDao
import me.tumur.portfolio.repository.database.dao.profile.SocialDao
import me.tumur.portfolio.repository.database.dao.resource.ResourceDao
import me.tumur.portfolio.repository.database.dao.screenshot.ScreenShotDao
import me.tumur.portfolio.repository.database.dao.settings.AppDao
import me.tumur.portfolio.repository.database.dao.task.TaskDao
import me.tumur.portfolio.repository.database.dao.welcome.WelcomeDao
import me.tumur.portfolio.repository.network.Failed
import me.tumur.portfolio.repository.network.RestApi
import me.tumur.portfolio.repository.network.Result
import me.tumur.portfolio.repository.network.Success
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImp @Inject constructor(
    private val welcomeDao: WelcomeDao,
    private val profileDao: ProfileDao,
    private val socialDao: SocialDao,
    private val aboutDao: AboutDao,
    private val appDao: AppDao,
    private val portfolioDao: PortfolioDao,
    private val experienceDao: ExperienceDao,
    private val buttonDao: ButtonDao,
    private val taskDao: TaskDao,
    private val categoryDao: CategoryDao,
    private val screenShotDao: ScreenShotDao,
    private val locationDao: LocationDao,
    private val resourceDao: ResourceDao,
    private val api: RestApi
) : Repository {

    /** VARIABLES * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Fetch data from network and update the database,
     * if constraints are met
     * */

    override suspend fun fetchAll(): Result {

        return try {
            /** Start fetch data from network's api */
            val httpResponseAll = api.getAll()

            /** Update the database */
            if (httpResponseAll.isSuccessful) {

                /** Update welcome table */
                httpResponseAll.body()?.welcome?.let {
                    welcomeDao.update(it)
                }

                /** Update profile table */
                httpResponseAll.body()?.profile?.let {
                    profileDao.update(it)
                }

                /** Update social table */
                httpResponseAll.body()?.social?.let {
                    socialDao.update(it)
                }

                /** Update about table */
                httpResponseAll.body()?.about?.let {
                    aboutDao.update(it)
                }

                /** Update portfolio table */
                httpResponseAll.body()?.portfolio?.let {
                    portfolioDao.update(it)
                }

                /** Update button table */
                httpResponseAll.body()?.button?.let {
                    buttonDao.update(it)
                }

                /** Update experience table */
                httpResponseAll.body()?.experience?.let {
                    experienceDao.update(it)
                }

                /** Update tasks table */
                httpResponseAll.body()?.task?.let {
                    taskDao.update(it)
                }

                /** Update app table */
                httpResponseAll.body()?.app?.let {
                    appDao.update(it)
                }

                /** Update category table */
                httpResponseAll.body()?.category?.let {
                    categoryDao.update(it)
                }

                /** Update screenshot table */
                httpResponseAll.body()?.screenshot?.let {
                    screenShotDao.update(it)
                }

                /** Update location table */
                httpResponseAll.body()?.location?.let {
                    locationDao.update(it)
                }

                /** Update resource table */
                httpResponseAll.body()?.resource?.let {
                    resourceDao.update(it)
                }

                Success
            } else Failed
        } catch (exception: IOException) {
            Failed
        }
    }
}
