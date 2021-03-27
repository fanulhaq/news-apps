/*
 * Copyright (c) 2020 - Irfanul Haq.
 */

package com.muchi.news.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseMigrations {
    const val DB_VERSION = 2

    val MIGRATIONS: Array<Migration>
        get() = arrayOf(
            migration12()
        )

    private fun migration12(): Migration = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE tbl_source ADD COLUMN body TEXT")
            database.execSQL("ALTER TABLE tbl_article ADD COLUMN body TEXT")
        }
    }
}