package com.example.newskotlinmvvmapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

data class NewsResponseClass(
    val status: String?,
    val code: String?,
    val message: String?,
    val totalResults: Int?,
    val articles: ArrayList<NewsDataResponseClass>
)

@Entity(tableName = "News")
data class NewsDataResponseClass(
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    @PrimaryKey
    val publishedAt: String,
    val content: String?
)

