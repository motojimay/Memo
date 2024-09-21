package com.example.android.memo.memos

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.android.memo.ADD_EDIT_RESULT_OK
import com.example.android.memo.DELETE_RESULT_OK
import com.example.android.memo.EDIT_RESULT_OK
import com.example.android.memo.R
import com.example.android.memo.data.Memo
import com.example.android.memo.data.Result
import com.example.android.memo.data.Result.Success
import com.example.android.memo.data.source.MemosRepository
import com.example.android.memo.memos.MemosFilterType.ACTIVE_MEMOS
import com.example.android.memo.memos.MemosFilterType.ALL_MEMOS
import com.example.android.memo.memos.MemosFilterType.FAVORED_MEMOS
import kotlinx.coroutines.launch

class MemosViewModel(
    private val memosRepository: MemosRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _forceUpdate = MutableLiveData(false)

    private val _items: LiveData<List<Memo>> = _forceUpdate.switchMap { forceUpdate ->
        if (forceUpdate) {
            _dataLoading.value = true
            viewModelScope.launch {
                memosRepository.refreshMemos()
                _dataLoading.value = false
            }
        }
        memosRepository.observeMemos().distinctUntilChanged().switchMap { filterTasks(it) }
    }

    val items: LiveData<List<Memo>> = _items

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _currentFilteringLabel = MutableLiveData<Int>()
    val currentFilteringLabel: LiveData<Int> = _currentFilteringLabel

    private val _noMemosLabel = MutableLiveData<Int>()
    val noMemosLabel: LiveData<Int> = _noMemosLabel

    private val _noMemoIconRes = MutableLiveData<Int>()
    val noMemoIconRes: LiveData<Int> = _noMemoIconRes

    private val _memosAddViewVisible = MutableLiveData<Boolean>()
    val memosAddViewVisible: LiveData<Boolean> = _memosAddViewVisible

    private val _snackBarText = MutableLiveData<Int?>()
    val snackBarText: LiveData<Int?> = _snackBarText

    private val isDataLoadingError = MutableLiveData<Boolean>()

    init {
        setFiltering(getSavedFilterType())
        loadTasks(true)
    }

    fun setFiltering(requestType: MemosFilterType) {
        savedStateHandle[TASKS_FILTER_SAVED_STATE_KEY] = requestType

        when (requestType) {
            ALL_MEMOS -> {
                setFilter(
                    R.string.no_Memos_all,
                    R.drawable.logo_no_fill, true
                )
            }
            ACTIVE_MEMOS -> {
                setFilter(
                    R.string.no_Memos_active,
                    R.drawable.ic_check_circle_96dp, false
                )
            }
            FAVORED_MEMOS -> {
                setFilter(
                    R.string.no_Memos_completed,
                    R.drawable.ic_verified_user_96dp, false
                )
            }
        }
        // Refresh list
        loadTasks(false)
    }

    private fun setFilter(
        @StringRes noMemosLabelString: Int,
        @DrawableRes noMemoIconDrawable: Int,
        tasksAddVisible: Boolean
    ) {
        _noMemosLabel.value = noMemosLabelString
        _noMemoIconRes.value = noMemoIconDrawable
        _memosAddViewVisible.value = tasksAddVisible
    }

    fun clearCompletedTasks() {
        viewModelScope.launch {
        }
    }

    fun completeTask(memo: Memo, completed: Boolean) = viewModelScope.launch {
        if (completed) {
            memosRepository.completeMemo(memo)
            showSnackBarMessage(R.string.Memo_marked_complete)
        } else {
            memosRepository.activateMemo(memo)
            showSnackBarMessage(R.string.Memo_marked_active)
        }
    }

    fun showEditResultMessage(result: Int) {
        when (result) {
            EDIT_RESULT_OK -> showSnackBarMessage(R.string.successfully_saved_Memo_message)
            ADD_EDIT_RESULT_OK -> showSnackBarMessage(R.string.successfully_added_Memo_message)
            DELETE_RESULT_OK -> showSnackBarMessage(R.string.successfully_deleted_Memo_message)
        }
    }

    fun snackBarMessageShown() {
        _snackBarText.value = null
    }

    private fun showSnackBarMessage(message: Int) {
        _snackBarText.value = message
    }

    private fun filterTasks(memosResult: Result<List<Memo>>): LiveData<List<Memo>> {
        val result = MutableLiveData<List<Memo>>()

        if (memosResult is Success) {
            isDataLoadingError.value = false
            viewModelScope.launch {
                result.value = filterItems(memosResult.data, getSavedFilterType())
            }
        } else {
            result.value = emptyList()
            showSnackBarMessage(R.string.loading_Memos_error)
            isDataLoadingError.value = true
        }

        return result
    }

    private fun loadTasks(forceUpdate: Boolean) {
        _forceUpdate.value = forceUpdate
    }

    private fun filterItems(memos: List<Memo>, filteringType: MemosFilterType): List<Memo> {
        val tasksToShow = ArrayList<Memo>()
        for (task in memos) {
            when (filteringType) {
                ALL_MEMOS -> tasksToShow.add(task)
                ACTIVE_MEMOS -> if (task.isActive) {
                    tasksToShow.add(task)
                }
                FAVORED_MEMOS -> if (task.isFavorite) {
                    tasksToShow.add(task)
                }
            }
        }
        return tasksToShow
    }

    fun refresh() {
        _forceUpdate.value = true
    }

    private fun getSavedFilterType(): MemosFilterType {
        return savedStateHandle.get(TASKS_FILTER_SAVED_STATE_KEY) ?: ALL_MEMOS
    }
}

const val TASKS_FILTER_SAVED_STATE_KEY = "TASKS_FILTER_SAVED_STATE_KEY"
