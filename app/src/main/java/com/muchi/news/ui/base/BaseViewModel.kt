/*
 * Copyright (c) 2021 - Muchi (Irfanul Haq).
 */

package com.muchi.news.ui.base

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.muchi.news.extentions.Event
import kotlin.reflect.KClass

open class BaseViewModel : ViewModel() {

    private var _navEvent = MutableLiveData<Event<Pair<KClass<*>, Bundle?>>>()

    val navEvent: LiveData<Event<Pair<KClass<*>, Bundle?>>>
        get() = _navEvent

    fun navEvent(pair: Pair<KClass<*>, Bundle?>){
        _navEvent.postValue(Event(pair))
    }
}