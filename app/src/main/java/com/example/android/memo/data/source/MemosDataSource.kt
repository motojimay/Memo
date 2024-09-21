package com.example.android.memo.data.source

import androidx.lifecycle.LiveData
import com.example.android.memo.data.Memo
import com.example.android.memo.data.Result

interface MemosDataSource {

    fun observeMemos(): LiveData<Result<List<Memo>>>

    suspend fun getMemos(): Result<List<Memo>>

    suspend fun refreshMemos()

    fun observeMemo(memoId: String): LiveData<Result<Memo>>

    suspend fun getMemo(memoId: String): Result<Memo>

    suspend fun refreshMemo(memoId: String)

    suspend fun saveMemo(memo: Memo)

    suspend fun completeMemo(memo: Memo)

    suspend fun completeMemo(memoId: String)

    suspend fun activateMemo(memo: Memo)

    suspend fun activateMemo(memoId: String)

    suspend fun deleteAllMemos()

    suspend fun deleteMemo(memoId: String)
}
