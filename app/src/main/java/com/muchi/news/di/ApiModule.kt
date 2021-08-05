/*
 * Copyright (c) 2020 - Irfanul Haq.
 */

package com.muchi.news.di

import com.muchi.news.data.remote.ApiService
import com.muchi.news.data.remote.ServiceGenerator.createService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApiModule {
    @Singleton
    @Provides
    fun provideRetrofitService(): ApiService = createService(ApiService::class.java)
}