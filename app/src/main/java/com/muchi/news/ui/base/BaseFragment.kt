/*
 * Copyright (c) 2020 - Irfanul Haq.
 */

package com.muchi.news.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import butterknife.Unbinder
import dagger.hilt.android.internal.managers.ViewComponentManager

abstract class BaseFragment: Fragment() {

    private var unbinder: Unbinder? = null
    private lateinit var itemView: View

    @LayoutRes
    protected abstract fun layoutRes(): Int

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(layoutRes(), container, false)
        unbinder = ButterKnife.bind(this, view)
        itemView = view
        return view
    }

    open fun activityContext(): Context? {
        val baseContext = itemView.context
        return if (baseContext is ViewComponentManager.FragmentContextWrapper)
                    baseContext.baseContext
                else context
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (unbinder != null) {
            unbinder?.unbind()
            unbinder = null
        }
    }

    override fun onAttach(context: Context) {
        try {
            super.onAttach(context)
        } catch (e: IllegalStateException){
            e.printStackTrace()
        }
    }
}