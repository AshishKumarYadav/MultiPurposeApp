package com.ashish.qrscanner.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface NewsApi {
    //https://newsapi.org/v2/top-headlines?country=in&category=business&apiKey=your_api_key

//    @GET
//    suspend fun handshakeUser(@Url url : String): Response<JsonObject>
//
//
//    @POST
//    suspend fun makePostRequest(
//        @Header("Authorization") token: String = getToken(),
//        @Url url: String,
//        @Body inputModel: JsonObject
//    ): Response<JsonObject>
    @GET("v2/top-headlines")
    suspend fun getNews(@Query("language") language : String,@Query("country") country : String, @Query("category") category : String?, @Query("apiKey") key : String) : Response<NewsModel>

    @GET
    suspend fun getJokes(@Url url:String="https://v2.jokeapi.dev/joke/Any?amount=20"):JokeModel
    // for global
//    https://newsapi.org/v2/top-headlines?category=general&language=en&apiKey=5a3e054de1834138a2fbc4a75ee69053

}