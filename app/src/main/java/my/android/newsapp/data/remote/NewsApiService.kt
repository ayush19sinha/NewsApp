package my.android.newsapp.data.remote

import my.android.newsapp.data.model.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("top-headlines") // Endpoint for fetching top headlines
    suspend fun getAllHeadlines(
        @Query("country") country: String, // The country filter for news articles
        @Query("category") category: String, // The category filter for news articles
        @Query("apiKey") apiKey: String // The API key for authentication
    ): ApiResponse
}