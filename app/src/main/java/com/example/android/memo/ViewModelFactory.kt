package com.example.android.memo

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.example.android.memo.addeditmemo.AddEditMemoViewModel
import com.example.android.memo.data.source.MemosRepository
import com.example.android.memo.memodetail.MemoDetailViewModel
import com.example.android.memo.memos.MemosViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val memosRepository: MemosRepository,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ) = with(modelClass) {
        when {
            isAssignableFrom(MemoDetailViewModel::class.java) ->
                MemoDetailViewModel(memosRepository)
            isAssignableFrom(AddEditMemoViewModel::class.java) ->
                AddEditMemoViewModel(memosRepository)
            isAssignableFrom(MemosViewModel::class.java) ->
                MemosViewModel(memosRepository, handle)
            else ->
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    } as T
}
