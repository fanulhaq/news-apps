/*
 * Copyright (c) 2021. ~ Irfanul Haq.
 */

package com.muchi.news.utils

/**
 * State Management for UI & Data.
 */
sealed class State<T> {

    class Loading<T> : State<T>()
    data class Success<T>(val data: T) : State<T>()
    data class Error<T>(val message: String, val code: Int) : State<T>()

    companion object {

        /**
         * Returns [State.Loading] instance.
         */
        fun <T> loading() = Loading<T>()

        /**
         * Returns [State.Success] instance.
         * @param data Data to emit with status.
         */
        fun <T> success(data: T) =
            Success(data)

        /**
         * Returns [State.Error] instance.
         * @param message Description of failure.
         */
        fun <T> error(message: String, code: Int) =
            Error<T>(message, code)
    }
}
