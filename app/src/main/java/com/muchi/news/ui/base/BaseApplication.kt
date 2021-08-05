/*
 * Copyright (c) 2020 - Irfanul Haq
 */

package com.muchi.news.ui.base

import android.app.Application
import com.bumptech.glide.Glide
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@HiltAndroidApp
class BaseApplication: Application() {

    override fun onLowMemory() {
        super.onLowMemory()
        Glide.get(this).clearMemory()
        Glide.get(this).clearDiskCache()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        Glide.get(this).trimMemory(level)
    }
}