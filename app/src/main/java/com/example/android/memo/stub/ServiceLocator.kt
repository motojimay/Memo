package com.example.android.memo.stub

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.example.android.memo.data.source.DefaultMemosRepository
import com.example.android.memo.data.source.MemosDataSource
import com.example.android.memo.data.source.MemosRepository
import com.example.android.memo.data.source.local.MemoDatabase
import com.example.android.memo.data.source.local.MemosLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object ServiceLocator {

    private val lock = Any()
    @Volatile
    private var database: MemoDatabase? = null
    @Volatile
    var tasksRepository: MemosRepository? = null
        @VisibleForTesting set

    @Volatile
    var ioDispatcher: CoroutineDispatcher = Dispatchers.IO
        @VisibleForTesting set

    fun provideTasksRepository(context: Context): MemosRepository {
        synchronized(lock) {
            return tasksRepository ?: tasksRepository ?: createTasksRepository(context)
        }
    }

    private fun createTasksRepository(context: Context): MemosRepository {
        val newRepo = DefaultMemosRepository(FakeTasksRemoteDataSource, createTaskLocalDataSource(context))
        tasksRepository = newRepo
        return newRepo
    }

    private fun createTaskLocalDataSource(context: Context): MemosDataSource {
        val database = database ?: createDataBase(context)
        return MemosLocalDataSource(database.memoDao(), ioDispatcher)
    }

    @VisibleForTesting
    fun createDataBase(
        context: Context,
        inMemory: Boolean = false
    ): MemoDatabase {
        synchronized(lock) {
            val result = if (inMemory) {
                // Use a faster in-memory database for tests
                Room.inMemoryDatabaseBuilder(context.applicationContext, MemoDatabase::class.java)
                    .allowMainThreadQueries()
                    .build()
            } else {
                // Real database using SQLite
                Room.databaseBuilder(
                    context.applicationContext,
                    MemoDatabase::class.java, "Memo.db"
                ).build()
            }
            database = result
            return result
        }
    }
}