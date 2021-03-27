/*
 * Copyright (c) 2021. ~ Irfanul Haq.
 */

package com.muchi.news.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_source")
data class SourceEntity(
    var id: String? = null,
    var name: String? = null,
    var description: String? = null,
    var url: String? = null,
    var category: String? = null
) {
    @PrimaryKey(autoGenerate = true)
    var ids: Int = 0
}