/*
 * Copyright (c) 2021 - Muchi (Irfanul Haq).
 */

@file:SuppressLint("NonConstantResourceId")
@file:Suppress("DEPRECATION")

package com.muchi.news.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.muchi.news.R
import com.muchi.news.R2
import com.muchi.news.data.local.entity.ArticleEntity
import com.muchi.news.utils.gone
import com.muchi.news.utils.loadImageForGlide
import com.muchi.news.utils.visible
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class ArticleAdapter @Inject constructor(
    @ActivityContext private val context: Context
) : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    private var data: List<ArticleEntity> = ArrayList()
    private var unbinder: Unbinder? = null
    private lateinit var listener: ItemClickArticle

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_article, parent, false)
        return ViewHolder(itemView)
    }

    @SuppressLint("DefaultLocale")
    @Suppress("DEPRECATION")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentData = data[position]

        holder.tvName.text = currentData.title
        holder.tvTime.text = currentData.publishedAt
        holder.tvAuthor.text = if(currentData.author.isNullOrEmpty() || currentData.author == "null") "Anonymous"
                                else currentData.author

        currentData.urlToImage?.let { url -> context.loadImageForGlide(holder.imageView, url) }

        when(position){
            0 -> {
                holder.lastView.gone()
            }
            data.lastIndex -> {
                holder.lastView.visible()
            }
            else -> {
                holder.lastView.gone()
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(data: List<ArticleEntity>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun getData(): List<ArticleEntity> {
        return this.data
    }

    fun setClickListener(itemlistener: ItemClickArticle) {
        listener = itemlistener
    }

    fun unBinder(){
        if (unbinder != null) {
            unbinder?.unbind()
            unbinder = null
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener  {

        @BindView(R2.id.imageView)
        lateinit var imageView: ImageView
        @BindView(R2.id.tvName)
        lateinit var tvName: TextView
        @BindView(R2.id.tvAuthor)
        lateinit var tvAuthor: TextView
        @BindView(R2.id.tvTime)
        lateinit var tvTime: TextView
        @BindView(R2.id.lastView)
        lateinit var lastView: View

        init {
            unbinder = ButterKnife.bind(this, itemView)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.itemClickArticle(v, adapterPosition, data[adapterPosition])
        }
    }
}

interface ItemClickArticle {
    fun itemClickArticle(view: View?, position: Int, data: ArticleEntity)
}