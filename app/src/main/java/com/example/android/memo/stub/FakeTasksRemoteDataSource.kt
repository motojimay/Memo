package com.example.android.memo.stub

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.android.memo.data.Memo
import com.example.android.memo.data.Result
import com.example.android.memo.data.Result.Error
import com.example.android.memo.data.Result.Success
import com.example.android.memo.data.source.MemosDataSource

/**
 * Implementation of a remote data source with static access to the data for easy testing.
 */
object FakeTasksRemoteDataSource : MemosDataSource {

    private var MEMOS_SERVICE_DATA: LinkedHashMap<String, Memo> = LinkedHashMap()

    private val observableTasks = MutableLiveData<Result<List<Memo>>>()

    override suspend fun refreshMemos() {
        observableTasks.postValue(getMemos())
    }

    override suspend fun refreshMemo(taskId: String) {
        refreshMemos()
    }

    override fun observeMemos(): LiveData<Result<List<Memo>>> {
        return observableTasks
    }


    override suspend fun deleteAllMemos() {
    }

    override fun observeMemo(taskId: String): LiveData<Result<Memo>> {
        return observableTasks.map { tasks ->
            when (tasks) {
                is Result.Loading -> Result.Loading
                is Error -> Error(tasks.exception)
                is Success -> {
                    val task = tasks.data.firstOrNull() { it.id == taskId }
                        ?: return@map Error(Exception("Not found"))
                    Success(task)
                }
            }
        }
    }

    override suspend fun getMemo(taskId: String): Result<Memo> {
        MEMOS_SERVICE_DATA[taskId]?.let {
            return Success(it)
        }
        return Error(Exception("Could not find task"))
    }

    override suspend fun getMemos(): Result<List<Memo>> {
        return Success(MEMOS_SERVICE_DATA.values.toList())
    }

    override suspend fun saveMemo(task: Memo) {
        MEMOS_SERVICE_DATA[task.id] = task
    }

    override suspend fun completeMemo(task: Memo) {
        val completedTask = Memo(task.title, task.content, true, task.id)
        MEMOS_SERVICE_DATA[task.id] = completedTask
    }

    override suspend fun completeMemo(taskId: String) {
        // Not required for the remote data source.
    }

    override suspend fun activateMemo(task: Memo) {
        val activeTask = Memo(task.title, task.content, false, task.id)
        MEMOS_SERVICE_DATA[task.id] = activeTask
    }

    override suspend fun activateMemo(taskId: String) {
        // Not required for the remote data source.
    }

    override suspend fun deleteMemo(taskId: String) {
        MEMOS_SERVICE_DATA.remove(taskId)
        refreshMemos()
    }

}