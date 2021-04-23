/*
 * Copyright (c) 2021. ~ Irfanul Haq.
 */

@file:SuppressLint("NonConstantResourceId", "DefaultLocale")
@file:Suppress("DEPRECATION")

package com.muchi.news.ui.main

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import androidx.recyclerview.widget.RecyclerView
import butterknife.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.muchi.news.R
import com.muchi.news.R2
import com.muchi.news.data.local.entity.ArticleEntity
import com.muchi.news.data.local.entity.SourceEntity
import com.muchi.news.data.remote.handlerErrorResponse
import com.muchi.news.extentions.*
import com.muchi.news.prefs.DataPreference.getSearch
import com.muchi.news.prefs.DataPreference.setSearch
import com.muchi.news.ui.adapter.ArticleAdapter
import com.muchi.news.ui.adapter.ItemClickArticle
import com.muchi.news.ui.adapter.ItemClickSource
import com.muchi.news.ui.adapter.SourceAdapter
import com.muchi.news.ui.article.ArticleActivity
import com.muchi.news.ui.article.ArticleActivity.Companion.SOURCE_ID
import com.muchi.news.ui.article.ArticleActivity.Companion.SOURCE_NAME
import com.muchi.news.ui.article.ArticleActivity.Companion.URL
import com.muchi.news.ui.dialog.bottomSheetError
import com.muchi.news.ui.dialog.bottomSheetNoInternet
import com.muchi.news.ui.webview.WebViewActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class BottomSheetSearch: BottomSheetDialogFragment(), ItemClickArticle, ItemClickSource {

    @BindView(R2.id.imgBack)
    lateinit var imgBack: ImageView
    @BindView(R2.id.etSearch)
    lateinit var etSearch: EditText
    @BindView(R2.id.tvChange)
    lateinit var tvChange: TextView
    @BindView(R2.id.progressBar)
    lateinit var progressBar: ProgressBar
    @BindView(R2.id.rvSources)
    lateinit var rvSources: RecyclerView
    @BindView(R2.id.rvArticles)
    lateinit var rvArticles: RecyclerView
    @BindView(R2.id.imageView)
    lateinit var imageView: ImageView

    private val mainVM: MainViewModel by viewModels()

    private var articleAdapter: ArticleAdapter = ArticleAdapter()
    private var sourceAdapter: SourceAdapter = SourceAdapter()
    private var dataSource: ArrayList<SourceEntity> = ArrayList()

    companion object {
        private const val TAG = "BottomSheetSearch"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog =  super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val view  = View.inflate(context, R.layout.bs_search, null)
        bottomSheetDialog.setContentView(view)
        ButterKnife.bind(this, view)

        val linearLayout = view.findViewById(R.id.bottomSheet) as LinearLayout
        val params = linearLayout.layoutParams
        params.height = Resources.getSystem().displayMetrics.heightPixels
        linearLayout.layoutParams = params
        val mBehavior = BottomSheetBehavior.from(view.parent as View)
        mBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        mBehavior.skipCollapsed = true

        mBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(view: View, i: Int) {
                if (i == BottomSheetBehavior.STATE_HIDDEN) {
                    dismiss()
                }

                if (i == BottomSheetBehavior.STATE_DRAGGING) {
                    mBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }

            override fun onSlide(view: View, v: Float) {

            }
        })

        intiView()
        initViewModel()

        return bottomSheetDialog
    }

    private fun intiView(){
        if(getSearch(context) == 0)
            etSearch.hint = resources.getString(R.string.search_news_sources)
        else
            etSearch.hint = resources.getString(R.string.search_news_articles)

        rvSources.apply {
            layoutManager = LinearLayoutManager(context, VERTICAL, false)
            adapter = sourceAdapter
        }
        sourceAdapter.setClickListener(this)

        rvArticles.apply {
            layoutManager = LinearLayoutManager(context, VERTICAL, false)
            adapter = articleAdapter
        }

        articleAdapter.setClickListener(this)
    }

    private fun initViewModel(){
        mainVM.navEvent.observe(this, EventObserver {
            startActivity(Intent(context, it.first.java).apply {
                if(it.second != null)
                    this.putExtras(it.second!!)
            })

            activity?.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        })

        mainVM.allSources.observe(this) { state ->
            when (state) {
                is State.Loading -> {
                    showLog(TAG, "Loading... Get data sources")
                }
                is State.Success -> {
                    dataSource.clear()

                    if (!state.data.isNullOrEmpty()) {
                        dataSource.addAll(state.data)

                        if(getSearch(context) == 0 && !etSearch.text.isNullOrEmpty())
                            searchItemData(etSearch.text.toString())
                    }
                }
                is State.Error -> {
                    if(state.code == -1)
                        context?.bottomSheetNoInternet(layoutInflater, 0)
                    else
                        context?.bottomSheetError(layoutInflater, handlerErrorResponse(state.code))
                }
            }
        }

        mainVM.searchArticle.observe(this) { state ->
            when (state) {
                is State.Loading -> {
                    progressBar.visible()
                }
                is State.Success -> {
                    progressBar.gone()

                    if (state.data.isNullOrEmpty()) {
                        articleAdapter.setData(emptyList())
                        imageView.visible()
                    } else {
                        imageView.gone()
                        articleAdapter.setData(state.data)
                    }
                }
                is State.Error -> {
                    progressBar.gone()

                    if(state.code == -1)
                        context?.bottomSheetNoInternet(layoutInflater, 0)
                    else
                        context?.bottomSheetError(layoutInflater, handlerErrorResponse(state.code))
                }
            }
        }

        if (mainVM.allSources.value !is State.Success)
            context?.let { mainVM.allSources(it) }
    }

    private fun searchSource(text: String){
        if(dataSource.isNullOrEmpty()){
            context.toastShort("Data sources is not ready")
        } else {
            val temp: MutableList<SourceEntity> = ArrayList()
            for (d in dataSource) {
                if (d.name?.toLowerCase()?.contains(text.toLowerCase()) == true)
                    temp.add(d)
            }

            sourceAdapter.setData(temp)

            if(temp.isNullOrEmpty())
                imageView.visible()
            else
                imageView.gone()
        }
    }

    @OnClick(value = [R2.id.imgBack])
    fun clickBack(){
        imgBack.didTapButton()
        dismiss()
    }

    @OnClick(value = [R2.id.tvChange])
    fun clickChange(){
        tvChange.didTapButton()
        dialogChange()
    }

    @OnTextChanged(value = [R2.id.etSearch])
    fun searchItemData(text: CharSequence) {
        if(text.length >= 3){
            when(getSearch(context)){
                0 -> {
                    searchSource(text.toString())
                }
                1 -> {
                    context?.let { mainVM.searchArticle(it, text.toString()) }
                }
            }
        } else {
            imageView.gone()
            sourceAdapter.setData(emptyList())
            articleAdapter.setData(emptyList())
        }
    }

    @OnEditorAction(R.id.etSearch)
    fun actionSearch(actionId: Int): Boolean {
        if(actionId == IME_ACTION_SEARCH){
            if(!etSearch.text.isNullOrEmpty()){
                when(getSearch(context)){
                    0 -> {
                        searchSource(etSearch.text.toString())
                    }
                    1 -> {
                        context?.let { mainVM.searchArticle(it, etSearch.text.toString()) }
                    }
                }
            }
        }
        return false
    }

    private fun dialogChange(){
        val builder = context?.let { AlertDialog.Builder(it, R.style.AlertDialogAnimationStyle) }
        val view = layoutInflater.inflate(R.layout.dialog_change, null)

        val tvCancel: TextView = view.findViewById(R.id.tvCancel)
        val tvYes: TextView = view.findViewById(R.id.tvYes)
        val source: RadioButton = view.findViewById(R.id.source)
        val article: RadioButton = view.findViewById(R.id.article)

        when(getSearch(context)){
            0 -> source.isChecked = true
            1 -> article.isChecked = true
        }

        builder?.setView(view)
        val dialog = builder?.create()
        dialog?.window?.setBackgroundDrawableResource(R.drawable.bg_dialog)
        dialog?.show()

        tvCancel.setOnClickListener {
            dialog?.dismiss()
        }

        tvYes.setOnClickListener {
            if(source.isChecked || article.isChecked){
                if(source.isChecked) {
                    setSearch(context, 0)
                    etSearch.hint = resources.getString(R.string.search_news_sources)
                }

                if (article.isChecked) {
                    setSearch(context, 1)
                    etSearch.hint = resources.getString(R.string.search_news_articles)
                }

                etSearch.setText("")

                dialog?.dismiss()
                (activity as MainActivity).changeHintSearch()
            }
        }
    }

    override fun itemClickArticle(view: View?, position: Int, data: ArticleEntity) {
        val bundle = Bundle()
        bundle.putString(URL, data.url)
        mainVM.navEvent(Pair(WebViewActivity::class, bundle))
    }

    override fun itemClickSource(view: View?, position: Int, data: SourceEntity) {
        val bundle = Bundle()
        bundle.putString(SOURCE_ID, data.id)
        bundle.putString(SOURCE_NAME, data.name)
        mainVM.navEvent(Pair(ArticleActivity::class, bundle))
    }
}