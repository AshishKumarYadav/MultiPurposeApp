package com.ashish.qrscanner.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ashish.qrscanner.BuildConfig
import com.ashish.qrscanner.api.NewsModel
import com.ashish.qrscanner.repository.NewsRepository
import com.ashish.qrscanner.utils.Cons
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(application: Application) : AndroidViewModel(application) {

     val newsData: MutableLiveData<NewsModel>? = MutableLiveData()
     private var repository: NewsRepository = NewsRepository()

    fun fetchNews(string:String,country:String){

       val category = if (string.isNullOrEmpty()) {
            "general"
        } else string

        viewModelScope.launch(Dispatchers.IO){
            try {
                Log.i("MSG_ ", "view-model $category $coroutineContext")
                val response = repository.getTopHeadlines("en",country, category, BuildConfig.NewsApiKey)
                newsData?.postValue(handleResponse(response))
            }catch (t: Throwable){
                t.message
                Log.i("MSG_ ","view-model1 "+t.message)
            }
        }

    }

    private fun handleResponse(response: Response<NewsModel>):NewsModel{
        lateinit var newsModel:NewsModel
        if (response.isSuccessful) {
            response.body().let {
                newsModel = response.body()!!
            }
        }else Log.i("MSG_ ","view-model2 "+response.message())
        return newsModel
    }

}