package com.example.android.memo.data.source.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.android.memo.data.Memo
import com.example.android.memo.data.Result
import com.example.android.memo.data.Result.Error
import com.example.android.memo.data.Result.Success
import com.example.android.memo.data.source.MemosDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MemosLocalDataSource internal constructor(
    private val memosDao: MemosDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : MemosDataSource {

    override fun observeMemos(): LiveData<Result<List<Memo>>> {
        return memosDao.observeMemos().map {
            Success(it)
        }
    }

    override fun observeMemo(memoId: String): LiveData<Result<Memo>> {
        return memosDao.observeMemoById(memoId).map {
            Success(it)
        }
    }

    override suspend fun refreshMemo(memoId: String) {
        // NO-OP
    }

    override suspend fun refreshMemos() {
        // NO-OP
    }

    override suspend fun getMemos(): Result<List<Memo>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(memosDao.getMemos())
        } catch (e: Exception) {
            Error(e)
        }
    }

    override suspend fun getMemo(memoId: String): Result<Memo> = withContext(ioDispatcher) {
        try {
            val memo = memosDao.getMemoById(memoId)
            if (memo != null) {
                return@withContext Success(memo)
            } else {
                return@withContext Error(Exception("Memo not found!"))
            }
        } catch (e: Exception) {
            return@withContext Error(e)
        }
    }

    override suspend fun saveMemo(memo: Memo) = withContext(ioDispatcher) {
        memosDao.insertMemo(memo)
    }

    override suspend fun completeMemo(memo: Memo) = withContext(ioDispatcher) {
        memosDao.updateFavored(memo.id, true)
    }

    override suspend fun completeMemo(memoId: String) {
        memosDao.updateFavored(memoId, true)
    }

    override suspend fun activateMemo(memo: Memo) = withContext(ioDispatcher) {
        memosDao.updateFavored(memo.id, false)
    }

    override suspend fun activateMemo(memoId: String) {
        memosDao.updateFavored(memoId, false)
    }

    override suspend fun deleteAllMemos() = withContext(ioDispatcher) {
        memosDao.deleteMemos()
    }

    override suspend fun deleteMemo(memoId: String) = withContext<Unit>(ioDispatcher) {
        memosDao.deleteMemoById(memoId)
    }
}
