package com.ashish.qrscanner.api

import com.ashish.qrscanner.utils.Cons
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitInstance {

    fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Cons.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val api by lazy {
        getInstance().create(NewsApi::class.java)
    }
}