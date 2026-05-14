package hiretimsf.com.app.repository.network

import hiretimsf.com.app.repository.database.model.all.RequestAll
import hiretimsf.com.app.repository.network.model.AboutResponse
import retrofit2.Response
import retrofit2.http.GET

interface RestApi {

    @GET("api/all/")
    suspend fun getAll(): Response<RequestAll>

    @GET("https://www.hiretimsf.com/api/about")
    suspend fun getAbout(): Response<AboutResponse>
}
