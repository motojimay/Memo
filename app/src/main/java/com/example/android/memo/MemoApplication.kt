package com.example.android.memo

import android.app.Application
import com.example.android.memo.data.source.MemosRepository
import com.example.android.memo.stub.ServiceLocator

class MemoApplication : Application() {

    val taskRepository: MemosRepository
        get() = ServiceLocator.provideTasksRepository(this)
}
