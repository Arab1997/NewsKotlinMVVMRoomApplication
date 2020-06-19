package com.example.newskotlinmvvmapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.newskotlinmvvmapplication.model.NewsDataResponseClass
import com.example.newskotlinmvvmapplication.model.NewsResponseClass
import com.example.newskotlinmvvmapplication.repository.NewsListActivityRepository

class NewsListActivityViewModel(application: Application): AndroidViewModel(application){
    private val repository = NewsListActivityRepository(application)
    val showProgress : LiveData<Boolean>
    val newsList : LiveData<NewsResponseClass>
    var isLoading = false
    var isLastPage = false

    init {
        this.showProgress = repository.showProgress
        this.newsList = repository.newsList
    }

    fun getNewsDataFromDatabase():  LiveData<List<NewsDataResponseClass>>{
        return repository.getNewsFromRoomDatabase()
    }

    fun getNewsData(){
         repository.getNewsData()
    }

    fun deleteNewsData() {
        repository.deleteNewsData()
    }
}
