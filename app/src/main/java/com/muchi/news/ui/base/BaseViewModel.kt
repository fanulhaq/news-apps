/*
 * Copyright (c) 2021 - Muchi (Irfanul Haq).
 */

package com.muchi.news.ui.base

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.muchi.news.utils.SingleEvent
import kotlin.reflect.KClass

open class BaseViewModel : ViewModel() {

    private var _singleEvent = MutableLiveData<SingleEvent<Pair<KClass<*>, Bundle?>>>()
    private val _showToast = MutableLiveData<SingleEvent<Any>>()

    val singleEvent: LiveData<SingleEvent<Pair<KClass<*>, Bundle?>>>
        get() = _singleEvent

    fun singleEvent(pair: Pair<KClass<*>, Bundle?>){
        _singleEvent.postValue(SingleEvent(pair))
    }

    val showToast: LiveData<SingleEvent<Any>> get() = _showToast

    fun showToast(msg: Any){
        _showToast.postValue(SingleEvent(msg))
    }
}