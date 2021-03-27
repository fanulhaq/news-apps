/*
 * Copyright (c) 2020 - Irfanul Haq.
 */

package com.muchi.news.data.repository

import com.muchi.news.data.local.dao.SourceDao
import com.muchi.news.data.local.entity.ArticleEntity
import com.muchi.news.data.local.entity.SourceEntity
import com.muchi.news.data.remote.ApiService
import com.muchi.news.data.remote.response.ArticleResponse
import com.muchi.news.data.remote.response.SourceResponse
import com.muchi.news.extentions.State
import com.muchi.news.extentions.timesNewsToDateTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton


@ExperimentalCoroutinesApi
@Singleton
class MainRepository @Inject constructor(
    private val sourceDao: SourceDao,
    private val apiService: ApiService
) {

    private fun resultAllSource(response: SourceResponse?): Flow<List<SourceEntity>> {
        return flow {
            response?.sources?.let { emit(it) }
        }
    }

    fun getAllSources(): Flow<State<List<SourceEntity>>> {
        return object : NetworkRequestRepository<List<SourceEntity>, SourceResponse>() {
            override suspend fun fetchFromRemote(): Response<SourceResponse> = apiService.getAllSources()

            override fun fetchFromData(): Flow<List<SourceEntity>> = resultAllSource(request)
        }.asFlow()
    }

    private fun resultSearchArticles(response: ArticleResponse?): Flow<List<ArticleEntity>> {
        val data: ArrayList<ArticleEntity> = ArrayList()

        response?.articles?.forEach {
            data.add(ArticleEntity("${it.source?.id}", "${it.author}", "${it.title}",
                "${it.url}", "${it.urlToImage}", "${it.publishedAt?.timesNewsToDateTime()}"))
        }

        return flow {
            emit(data)
        }
    }

    fun getSearchArticles(value: String): Flow<State<List<ArticleEntity>>> {
        return object : NetworkRequestRepository<List<ArticleEntity>, ArticleResponse>() {
            override suspend fun fetchFromRemote(): Response<ArticleResponse> = apiService.getSearchArticle(value)

            override fun fetchFromData(): Flow<List<ArticleEntity>> = resultSearchArticles(request)
        }.asFlow()
    }

    fun getSources(value: String): Flow<State<List<SourceEntity>>> {
        return object : NetworkBoundRepository<List<SourceEntity>, SourceResponse>() {

            override suspend fun saveRemoteData(response: SourceResponse) = response.sources?.let { sourceDao.insertData(it) }

            override suspend fun fetchFromRemote(): Response<SourceResponse> = apiService.getSources(value)

            override suspend fun deleteOldData() = sourceDao.deleteData(value)

            override fun fetchFromLocal(): Flow<List<SourceEntity>> = sourceDao.getData(value)
        }.asFlow()
    }
}