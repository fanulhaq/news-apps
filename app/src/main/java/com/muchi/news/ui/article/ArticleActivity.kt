/*
 * Copyright (c) 2021. ~ Irfanul Haq.
 */

@file:SuppressLint("NonConstantResourceId")

package com.muchi.news.ui.article

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.*
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.muchi.news.R
import com.muchi.news.R2
import com.muchi.news.data.local.entity.ArticleEntity
import com.muchi.news.data.remote.handlerErrorResponse
import com.muchi.news.extentions.*
import com.muchi.news.ui.adapter.ArticleAdapter
import com.muchi.news.ui.adapter.ItemClickArticle
import com.muchi.news.ui.base.BaseActivity
import com.muchi.news.ui.dialog.bottomSheetError
import com.muchi.news.ui.dialog.bottomSheetNoInternet
import com.muchi.news.ui.webview.WebViewActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ArticleActivity : BaseActivity(), ItemClickArticle {

    @BindView(R2.id.imageBack)
    lateinit var imageBack: ImageView
    @BindView(R2.id.tvTitle)
    lateinit var tvTitle: TextView
    @BindView(R2.id.progressBar)
    lateinit var progressBar: ProgressBar
    @BindView(R2.id.imageView)
    lateinit var imageView: ImageView
    @BindView(R2.id.itemRV)
    lateinit var itemRV: RecyclerView

    private val articleVM: ArticleViewModel by viewModels()

    private var articleAdapter: ArticleAdapter = ArticleAdapter()
    private var source: String? = null

    override fun layoutRes(): Int {
        return R.layout.activity_article
    }

    companion object {
        const val URL = "url"
        const val SOURCE_ID = "sourceId"
        const val SOURCE_NAME = "sourceName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState == null) getBind()

        val bundle = intent.extras
        if(bundle != null) {
            tvTitle.text = bundle.getString(SOURCE_NAME)
            source = bundle.getString(SOURCE_ID)
        }

        initView()
        initViewModel()

        // Buat jadwal untuk membersihkan cache glide
//        Thread {
//            Glide.get(this).clearDiskCache()
//        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        Glide.get(this).clearMemory()
    }

    private fun initView(){
        itemRV.apply {
            layoutManager = LinearLayoutManager(context, VERTICAL, false)
            adapter = articleAdapter
        }

        articleAdapter.setClickListener(this)
    }

    private fun initViewModel(){
        articleVM.navEvent.observe(this, EventObserver {
            startActivity(Intent(this, it.first.java).apply {
                if(it.second != null)
                    this.putExtras(it.second!!)
            })

            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        })


        articleVM.article.observe(this) { state ->
            when (state) {
                is State.Loading -> {
                    progressBar.visible()
                }
                is State.Success -> {
                    progressBar.gone()

                    if (state.data.isNullOrEmpty()) {
                        imageView.visible()
                    } else {
                        imageView.gone()
                        articleAdapter.setData(state.data)
                    }
                }
                is State.Error -> {
                    progressBar.gone()

                    if(state.code == -1)
                        bottomSheetNoInternet(layoutInflater, 0)
                    else
                        bottomSheetError(layoutInflater, handlerErrorResponse(state.code))
                }
            }
        }

        if (articleVM.article.value !is State.Success)
            source?.let { articleVM.article(this, it) }
    }

    @OnClick(value = [R2.id.imageBack])
    fun clickBack(){
        onBackPressed()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    override fun itemClickArticle(view: View?, position: Int, data: ArticleEntity) {
        val bundle = Bundle()
        bundle.putString(URL, data.url)
        articleVM.navEvent(Pair(WebViewActivity::class, bundle))
    }
}