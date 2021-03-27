/*
 * Copyright (c) 2020 - Irfanul Haq.
 */

package com.muchi.news.extentions

import androidx.lifecycle.Observer

open class Event<out T>(private val event: T) {

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

class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<Event<T>> {
    override fun onChanged(event: Event<T>?) {
        event?.getEventIfNotHandled()?.let { value ->
            onEventUnhandledContent(value)
        }
    }
}