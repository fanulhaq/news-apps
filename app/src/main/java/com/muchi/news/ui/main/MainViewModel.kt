/*
 * Copyright (c) 2021 - Muchi (Irfanul Haq).
 */

@file:Suppress("DEPRECATION")

package com.muchi.news.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.muchi.news.data.local.entity.ArticleEntity
import com.muchi.news.data.local.entity.SourceEntity
import com.muchi.news.data.repository.MainRepository
import com.muchi.news.extentions.State
import com.muchi.news.ui.adapter.CategoryModel
import com.muchi.news.ui.base.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MainViewModel  @ViewModelInject constructor(
    private val mainRepository: MainRepository
): BaseViewModel() {

    private val _itemCategory = MutableLiveData<List<CategoryModel>>()
    private val _sources = MutableLiveData<State<List<SourceEntity>>>()
    private val _allSources = MutableLiveData<State<List<SourceEntity>>>()
    private val _searchArticle = MutableLiveData<State<List<ArticleEntity>>>()

    init {
        val tempCategory = arrayListOf(
                CategoryModel("Business", true),
                CategoryModel("Entertainment", false),
                CategoryModel("General", false),
                CategoryModel("Health", false),
                CategoryModel("Science", false),
                CategoryModel("Sports", false),
                CategoryModel("Technology", false)
        )

        itemCategory(tempCategory)
    }

    val itemCategory: LiveData<List<CategoryModel>>
        get() = _itemCategory

    fun itemCategory(value: List<CategoryModel>){
        _itemCategory.postValue(value)
    }

    val sources: LiveData<State<List<SourceEntity>>>
        get() = _sources

    fun sources(value: String){
        viewModelScope.launch {
            mainRepository.getSources(value).collect {
                _sources.postValue(it)
            }
        }
    }

    val allSources: LiveData<State<List<SourceEntity>>>
        get() = _allSources

    fun allSources(){
        viewModelScope.launch {
            mainRepository.getAllSources().collect {
                _allSources.postValue(it)
            }
        }
    }

    val searchArticle: LiveData<State<List<ArticleEntity>>>
        get() = _searchArticle

    fun searchArticle(value: String){
        viewModelScope.launch {
            mainRepository.getSearchArticles(value).collect {
                _searchArticle.postValue(it)
            }
        }
    }
}