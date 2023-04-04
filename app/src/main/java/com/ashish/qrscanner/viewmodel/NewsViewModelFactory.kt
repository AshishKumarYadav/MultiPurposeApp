package com.ashish.qrscanner.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ashish.qrscanner.repository.NewsRepository

class NewsViewModelFactory(private val application: Application
) :ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)){
            return NewsViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}