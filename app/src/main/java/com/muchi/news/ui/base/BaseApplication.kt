/*
 * Copyright (c) 2020 - Irfanul Haq
 */

package com.muchi.news.ui.base

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@HiltAndroidApp
class BaseApplication: Application()