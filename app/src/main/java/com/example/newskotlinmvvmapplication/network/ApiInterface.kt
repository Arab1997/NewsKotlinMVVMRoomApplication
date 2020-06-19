package com.example.newskotlinmvvmapplication.network

import androidx.annotation.NonNull
import com.example.newskotlinmvvmapplication.model.NewsResponseClass
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val  BASE_URL = "http://newsapi.org/v2/"

interface ApiInterface {
    @GET("everything")
    fun getNewsData(
        @NonNull @Query("q") q: String,
         @NonNull@Query("from") from: String,
        @NonNull @Query("sortBy") sortBy: String,
        @NonNull @Query("apiKey") apiKey: String,
        @NonNull  @Query("page") page: Int
    ): Call<NewsResponseClass>
}