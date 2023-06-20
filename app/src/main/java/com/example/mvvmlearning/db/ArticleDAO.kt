package com.example.mvvmlearning.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mvvmlearning.models.Article

@Dao
interface ArticleDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article):Long // Returns id that was inserted

    @Query("SELECT * FROM Articles ")
    fun getAllArticles():LiveData<List<Article>> //Not a suspend function because it returns a live data and livedata cant be used with suspend functions

     @Delete
     suspend fun deleteArticle(article: Article)
}