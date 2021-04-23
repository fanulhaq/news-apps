/*
 * Copyright (c) 2021 - Muchi (Irfanul Haq).
 */

@file:Suppress("DEPRECATION")

package com.muchi.news.ui.article

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.muchi.news.data.local.entity.ArticleEntity
import com.muchi.news.data.repository.ArticleRepository
import com.muchi.news.extentions.State
import com.muchi.news.extentions.isNetworkAvailable
import com.muchi.news.ui.base.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class ArticleViewModel  @ViewModelInject constructor(
    private val articleRepository: ArticleRepository
): BaseViewModel() {

    private val _article = MutableLiveData<State<List<ArticleEntity>>>()

    val article: LiveData<State<List<ArticleEntity>>>
        get() = _article

    fun article(context: Context, value: String){
        if(context.isNetworkAvailable()){
            viewModelScope.launch {
                articleRepository.getArticleSource(value).collect {
                    _article.postValue(it)
                }
            }
        } else {
            _article.postValue(State.error("No Internet", -1))
        }
    }
}