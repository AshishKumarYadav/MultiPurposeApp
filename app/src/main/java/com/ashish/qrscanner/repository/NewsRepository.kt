package com.ashish.qrscanner.repository

import com.ashish.qrscanner.api.NewsApi
import com.ashish.qrscanner.api.NewsModel
import com.ashish.qrscanner.api.RetrofitInstance
import retrofit2.Response

class NewsRepository() {
    suspend fun getTopHeadlines(language : String,country:String,category:String?,apiKey:String): Response<NewsModel> {
       return RetrofitInstance.api.getNews(language,country,category, apiKey)
    }

}