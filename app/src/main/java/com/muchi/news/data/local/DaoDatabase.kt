/*
 * Copyright (c) 2020 - Irfanul Haq.
 */

package com.muchi.news.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.muchi.news.data.local.dao.ArticleDao
import com.muchi.news.data.local.dao.SourceDao
import com.muchi.news.data.local.entity.ArticleEntity
import com.muchi.news.data.local.entity.SourceEntity

@Database(
    entities = [SourceEntity::class, ArticleEntity::class],
    version = DatabaseMigrations.DB_VERSION
)
abstract class DaoDatabase: RoomDatabase() {

    abstract fun sourceDoa(): SourceDao
    abstract fun articleDao(): ArticleDao

    companion object {
        private const val DB_NAME = "db_news"

        @Volatile
        private var INSTANCE: DaoDatabase? = null

        fun getInstance(context: Context): DaoDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DaoDatabase::class.java,
                    DB_NAME
                ).addMigrations(*DatabaseMigrations.MIGRATIONS).build()

                INSTANCE = instance
                return instance
            }
        }
    }
}