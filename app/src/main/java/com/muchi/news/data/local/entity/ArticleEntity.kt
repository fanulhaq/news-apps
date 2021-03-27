/*
 * Copyright (c) 2021. ~ Irfanul Haq.
 */

package com.muchi.news.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_article")
data class ArticleEntity(
    var source: String? = null,
    var author: String? = null,
    var title: String? = null,
    var url: String? = null,
    var urlToImage: String? = null,
    var publishedAt: String? = null
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}