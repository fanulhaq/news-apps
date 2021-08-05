/*
 * Copyright (c) 2021 - Muchi (Irfanul Haq).
 */

package com.muchi.news.ui.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.muchi.news.data.local.entity.ArticleEntity
import com.muchi.news.data.repository.ArticleRepository
import com.muchi.news.utils.State
import com.muchi.news.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val articleRepository: ArticleRepository
): BaseViewModel() {

    private val _article = MutableLiveData<State<List<ArticleEntity>>>()

    val article: LiveData<State<List<ArticleEntity>>>
        get() = _article

    fun article(value: String){
        viewModelScope.launch {
            articleRepository.getArticleSource(value).collect {
                _article.postValue(it)
            }
        }
    }
}