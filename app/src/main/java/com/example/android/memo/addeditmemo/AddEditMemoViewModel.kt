package com.example.android.memo.addeditmemo

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.memo.R
import com.example.android.memo.data.Memo
import com.example.android.memo.data.Result.Success
import com.example.android.memo.data.source.MemosRepository
import kotlinx.coroutines.launch

class AddEditMemoViewModel(
    private val memosRepository: MemosRepository
) : ViewModel() {

    val title = MutableLiveData<String>()
    val content = MutableLiveData<TextFieldValue>()

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _snackBarText = MutableLiveData<Int?>()
    val snackBarText: LiveData<Int?> = _snackBarText

    private val _memoUpdatedEvent = MutableLiveData(false)
    val memoUpdatedEvent: LiveData<Boolean> = _memoUpdatedEvent

    private var memoId: String? = null
    private var isNewMemo: Boolean = false
    private var isDataLoaded = false
    private var isFavorite = false

    fun start(memoId: String?) {
        if (_dataLoading.value == true) {
            return
        }

        this.memoId = memoId
        if (memoId == null) {
            isNewMemo = true
            return
        }
        if (isDataLoaded) {
            return
        }

        isNewMemo = false
        _dataLoading.value = true

        viewModelScope.launch {
            memosRepository.getMemo(memoId).let { result ->
                if (result is Success) {
                    onTaskLoaded(result.data)
                } else {
                    onDataNotAvailable()
                }
            }
        }
    }

    private fun onTaskLoaded(memo: Memo) {
        title.value = memo.title
        content.value = TextFieldValue(memo.content)
        isFavorite = memo.isFavorite
        _dataLoading.value = false
        isDataLoaded = true
    }

    private fun onDataNotAvailable() {
        _dataLoading.value = false
    }

    fun saveMemo() {
        val currentTitle = title.value
        val currentContent = content.value?.text

        if (currentTitle == null || currentContent == null) {
            _snackBarText.value = R.string.empty_memo_message
            return
        }
        if (Memo(currentTitle, currentContent).isEmpty) {
            _snackBarText.value = R.string.empty_memo_message
            return
        }

        val currentTaskId = memoId
        if (isNewMemo || currentTaskId == null) {
            createMemo(Memo(currentTitle, currentContent))
        } else {
            val memo = Memo(currentTitle, currentContent, isFavorite, currentTaskId)
            updateMemo(memo)
        }
    }

    fun snackBarMessageShown() {
        _snackBarText.value = null
    }

    fun onOptionSelected(option: String) {
        val currentContent = content.value ?: TextFieldValue("")
        val newText = when (option) {
            "# " -> "# "
            "## " -> "## "
            "### " -> "### "
            "#### " -> "#### "
            "##### " -> "##### "
            "###### " -> "###### "
            "> " -> "> "
            ">> " -> ">> "
            "* *" -> "* *"
            "** **" -> "** **"
            "*** ***" -> "*** ***"
            "```\n\n```" -> "```\n\n```"
            else -> ""
        }
        val newContentText = currentContent.text + newText

        // 新しいカーソル位置を設定
        val newCursorPosition = newContentText.length
        content.value = TextFieldValue(
            text = newContentText,
            selection = TextRange(newCursorPosition)
        )
    }

    private fun createMemo(newMemo: Memo) = viewModelScope.launch {
        memosRepository.saveMemo(newMemo)
        _memoUpdatedEvent.value = true
    }

    private fun updateMemo(memo: Memo) {
        if (isNewMemo) {
            throw RuntimeException("updateMemo() was called but memo is new.")
        }
        viewModelScope.launch {
            memosRepository.saveMemo(memo)
            _memoUpdatedEvent.value = true
        }
    }
}
