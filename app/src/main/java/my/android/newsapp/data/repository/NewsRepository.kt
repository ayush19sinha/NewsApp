package my.android.newsapp.data.repository

import my.android.newsapp.BuildConfig
import my.android.newsapp.data.model.Article
import my.android.newsapp.data.remote.NewsApiService
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsApiService: NewsApiService
) {
    suspend fun getHeadlines(country: String, category: String): Result<List<Article>> {
        return try {
            val response = newsApiService.getAllHeadlines(
                country = country,
                category = category,
                apiKey = BuildConfig.API_KEY
            )
            Result.success(response.articles)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}