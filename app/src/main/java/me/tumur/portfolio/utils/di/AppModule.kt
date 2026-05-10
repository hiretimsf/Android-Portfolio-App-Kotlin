package me.tumur.portfolio.utils.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.tumur.portfolio.repository.database.AppDatabase
import me.tumur.portfolio.repository.database.dao.button.ButtonDao
import me.tumur.portfolio.repository.database.dao.category.CategoryDao
import me.tumur.portfolio.repository.database.dao.experience.ExperienceDao
import me.tumur.portfolio.repository.database.dao.favorite.FavoriteDao
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
import me.tumur.portfolio.repository.network.RestApi
import me.tumur.portfolio.repository.repo.Repository
import me.tumur.portfolio.repository.repo.RepositoryImp
import me.tumur.portfolio.utils.constants.DbConstants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
    fun provideNetworkProperties(@ApplicationContext context: Context): Properties {
        return Properties().apply {
            context.assets.open(NETWORK_PROPERTIES).use(::load)
        }
    }

    @Provides
    @Singleton
    fun provideRestApi(properties: Properties, okHttpClient: OkHttpClient): RestApi {
        return Retrofit.Builder()
            .baseUrl(properties.getProperty(Http.URL))
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RestApi::class.java)
    }

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
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, DbConstants.DATABASE_NAME)
            .fallbackToDestructiveMigration(true)
            .enableMultiInstanceInvalidation()
            .build()
    }

    @Provides
    @Singleton
    fun provideRepository(repository: RepositoryImp): Repository = repository

    @Provides
    fun provideWelcomeDao(database: AppDatabase): WelcomeDao = database.welcomeDao

    @Provides
    fun provideProfileDao(database: AppDatabase): ProfileDao = database.profileDao

    @Provides
    fun provideSocialDao(database: AppDatabase): SocialDao = database.socialDao

    @Provides
    fun provideAboutDao(database: AppDatabase): AboutDao = database.aboutDao

    @Provides
    fun provideAppDao(database: AppDatabase): AppDao = database.appDao

    @Provides
    fun providePortfolioDao(database: AppDatabase): PortfolioDao = database.portfolioDao

    @Provides
    fun provideExperienceDao(database: AppDatabase): ExperienceDao = database.experienceDao

    @Provides
    fun provideTaskDao(database: AppDatabase): TaskDao = database.taskDao

    @Provides
    fun provideButtonDao(database: AppDatabase): ButtonDao = database.buttonDao

    @Provides
    fun provideCategoryDao(database: AppDatabase): CategoryDao = database.categoryDao

    @Provides
    fun provideScreenShotDao(database: AppDatabase): ScreenShotDao = database.screenShotDao

    @Provides
    fun provideFavoriteDao(database: AppDatabase): FavoriteDao = database.favoriteDao

    @Provides
    fun provideLocationDao(database: AppDatabase): LocationDao = database.locationDao

    @Provides
    fun provideResourceDao(database: AppDatabase): ResourceDao = database.resourceDao
}
