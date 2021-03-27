/*
 * Copyright (c) 2020 - Irfanul Haq.
 */

package com.muchi.news.data.remote

import com.muchi.news.data.remote.ServiceGenerator.API_KEY
import com.muchi.news.data.remote.response.ArticleResponse
import com.muchi.news.data.remote.response.SourceResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("sources")
    suspend fun getSources(@Query("category") category: String,
                           @Query("apiKey") apiKey: String = API_KEY): Response<SourceResponse>

    @GET("sources")
    suspend fun getAllSources(@Query("apiKey") apiKey: String = API_KEY): Response<SourceResponse>

    @GET("top-headlines")
    suspend fun getArticleSource(@Query("sources") sources: String,
                           @Query("apiKey") apiKey: String = API_KEY): Response<ArticleResponse>

    @GET("everything")
    suspend fun getSearchArticle(@Query("q") q: String,
                                 @Query("apiKey") apiKey: String = API_KEY): Response<ArticleResponse>
}