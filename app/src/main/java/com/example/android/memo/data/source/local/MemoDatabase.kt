package com.example.android.memo.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.android.memo.data.Memo

@Database(entities = [Memo::class], version = 1, exportSchema = false)
abstract class MemoDatabase : RoomDatabase() {
    abstract fun memoDao(): MemosDao
}
