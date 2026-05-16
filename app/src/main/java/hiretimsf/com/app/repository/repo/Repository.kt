package hiretimsf.com.app.repository.repo

import hiretimsf.com.app.repository.database.model.button.ButtonModel
import hiretimsf.com.app.repository.database.model.category.CategoryModel
import hiretimsf.com.app.repository.database.model.portfolio.PortfolioModel
import hiretimsf.com.app.repository.database.model.screenshot.ScreenShotModel
import hiretimsf.com.app.repository.network.Result
import hiretimsf.com.app.repository.network.model.AboutData
import hiretimsf.com.app.screens.blog.BlogPost
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun observeAbout(): Flow<AboutData>

    fun observePortfolio(ownerId: String): Flow<List<PortfolioModel>>

    fun observePortfolioItem(id: String): Flow<PortfolioModel?>

    fun observePortfolioButtons(ownerId: String): Flow<List<ButtonModel>>

    fun observePortfolioCategories(type: Int): Flow<List<CategoryModel>>

    fun observePortfolioScreenshots(ownerId: String): Flow<List<ScreenShotModel>>

    fun observeBlogPosts(): Flow<List<BlogPost>>

    fun observeBlogPost(slug: String): Flow<BlogPost?>

    /**
     * Fetch project data from network and update the local cache.
     */
    suspend fun fetchAll(): Result

    suspend fun fetchAbout(): Result

    suspend fun fetchBlogPosts(): Result

    suspend fun fetchBlogPost(slug: String): Result
}
