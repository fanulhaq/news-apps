/*
 * Copyright (c) 2021 - Muchi (Irfanul Haq).
 */

package com.muchi.news.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.muchi.news.data.local.entity.SourceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SourceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(data: List<SourceEntity>)

    @Query("DELETE FROM tbl_source WHERE category =:category")
    suspend fun deleteData(category: String)

    @Query("SELECT * FROM tbl_source WHERE category =:category")
    fun getData(category: String): Flow<List<SourceEntity>>
}