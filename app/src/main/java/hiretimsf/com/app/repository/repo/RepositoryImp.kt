package hiretimsf.com.app.repository.repo

import hiretimsf.com.app.repository.cache.CacheDao
import hiretimsf.com.app.repository.cache.CacheEntry
import hiretimsf.com.app.repository.cache.CacheKeys
import hiretimsf.com.app.repository.database.LocalPortfolioStrings
import hiretimsf.com.app.repository.database.dao.button.ButtonDao
import hiretimsf.com.app.repository.database.dao.category.CategoryDao
import hiretimsf.com.app.repository.database.dao.portfolio.PortfolioDao
import hiretimsf.com.app.repository.database.dao.screenshot.ScreenShotDao
import hiretimsf.com.app.repository.database.model.button.ButtonModel
import hiretimsf.com.app.repository.database.model.category.CategoryModel
import hiretimsf.com.app.repository.database.model.portfolio.PortfolioModel
import hiretimsf.com.app.repository.database.model.screenshot.ScreenShotModel
import hiretimsf.com.app.repository.network.Failed
import hiretimsf.com.app.repository.network.Result
import hiretimsf.com.app.repository.network.RestApi
import hiretimsf.com.app.repository.network.Success
import hiretimsf.com.app.repository.network.model.AboutData
import hiretimsf.com.app.repository.network.model.ProjectResponse
import hiretimsf.com.app.screens.blog.BlogPost
import hiretimsf.com.app.screens.blog.toBlogPost
import hiretimsf.com.app.utils.constants.DbConstants
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Singleton
class RepositoryImp @Inject constructor(
    private val restApi: RestApi,
    private val cacheDao: CacheDao,
    private val json: Json,
    private val portfolioDao: PortfolioDao,
    private val buttonDao: ButtonDao,
    private val categoryDao: CategoryDao,
    private val screenshotDao: ScreenShotDao,
) : Repository {

    private val localAll = LocalPortfolioStrings.all
    private val localAbout = LocalPortfolioStrings.about.data

    override fun observeAbout(): Flow<AboutData> {
        return observeCached(CacheKeys.ABOUT, localAbout)
    }

    override fun observePortfolio(ownerId: String): Flow<List<PortfolioModel>> {
        return observeCached(CacheKeys.PORTFOLIO, localAll.portfolio)
            .map { items -> items.filter { it.ownerId == ownerId }.sortedBy { it.order } }
    }

    override fun observePortfolioItem(id: String): Flow<PortfolioModel?> {
        return observeCached(CacheKeys.PORTFOLIO, localAll.portfolio)
            .map { items -> items.firstOrNull { it.id == id } }
    }

    override fun observePortfolioButtons(ownerId: String): Flow<List<ButtonModel>> {
        return observeCached(CacheKeys.PORTFOLIO_BUTTONS, localAll.button)
            .map { items -> items.filter { it.ownerId == ownerId }.sortedBy { it.order } }
    }

    override fun observePortfolioCategories(type: Int): Flow<List<CategoryModel>> {
        return observeCached(CacheKeys.PORTFOLIO_CATEGORIES, localAll.category)
            .map { items -> items.filter { it.type == type }.sortedBy { it.order } }
    }

    override fun observePortfolioScreenshots(ownerId: String): Flow<List<ScreenShotModel>> {
        return observeCached(CacheKeys.PORTFOLIO_SCREENSHOTS, localAll.screenshot)
            .map { items -> items.filter { it.ownerId == ownerId }.sortedBy { it.order } }
    }

    override fun observeBlogPosts(): Flow<List<BlogPost>> {
        return observeCached(CacheKeys.BLOG_POSTS, emptyList())
    }

    override fun observeBlogPost(slug: String): Flow<BlogPost?> {
        return cacheDao.observePayload(CacheKeys.blogPost(slug))
            .combine(observeBlogPosts()) { payload, posts ->
                payload?.let { runCatching { json.decodeFromString<BlogPost>(it) }.getOrNull() }
                    ?: posts.firstOrNull { it.slug == slug }
            }
    }

    override suspend fun fetchAll(): Result {
        return try {
            val response = restApi.getProjects()
            val body = response.body()
            if (!response.isSuccessful || body == null) return Failed

            val projects = body.data
                .filter { it.showOnPortfolio }
                .sortedByDescending { it.weight }

            val portfolioItems = projects.mapIndexed { index, project ->
                project.toPortfolioModel(index)
            }
            val ownerIds = portfolioItems.map { it.id }.toSet()
            val categoryTypes = portfolioItems.map { it.categoryType }.toSet()
            val buttonItems = projects.flatMap { it.toButtonModels() }
            val categoryItems = projects.flatMap { it.toCategoryModels() }
            val screenshotItems = projects.map { it.toScreenShotModel() }

            portfolioDao.update(portfolioItems)
            buttonDao.updateForOwners(ownerIds, buttonItems)
            categoryDao.updateForTypes(categoryTypes, categoryItems)
            screenshotDao.updateForOwners(ownerIds, screenshotItems)

            cache(CacheKeys.PORTFOLIO, portfolioItems)
            cache(CacheKeys.PORTFOLIO_BUTTONS, buttonItems)
            cache(CacheKeys.PORTFOLIO_CATEGORIES, categoryItems)
            cache(CacheKeys.PORTFOLIO_SCREENSHOTS, screenshotItems)

            Success
        } catch (_: Exception) {
            Failed
        }
    }

    override suspend fun fetchAbout(): Result {
        return try {
            val response = restApi.getAbout()
            val body = response.body()
            if (!response.isSuccessful || body == null) return Failed
            cache(CacheKeys.ABOUT, body.data)
            Success
        } catch (_: Exception) {
            Failed
        }
    }

    override suspend fun fetchBlogPosts(): Result {
        return try {
            val response = restApi.getBlogPosts()
            val body = response.body()
            if (!response.isSuccessful || body == null) return Failed
            cache(CacheKeys.BLOG_POSTS, body.data.map { it.toBlogPost() })
            Success
        } catch (_: Exception) {
            Failed
        }
    }

    override suspend fun fetchBlogPost(slug: String): Result {
        return try {
            val response = restApi.getBlogPost(slug)
            val body = response.body()
            if (!response.isSuccessful || body == null) return Failed
            cache(CacheKeys.blogPost(slug), body.data.toBlogPost())
            Success
        } catch (_: Exception) {
            Failed
        }
    }

    private inline fun <reified T> observeCached(key: String, fallback: T): Flow<T> {
        return cacheDao.observePayload(key).map { payload ->
            payload?.let { runCatching { json.decodeFromString<T>(it) }.getOrNull() } ?: fallback
        }
    }

    private suspend inline fun <reified T> cache(key: String, payload: T) {
        cacheDao.upsert(
            CacheEntry(
                key = key,
                payload = json.encodeToString(payload),
                updatedAtMillis = System.currentTimeMillis(),
            ),
        )
    }

    private fun ProjectResponse.toPortfolioModel(index: Int): PortfolioModel {
        val projectId = projectId()
        return PortfolioModel(
            id = projectId,
            ownerId = DbConstants.PERSON_ID,
            title = title,
            subTitle = title,
            logo = "",
            logoDescription = imageAlt,
            coverImage = imageUrl,
            imageDescription = imageAlt,
            text = description,
            info = techStacks.joinToString(", "),
            dateFrom = parseProjectDate(fromDate),
            dateTo = parseProjectDate(toDate),
            header = category,
            categoryType = categoryType(),
            videoUrl = videoEmbedUrl,
            linkToShare = websiteUrl ?: portfolioUrl ?: githubUrl,
            order = index + 1,
        )
    }

    private fun ProjectResponse.toButtonModels(): List<ButtonModel> {
        val projectId = projectId()
        return listOfNotNull(
            websiteUrl?.takeIf { it.isNotBlank() }?.let {
                ButtonModel("$projectId-website", projectId, "Live Demo", it, "web", 1)
            },
            githubUrl?.takeIf { it.isNotBlank() }?.let {
                ButtonModel("$projectId-github", projectId, "Github", it, "github", 2)
            },
        )
    }

    private fun ProjectResponse.toCategoryModels(): List<CategoryModel> {
        return techStacks.mapIndexed { index, techStack ->
            CategoryModel(
                id = "${projectId()}-tech-$index",
                title = techStack,
                type = categoryType(),
                icon = "code",
                iconDescription = techStack,
                order = index + 1,
            )
        }
    }

    private fun ProjectResponse.toScreenShotModel(): ScreenShotModel {
        return ScreenShotModel(
            id = "${projectId()}-screenshot",
            ownerId = projectId(),
            url = imageUrl,
            imageDescription = imageAlt,
            order = 1,
        )
    }

    private fun ProjectResponse.projectId(): String = slug.ifBlank { "project-$id" }

    private fun ProjectResponse.categoryType(): Int = PROJECT_CATEGORY_OFFSET + id

    private fun parseProjectDate(value: String): Date {
        return PROJECT_DATE_FORMATS.firstNotNullOfOrNull { pattern ->
            runCatching {
                SimpleDateFormat(pattern, Locale.US).apply {
                    timeZone = TimeZone.getTimeZone("UTC")
                }.parse(value)
            }.getOrNull()
        } ?: Date(0)
    }

    private companion object {
        private const val PROJECT_CATEGORY_OFFSET = 10_000
        private val PROJECT_DATE_FORMATS = listOf("yyyy-MM-dd", "MM/dd/yyyy")
    }
}
