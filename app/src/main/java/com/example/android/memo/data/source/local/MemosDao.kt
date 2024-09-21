package com.example.android.memo.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.android.memo.data.Memo

@Dao
interface MemosDao {

    @Query("SELECT * FROM Memos")
    fun observeMemos(): LiveData<List<Memo>>

    @Query("SELECT * FROM Memos WHERE entryid = :memoId")
    fun observeMemoById(memoId: String): LiveData<Memo>

    @Query("SELECT * FROM Memos")
    suspend fun getMemos(): List<Memo>

    @Query("SELECT * FROM Memos WHERE entryid = :memoId")
    suspend fun getMemoById(memoId: String): Memo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemo(memo: Memo)

    @Update
    suspend fun updateTask(memo: Memo): Int

    @Query("UPDATE Memos SET favorite = :favored WHERE entryid = :memoId")
    suspend fun updateFavored(memoId: String, favored: Boolean)

    @Query("DELETE FROM Memos WHERE entryid = :memoId")
    suspend fun deleteMemoById(memoId: String): Int

    @Query("DELETE FROM Memos")
    suspend fun deleteMemos()
}
