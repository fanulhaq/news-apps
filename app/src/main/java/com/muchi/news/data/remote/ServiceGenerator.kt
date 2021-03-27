/*
 * Copyright (c) 2020 - Irfanul Haq.
 */

package com.muchi.news.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit.*

object ServiceGenerator {
    private const val BASE_URL = "https://newsapi.org/v2/"
    const val API_KEY = "afc30bf8a3ae4321bb2484200146f663" //"79265fdb101b43ab9986d22e2a89b5e7"

    fun <S> createService(serviceClass: Class<S>): S {
        val httpClient = OkHttpClient.Builder()
            .connectTimeout(30, SECONDS)
            .readTimeout(30, SECONDS)
            .writeTimeout(30, SECONDS)
            .retryOnConnectionFailure(true)

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                )
            )
            .client(httpClient.build())
            .build()
            .create(serviceClass)
    }
}