/*
 * Copyright (c) 2020 - Irfanul Haq.
 */

package com.muchi.news.data.repository

import android.content.Context
import com.muchi.news.data.local.dao.ArticleDao
import com.muchi.news.data.local.entity.ArticleEntity
import com.muchi.news.data.remote.ApiService
import com.muchi.news.data.remote.response.ArticleModel
import com.muchi.news.data.remote.response.ArticleResponse
import com.muchi.news.extentions.State
import com.muchi.news.extentions.formatterDateOrTime
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton


@ExperimentalCoroutinesApi
@Singleton
class ArticleRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val articleDao: ArticleDao,
    private val apiService: ApiService
) {

    private fun resultArticle(response: List<ArticleModel>?): List<ArticleEntity> {
        val data: ArrayList<ArticleEntity> = ArrayList()
        response?.forEach {
            data.add(ArticleEntity("${it.source?.id}", "${it.author}", "${it.title}",
                "${it.url}", "${it.urlToImage}",
                "${it.publishedAt?.replace("T", " ")?.replace("Z", "")?.formatterDateOrTime("yyyy-MM-dd hh:mm:ss", "dd MMM yyyy - hh:mm")}"))
        }

        return data
    }

    fun getArticleSource(value: String): Flow<State<List<ArticleEntity>>> {
        return object : NetworkBoundRepository<List<ArticleEntity>, ArticleResponse>() {

            override suspend fun saveRemoteData(response: ArticleResponse) = articleDao.insertData(resultArticle(response.articles))

            override suspend fun fetchFromRemote(): Response<ArticleResponse> = apiService.getArticleSource(value)

            override suspend fun deleteOldData() = articleDao.deleteData(value)

            override fun fetchFromLocal(): Flow<List<ArticleEntity>> = articleDao.getData(value)
        }.asFlow(context)
    }
}