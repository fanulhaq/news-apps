/*
 * Copyright (c) 2021. ~ Irfanul Haq.
 */

@file:SuppressLint("NonConstantResourceId", "DefaultLocale")

package com.muchi.news.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.*
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.OnClick
import com.muchi.news.R
import com.muchi.news.R2
import com.muchi.news.data.local.entity.SourceEntity
import com.muchi.news.data.remote.handlerErrorResponse
import com.muchi.news.extentions.*
import com.muchi.news.prefs.DataPreference.getSearch
import com.muchi.news.ui.adapter.*
import com.muchi.news.ui.article.ArticleActivity
import com.muchi.news.ui.article.ArticleActivity.Companion.SOURCE_ID
import com.muchi.news.ui.article.ArticleActivity.Companion.SOURCE_NAME
import com.muchi.news.ui.base.BaseActivity
import com.muchi.news.ui.dialog.bottomSheetError
import com.muchi.news.ui.dialog.bottomSheetNoInternet
import com.muchi.news.utils.NetworkUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : BaseActivity(), ItemClickCategory, ItemClickSource {

    @BindView(R2.id.categoryRV)
    lateinit var categoryRV: RecyclerView
    @BindView(R2.id.itemRV)
    lateinit var itemRV: RecyclerView
    @BindView(R2.id.imageView)
    lateinit var imageView: ImageView
    @BindView(R2.id.progressBar)
    lateinit var progressBar: ProgressBar
    @BindView(R2.id.cvSearch)
    lateinit var cvSearch: CardView
    @BindView(R2.id.tvSearch)
    lateinit var tvSearch: TextView

    private val mainVM: MainViewModel by viewModels()

    private var categoryAdapter: CategoryAdapter? = null
    private var sourceAdapter: SourceAdapter = SourceAdapter()
    private var linearLayoutManager: LinearLayoutManager? = null
    private var beforePosition = 0
    private var category = "business"

    override fun layoutRes(): Int {
        return R.layout.activity_main
    }

    companion object {
        const val RC_SEARCH = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState == null) getBind()

        initView()
        initViewModel()
    }

    private fun initView(){
        changeHintSearch()

        linearLayoutManager = LinearLayoutManager(this, HORIZONTAL, false)
        categoryAdapter = CategoryAdapter(this)
        categoryRV.apply {
            layoutManager = linearLayoutManager
            adapter = categoryAdapter
        }
        categoryAdapter?.setClickListener(this)

        itemRV.apply {
            layoutManager = LinearLayoutManager(context, VERTICAL, false)
            adapter = sourceAdapter
        }
        sourceAdapter.setClickListener(this)
    }

    private fun initViewModel(){
        mainVM.itemCategory.observe(this){
            categoryAdapter?.setData(it)
        }

        mainVM.sources.observe(this) { state ->
            when (state) {
                is State.Loading -> {
                    sourceAdapter.setData(emptyList())
                    progressBar.visible()
                }
                is State.Success -> {
                    progressBar.gone()

                    if (state.data.isNullOrEmpty()) {
                        imageView.visible()
                    } else {
                        imageView.gone()

                        if(state.data[0].category == category)
                            sourceAdapter.setData(state.data)
                    }
                }
                is State.Error -> {
                    progressBar.gone()

                    if(state.code != 69)
                        bottomSheetError(layoutInflater, handlerErrorResponse(state.code))
                }
            }
        }

        mainVM.navEvent.observe(this, EventObserver {
            startActivity(Intent(this, it.first.java).apply {
                if(it.second != null)
                    this.putExtras(it.second!!)
            })

            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        })


        if (mainVM.sources.value !is State.Success)
            mainVM.sources(this, category)

        handleNetworkChanges()
    }

    override fun itemClickCategory(view: View?, position: Int, data: CategoryModel) {
        if(beforePosition != position){
            beforePosition = position
            category = data.name.toLowerCase()

            val selectedData = categoryAdapter?.getData()
            selectedData?.forEach {
                it.selected = data.name == it.name
            }

            selectedData?.let { mainVM.itemCategory(it) }
            linearLayoutManager?.scrollToPositionWithOffset(position, 30)

            mainVM.sources(this, category)
        }
    }

    override fun itemClickSource(view: View?, position: Int, data: SourceEntity) {
        val bundle = Bundle()
        bundle.putString(SOURCE_ID, data.id)
        bundle.putString(SOURCE_NAME, data.name)
        mainVM.navEvent(Pair(ArticleActivity::class, bundle))
    }

    @OnClick(value = [R2.id.cvSearch])
    fun clickSearch(){
        cvSearch.didTapButton()

        val bottomSheetFragment =  BottomSheetSearch()
        val bundle = Bundle()
        bottomSheetFragment.arguments = bundle
        val ft = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag("BottomSheetFragment")

        if (prev != null) {
            ft.remove(prev)
        }

        ft.addToBackStack(null)
        ft.let { bottomSheetFragment.show(it, "BottomSheetFragment") }
    }

    fun changeHintSearch(){
        if(getSearch(this) == 0)
            tvSearch.text = resources.getString(R.string.search_news_sources)
        else
            tvSearch.text = resources.getString(R.string.search_news_articles)
    }

    private fun handleNetworkChanges() {
        val dialog = bottomSheetNoInternet(layoutInflater, 1)
        NetworkUtils.getNetworkLiveData(this).observe(this) { isConnected ->
            if (!isConnected) {
                if(!dialog.isShowing) dialog.show()
            } else {
                dialog.dismiss()

                if(mainVM.sources.value is State.Error || sourceAdapter.itemCount == 0)
                    mainVM.sources(this, category)
            }
        }
    }
}