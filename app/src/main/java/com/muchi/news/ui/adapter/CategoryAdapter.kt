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
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.muchi.news.R
import com.muchi.news.R2
import com.muchi.news.utils.gone
import com.muchi.news.utils.visible
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class CategoryAdapter @Inject constructor(
    @ActivityContext private val context: Context
): RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private var data: List<CategoryModel> = ArrayList()
    private var unbinder: Unbinder? = null
    private lateinit var listener: ItemClickCategory

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_category, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentData = data[position]

        holder.tvName.text = currentData.name

        if(currentData.selected){
            holder.lineSelected.visible()
            holder.lineStart.gone()
            holder.tvName.setTextColor(context.resources.getColor(R.color.textColorPrimary))
        } else {
            holder.lineSelected.gone()
            holder.lineStart.visible()
            holder.tvName.setTextColor(context.resources.getColor(R.color.textColorHint))
        }

        when(position){
            0 -> {
                holder.firstView.visible()
                holder.lastView.gone()
                holder.lineEnd.visible()
            }
            data.lastIndex -> {
                holder.firstView.gone()
                holder.lastView.visible()
                holder.lineEnd.gone()
            }
            else -> {
                holder.firstView.gone()
                holder.lastView.gone()
                holder.lineEnd.visible()
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(data: List<CategoryModel>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun getData(): List<CategoryModel> {
        return this.data
    }

    fun setClickListener(itemlistener: ItemClickCategory) {
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
        @BindView(R2.id.firstView)
        lateinit var firstView: View
        @BindView(R2.id.lastView)
        lateinit var lastView: View
        @BindView(R2.id.lineEnd)
        lateinit var lineEnd: View
        @BindView(R2.id.lineStart)
        lateinit var lineStart: View
        @BindView(R2.id.lineSelected)
        lateinit var lineSelected: View

        init {
            unbinder = ButterKnife.bind(this, itemView)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.itemClickCategory(v, adapterPosition, data[adapterPosition])
        }
    }
}

interface ItemClickCategory {
    fun itemClickCategory(view: View?, position: Int, data: CategoryModel)
}

data class CategoryModel(
    var name: String,
    var selected: Boolean
)