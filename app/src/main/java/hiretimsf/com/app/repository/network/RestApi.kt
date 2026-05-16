package hiretimsf.com.app.repository.network

import hiretimsf.com.app.repository.database.model.all.RequestAll
import hiretimsf.com.app.repository.network.model.AboutResponse
import hiretimsf.com.app.repository.network.model.BlogPostDetailResponse
import hiretimsf.com.app.repository.network.model.BlogPostsResponse
import hiretimsf.com.app.repository.network.model.ProjectsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RestApi {

    @GET("api/all/")
    suspend fun getAll(): Response<RequestAll>

    @GET("https://www.hiretimsf.com/api/about")
    suspend fun getAbout(): Response<AboutResponse>

    @GET("https://hiretimsf.com/api/projects")
    suspend fun getProjects(): Response<ProjectsResponse>

    @GET("https://hiretimsf.com/api/blog-posts")
    suspend fun getBlogPosts(
        @Query("q") query: String? = null,
        @Query("limit") limit: Int? = null,
    ): Response<BlogPostsResponse>

    @GET("https://hiretimsf.com/api/blog-posts/{slug}")
    suspend fun getBlogPost(@Path("slug") slug: String): Response<BlogPostDetailResponse>
}
