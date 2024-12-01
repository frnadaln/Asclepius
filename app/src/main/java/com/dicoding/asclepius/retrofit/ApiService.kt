package com.dicoding.asclepius.retrofit

import com.dicoding.asclepius.data.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface Apiservice {
    @GET("top-headlines")
    suspend fun getCancerNews(
        @Query("q") query: String = "cancer",
        @Query("category") category: String = "health",
        @Query("language") language: String = "en",
        @Query("apiKey") apiKey: String
    ): NewsResponse
}
