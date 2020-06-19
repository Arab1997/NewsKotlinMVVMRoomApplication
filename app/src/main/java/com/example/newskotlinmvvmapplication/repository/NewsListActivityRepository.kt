package com.example.newskotlinmvvmapplication.repository

import android.app.Application
import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.newskotlinmvvmapplication.R
import com.example.newskotlinmvvmapplication.database.getDatabase
import com.example.newskotlinmvvmapplication.model.NewsDataResponseClass
import com.example.newskotlinmvvmapplication.model.NewsResponseClass
import com.example.newskotlinmvvmapplication.network.ApiInterface
import com.example.newskotlinmvvmapplication.network.BASE_URL
import com.example.newskotlinmvvmapplication.util.Constants
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class NewsListActivityRepository(val application: Application) {
    val showProgress = MutableLiveData<Boolean>()
    val newsList = MutableLiveData<NewsResponseClass>()
    private var date: String = ""
    private var currentPage = 0
    var isLoading = false
    var isLastPage = false

    fun getNewsFromRoomDatabase(): LiveData<List<NewsDataResponseClass>> {
        return getDatabase(application).newsDao().getAllNews()
    }

    fun deleteNewsData() {
        Thread(Runnable {
            getDatabase(application).newsDao().deleteAllNews()
        }).start()
    }

    fun getNewsData() {
        showProgress.value = true

        val retrofit =
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .build()

        val service = retrofit.create(ApiInterface::class.java)

        date = SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault())
            .format(Date())
        currentPage++
        isLoading = true
        var call = service.getNewsData(
            Constants.QUERY,
            date,
            Constants.SORT_BY,
            Constants.API_KEY,
            currentPage
        )
        Log.e("Repository", "call.request().url()::" + call.request().url())
        executeAsync(call, object : Callback<NewsResponseClass> {
            override fun onFailure(call: Call<NewsResponseClass>, t: Throwable) {
                showProgress.value = false
                isLoading = false
                Constants.displayToast(application,application.getString(R.string.error_api))
            }

            override fun onResponse(
                call: Call<NewsResponseClass>,
                response: Response<NewsResponseClass>
            ) {
                newsList.value = response.body()
                Log.e("newslist","newsList.value::"+newsList.value)
                if (newsList.value!=null && newsList.value!!.status.equals("ok")) {

                    Log.d("NewsRepository", "Response : ${Gson().toJson(response.body())}")
                    Thread(Runnable {
                        getDatabase(application).newsDao().insertAllNews(newsList.value!!.articles)
                    }).start()
                    isLoading = false

                    showProgress.value = false
                    if (newsList.value!!.articles.size < PAGE_SIZE || newsList.value!!.articles.size == 0 || newsList.value == null) {
                        currentPage = 0
                        date = getPreviousDayDate()!!
                        getNewsData()
                    }
                }else{
                    showProgress.value = false
                    isLoading = false
                    val gson = Gson()
                    val adapter = gson.getAdapter(
                        NewsResponseClass::class.java
                    )
                    try {
                        var registerResponse: NewsResponseClass? = null
                        if (response.errorBody() != null) registerResponse = adapter.fromJson(
                            response.errorBody()!!.string()
                        )
                        Log.e("TAG", "onResponse: registerResponse$registerResponse")
                        if (registerResponse != null) Constants.displayToast(
                            application,
                            registerResponse.message
                        )
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

        })

    }

    private fun getPreviousDayDate(): String? {
        val dateFormat =
            SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault())
        var date1: Date? = null
        try {
            date1 = dateFormat.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val calendar = Calendar.getInstance()
        calendar.time = date1!!
        calendar.add(Calendar.DATE, -1)
        return dateFormat.format(calendar.time)
    }

}

operator fun <T> MutableLiveData<ArrayList<T>>.plusAssign(values: List<T>) {
    val value = this.value ?: arrayListOf()
    value.addAll(values)
    this.value = value
}

private fun <T> executeAsync(call: Call<T>, callback: Callback<T>) {
    call.enqueue(callback)
}

