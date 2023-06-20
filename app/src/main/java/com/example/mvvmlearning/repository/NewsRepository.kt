package com.example.mvvmlearning.repository

import com.example.mvvmlearning.api.RetrofitInstance
import com.example.mvvmlearning.db.ArticleDataBase
import com.example.mvvmlearning.models.Article
import retrofit2.Response
import retrofit2.http.Query

//The purpose of newsRepository is to get data from our database and from our remote data source from retrofit
class  NewsRepository (
    val db:ArticleDataBase
) {
    suspend fun getBreakingNews(countryCode:String,pageNumber:Int) =
        RetrofitInstance.api.getBreakingNews(countryCode,pageNumber)

    suspend fun searchNews(searchQuery: String,pageNumber:Int)=
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    suspend fun upsert(article:Article) =db.getArticleDao().upsert(article)
    fun getSavedNews()=db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
}