/*
 * Copyright (c) 2020 - Irfanul Haq.
 */

package com.muchi.news.data.repository

import android.content.Context
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.muchi.news.extentions.State
import com.muchi.news.extentions.isNetworkAvailable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import retrofit2.Response

/**
 * A repository which provides resource from local database as well as remote end point.
 *
 * [RESULT] represents the type for database.
 * [REQUEST] represents the type for network.
 */
@ExperimentalCoroutinesApi
abstract class NetworkBoundRepository<RESULT, REQUEST> {

    fun asFlow(context: Context) = flow<State<RESULT>> {

        emit(State.loading())
        emit(State.success(fetchFromLocal().first()))

        if(context.isNetworkAvailable()) {
            val apiResponse = fetchFromRemote()
            val remoteBody = apiResponse.body()

            if (apiResponse.isSuccessful && remoteBody != null) {
                deleteOldData()
                saveRemoteData(remoteBody)
            } else {
                emit(State.error(apiResponse.message(), apiResponse.code()))
            }

            emitAll(
                fetchFromLocal().map {
                    State.success(it)
                }
            )
        } else {
            emit(State.error("", -1))
        }
    }.catch { e ->
        emit(State.error(e.message.toString(), 69))
        e.printStackTrace()
    }


    @WorkerThread
    protected abstract suspend fun deleteOldData()

    @WorkerThread
    protected abstract suspend fun saveRemoteData(response: REQUEST): Unit?

    @MainThread
    protected abstract fun fetchFromLocal(): Flow<RESULT>

    @MainThread
    protected abstract suspend fun fetchFromRemote(): Response<REQUEST>
}

@ExperimentalCoroutinesApi
abstract class NetworkRequestRepository<RESULT, REQUEST>  {

    var request: REQUEST? = null

    fun asFlow(context: Context) = flow<State<RESULT>> {
        if(context.isNetworkAvailable()) {
            emit(State.loading())

            val apiResponse = fetchFromRemote()
            val remoteBody = apiResponse.body()

            if (apiResponse.isSuccessful && remoteBody != null) {
                request = remoteBody
            } else {
                emit(State.error(apiResponse.message(), apiResponse.code()))
            }

            emitAll(
                fetchFromData().map {
                    State.success(it)
                }
            )
        } else {
            emit(State.error("", -1))
        }
    }.catch { e ->
        emit(State.error(e.message.toString(), 69))
        e.printStackTrace()
    }

    @MainThread
    protected abstract fun fetchFromData(): Flow<RESULT>

    @MainThread
    protected abstract suspend fun fetchFromRemote(): Response<REQUEST>
}
