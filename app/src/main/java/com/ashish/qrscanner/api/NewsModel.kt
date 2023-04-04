package com.ashish.qrscanner.api

data class NewsModel(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
    )