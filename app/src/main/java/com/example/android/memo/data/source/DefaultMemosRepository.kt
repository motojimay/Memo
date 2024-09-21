package com.example.android.memo.data.source

import androidx.lifecycle.LiveData
import com.example.android.memo.data.Memo
import com.example.android.memo.data.Result
import com.example.android.memo.data.Result.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DefaultMemosRepository(
    private val memosRemoteDataSource: MemosDataSource,
    private val memosLocalDataSource: MemosDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : MemosRepository {
    override fun observeMemos(): LiveData<Result<List<Memo>>> {
        return memosLocalDataSource.observeMemos()
    }

    override suspend fun getMemos(forceUpdate: Boolean): Result<List<Memo>> {
        if (forceUpdate) {
            try {
                updateTasksFromRemoteDataSource()
            } catch (ex: Exception) {
                return Result.Error(ex)
            }
        }
        return memosLocalDataSource.getMemos()
    }

    override suspend fun refreshMemos() {
        updateTasksFromRemoteDataSource()
    }

    override suspend fun refreshMemo(memoId: String) {
        updateMemoFromRemoteDataSource(memoId)
    }

    private suspend fun updateTasksFromRemoteDataSource() {
        val remoteMemos = memosRemoteDataSource.getMemos()

        if (remoteMemos is Success) {
            memosLocalDataSource.deleteAllMemos()
            remoteMemos.data.forEach { memo ->
                memosLocalDataSource.saveMemo(memo)
            }
        } else if (remoteMemos is Result.Error) {
            throw remoteMemos.exception
        }
    }

    override fun observeMemo(memoId: String): LiveData<Result<Memo>> {
        return memosLocalDataSource.observeMemo(memoId)
    }

    private suspend fun updateMemoFromRemoteDataSource(memoId: String) {
        val remoteMemo = memosRemoteDataSource.getMemo(memoId)

        if (remoteMemo is Success) {
            memosLocalDataSource.saveMemo(remoteMemo.data)
        }
    }

    override suspend fun getMemo(memoId: String, forceUpdate: Boolean): Result<Memo> {
        if (forceUpdate) {
            updateMemoFromRemoteDataSource(memoId)
        }
        return memosLocalDataSource.getMemo(memoId)
    }

    override suspend fun saveMemo(memo: Memo) {
        coroutineScope {
            launch { memosRemoteDataSource.saveMemo(memo) }
            launch { memosLocalDataSource.saveMemo(memo) }
        }
    }

    override suspend fun completeMemo(memo: Memo) {
        coroutineScope {
            launch { memosRemoteDataSource.completeMemo(memo) }
            launch { memosLocalDataSource.completeMemo(memo) }
        }
    }

    override suspend fun completeMemo(memoId: String) {
        withContext(ioDispatcher) {
            (getMemoWithId(memoId) as? Success)?.let {
                completeMemo(it.data)
            }
        }
    }

    override suspend fun activateMemo(memo: Memo) = withContext<Unit>(ioDispatcher) {
        coroutineScope {
            launch { memosRemoteDataSource.activateMemo(memo) }
            launch { memosLocalDataSource.activateMemo(memo) }
        }
    }

    override suspend fun activateMemo(taskId: String) {
        withContext(ioDispatcher) {
            (getMemoWithId(taskId) as? Success)?.let {
                activateMemo(it.data)
            }
        }
    }

    override suspend fun deleteAllMemos() {
        withContext(ioDispatcher) {
            coroutineScope {
                launch { memosRemoteDataSource.deleteAllMemos() }
                launch { memosLocalDataSource.deleteAllMemos() }
            }
        }
    }

    override suspend fun deleteMemo(taskId: String) {
        coroutineScope {
            launch { memosRemoteDataSource.deleteMemo(taskId) }
            launch { memosLocalDataSource.deleteMemo(taskId) }
        }
    }

    private suspend fun getMemoWithId(id: String): Result<Memo> {
        return memosLocalDataSource.getMemo(id)
    }

}
