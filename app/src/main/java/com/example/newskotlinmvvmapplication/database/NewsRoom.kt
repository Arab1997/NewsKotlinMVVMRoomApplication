package com.example.newskotlinmvvmapplication.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newskotlinmvvmapplication.model.NewsDataResponseClass

@Dao
interface NewsDao {
    @Query("SELECT * FROM News")
    fun getAllNews(): LiveData<List<NewsDataResponseClass>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllNews(countryList: List<NewsDataResponseClass>)

    @Query("DELETE FROM News")
    fun deleteAllNews()
}

@Database(entities = [(NewsDataResponseClass::class)], version = 2)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun newsDao(): NewsDao
}

private lateinit var INSTANCE: NewsDatabase
fun getDatabase(context: Context): NewsDatabase {

    synchronized(NewsDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE =
                Room.databaseBuilder(context.applicationContext, NewsDatabase::class.java, "news").fallbackToDestructiveMigration().build()
        }
    }
    return INSTANCE

}