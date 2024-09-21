package com.example.android.memo.memodetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.android.memo.R
import com.example.android.memo.data.Memo
import com.example.android.memo.data.Result
import com.example.android.memo.data.Result.Success
import com.example.android.memo.data.source.MemosRepository
import kotlinx.coroutines.launch

class MemoDetailViewModel(
    private val memosRepository: MemosRepository
) : ViewModel() {

    private val _memoId = MutableLiveData<String>()

    private val _memo = _memoId.switchMap { taskId ->
        memosRepository.observeMemo(taskId).map { favoriteResult(it) }
    }
    val memo: LiveData<Memo?> = _memo

    val isDataAvailable: LiveData<Boolean> = _memo.map { it != null }

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _snackBarText = MutableLiveData<Int?>()
    val snackBarText: LiveData<Int?> = _snackBarText

    val favored: LiveData<Boolean> = _memo.map { input: Memo? ->
        input?.isFavorite?: false
    }

    suspend fun deleteMemo() {
        _memoId.value?.let {
            memosRepository.deleteMemo(it)
        }
    }

    fun setFavorite(favored: Boolean) = viewModelScope.launch {
        val task = _memo.value ?: return@launch
        if (favored) {
            memosRepository.completeMemo(task)
            showSnackbarMessage(R.string.Memo_marked_complete)
        } else {
            memosRepository.activateMemo(task)
            showSnackbarMessage(R.string.Memo_marked_active)
        }
    }

    fun start(memoId: String) {
        if (_dataLoading.value == true || memoId == _memoId.value) {
            return
        }
        _memoId.value = memoId
    }

    private fun favoriteResult(memoResult: Result<Memo>): Memo? {
        return if (memoResult is Success) {
            memoResult.data
        } else {
            showSnackbarMessage(R.string.loading_Memos_error)
            null
        }
    }

    fun refresh() {
        _memo.value?.let {
            _dataLoading.value = true
            viewModelScope.launch {
                memosRepository.refreshMemo(it.id)
                _dataLoading.value = false
            }
        }
    }

    fun snackBarMessageShown() {
        _snackBarText.value = null
    }

    private fun showSnackbarMessage(message: Int) {
        _snackBarText.value = message
    }
}
