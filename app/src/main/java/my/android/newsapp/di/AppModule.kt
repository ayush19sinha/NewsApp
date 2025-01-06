package my.android.newsapp.di


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import my.android.newsapp.data.remote.NewsApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

 /* Dagger Hilt module for providing application-wide dependencies. */
 /* This module defines methods for providing the Retrofit instance and the NewsApiService, */
 /* which will be injected into the application where needed. */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton // Ensures that only one instance of Retrofit is created and shared throughout the app
    fun provideRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/") //Base Url of the api
            .addConverterFactory(GsonConverterFactory.create()) // Gson convertor for serialization and de-serialization
            .build()
    }

    @Provides
    @Singleton // Ensures that the NewsApiService is a singleton and shared throughout the app
    fun provideApiService(retrofit: Retrofit): NewsApiService{
        return retrofit.create(NewsApiService::class.java)  // Creates and returns the NewsApiService instance
    }
}