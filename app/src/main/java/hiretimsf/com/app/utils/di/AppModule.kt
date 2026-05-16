package hiretimsf.com.app.utils.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import androidx.room.Room
import hiretimsf.com.app.repository.cache.CacheDao
import hiretimsf.com.app.repository.cache.PortfolioCacheDatabase
import hiretimsf.com.app.repository.network.RestApi
import hiretimsf.com.app.repository.repo.Repository
import hiretimsf.com.app.repository.repo.RepositoryImp
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.Properties
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val NETWORK_PROPERTIES = "network.properties"

object Http {
    const val URL = "URL"
    const val CONNECT = "CONNECT"
    const val READ = "READ"
    const val WRITE = "WRITE"
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    @Provides
    @Singleton
    fun provideNetworkProperties(@ApplicationContext context: Context): Properties {
        return Properties().apply {
            context.assets.open(NETWORK_PROPERTIES).use(::load)
        }
    }

    @Provides
    @Singleton
    fun provideRestApi(properties: Properties, okHttpClient: OkHttpClient, json: Json): RestApi {
        return Retrofit.Builder()
            .baseUrl(properties.getProperty(Http.URL))
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(RestApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCacheDatabase(@ApplicationContext context: Context): PortfolioCacheDatabase {
        return Room.databaseBuilder(
            context,
            PortfolioCacheDatabase::class.java,
            "portfolio-cache.db",
        ).build()
    }

    @Provides
    fun provideCacheDao(database: PortfolioCacheDatabase): CacheDao = database.cacheDao()

    @Provides
    @Singleton
    fun provideOkHttpClient(properties: Properties, interceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(properties.getProperty(Http.CONNECT).toLong(), TimeUnit.SECONDS)
            .readTimeout(properties.getProperty(Http.READ).toLong(), TimeUnit.SECONDS)
            .writeTimeout(properties.getProperty(Http.WRITE).toLong(), TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideRepository(repository: RepositoryImp): Repository = repository
}
