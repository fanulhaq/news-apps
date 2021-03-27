/*
 * Copyright (c) 2021. ~ Irfanul Haq.
 */

package com.muchi.news.data.remote.response

import com.muchi.news.data.local.entity.SourceEntity

data class SourceResponse(
    var status: String? = null,
    var totalResults: String? = null,
    var code: String? = null,
    var message: String? = null,
    var sources: List<SourceEntity>? = null
)