/*
 * Copyright (c) 2020 - Irfanul Haq.
 */

package com.muchi.news.prefs

import android.content.Context

object DataPreference {
    private const val PREF_DATA_USER = "prefDataUser"
    private const val SEARCH = "search"

    fun getSearch(context: Context?): Int? {
        return context?.getSharedPreferences(PREF_DATA_USER, Context.MODE_PRIVATE)
            ?.getInt(SEARCH, 0)
    }

    fun setSearch(context: Context?, value: Int) {
        context?.getSharedPreferences(PREF_DATA_USER, Context.MODE_PRIVATE)?.edit()
            ?.putInt(SEARCH, value)?.apply()
    }

    fun clearDataUser(context: Context?) {
        context?.getSharedPreferences(PREF_DATA_USER, Context.MODE_PRIVATE)?.edit()?.clear()?.apply()
    }
}