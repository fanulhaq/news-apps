/*
 * Copyright (c) 2021 - Muchi (Irfanul Haq).
 */

@file:SuppressLint("NonConstantResourceId")

package com.muchi.news.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.muchi.news.R
import com.muchi.news.R2
import com.muchi.news.data.local.entity.SourceEntity
import com.muchi.news.extentions.gone
import com.muchi.news.extentions.visible

class SourceAdapter: RecyclerView.Adapter<SourceAdapter.ViewHolder>() {

    private var data: List<SourceEntity> = ArrayList()
    private var unbinder: Unbinder? = null
    private lateinit var listener: ItemClickSource

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_source, parent, false)
        return ViewHolder(itemView)
    }

    @SuppressLint("DefaultLocale")
    @Suppress("DEPRECATION")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentData = data[position]

        holder.tvName.text = currentData.name
        holder.tvCategory.text = currentData.category?.capitalize()
        holder.tvDesc.text = currentData.description

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

    fun setData(data: List<SourceEntity>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun getData(): List<SourceEntity> {
        return this.data
    }

    fun setClickListener(itemlistener: ItemClickSource) {
        listener = itemlistener
    }

    fun unBinder(){
        if (unbinder != null) {
            unbinder?.unbind()
            unbinder = null
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener  {

        @BindView(R2.id.tvName)
        lateinit var tvName: TextView
        @BindView(R2.id.tvCategory)
        lateinit var tvCategory: TextView
        @BindView(R2.id.tvDesc)
        lateinit var tvDesc: TextView
        @BindView(R2.id.lastView)
        lateinit var lastView: View

        init {
            unbinder = ButterKnife.bind(this, itemView)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.itemClickSource(v, adapterPosition, data[adapterPosition])
        }
    }
}

interface ItemClickSource {
    fun itemClickSource(view: View?, position: Int, data: SourceEntity)
}