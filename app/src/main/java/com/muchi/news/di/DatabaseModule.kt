/*
 * Copyright (c) 2020 - Irfanul Haq.
 */

package com.muchi.news.di

import android.app.Application
import com.muchi.news.data.local.DaoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(application: Application) = DaoDatabase.getInstance(application)

    @Singleton
    @Provides
    fun provideSourceDao(database: DaoDatabase) = database.sourceDoa()

    @Singleton
    @Provides
    fun provideArticleDao(database: DaoDatabase) = database.articleDao()
}