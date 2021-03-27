/*
 * Copyright (c) 2020 - Irfanul Haq.
 */

package com.muchi.news.ui.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import butterknife.Unbinder

abstract class BaseActivity: AppCompatActivity() {

    @LayoutRes
    protected abstract fun layoutRes(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes())
        getBind()
    }

    open fun getBind(): Unbinder {
        return ButterKnife.bind(this)
    }
}