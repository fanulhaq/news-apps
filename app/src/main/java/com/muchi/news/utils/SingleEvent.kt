/*
 * Copyright (c) 2021. ~ Irfanul Haq.
 */

package com.muchi.news.utils

import androidx.lifecycle.Observer

open class SingleEvent<out T>(private val event: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getEventIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            event
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekEvent(): T = event
}

class SingleEventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<SingleEvent<T>> {
    override fun onChanged(event: SingleEvent<T>?) {
        event?.getEventIfNotHandled()?.let { value ->
            onEventUnhandledContent(value)
        }
    }
}