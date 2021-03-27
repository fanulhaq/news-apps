/*
 * Copyright (c) 2021. ~ Irfanul Haq.
 */

@file:SuppressLint("NonConstantResourceId", "SetJavaScriptEnabled")

package com.muchi.news.ui.webview

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.ProgressBar
import butterknife.BindView
import butterknife.OnClick
import com.muchi.news.R
import com.muchi.news.R2
import com.muchi.news.extentions.gone
import com.muchi.news.extentions.isNetworkAvailable
import com.muchi.news.extentions.visible
import com.muchi.news.ui.article.ArticleActivity.Companion.URL
import com.muchi.news.ui.base.BaseActivity
import com.muchi.news.ui.dialog.bottomSheetNoInternet
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class WebViewActivity : BaseActivity() {

    @BindView(R2.id.imageBack)
    lateinit var imageBack: ImageView
    @BindView(R2.id.webView)
    lateinit var webView: WebView
    @BindView(R2.id.progressBar)
    lateinit var progressBar: ProgressBar

    override fun layoutRes(): Int {
        return R.layout.activity_web_view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState == null) getBind()

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar.gone()
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar.visible()
            }
        }

        webView.settings.javaScriptEnabled = true

        val bundle = intent.extras
        if(bundle != null) {
            if(isNetworkAvailable())
                webView.loadUrl("${bundle.getString(URL)}")
            else
                bottomSheetNoInternet(layoutInflater).show()
        }
    }

    @OnClick(value = [R2.id.imageBack])
    fun clickBack(){
        onBackPressed()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}