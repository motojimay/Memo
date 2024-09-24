package com.example.android.memo.addeditmemo

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun rememberAddEditTaskState(
    memoId: String?,
    viewModel: AddEditMemoViewModel,
    onMemoUpdate: () -> Unit,
    scaffoldState: SnackbarHostState = remember { SnackbarHostState() },
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    context: Context = LocalContext.current,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): AddEditMemoState {
    val currentOnTaskUpdateState by rememberUpdatedState(onMemoUpdate)
    return remember(memoId, viewModel, scaffoldState, lifecycleOwner, context, coroutineScope) {
        AddEditMemoState(
            scaffoldState, viewModel, memoId, currentOnTaskUpdateState,
            lifecycleOwner, context, coroutineScope
        )
    }
}

@Stable
class AddEditMemoState(
    val scaffoldState: SnackbarHostState,
    private val viewModel: AddEditMemoViewModel,
    taskId: String?,
    onTaskUpdate: () -> Unit,
    lifecycleOwner: LifecycleOwner,
    context: Context,
    coroutineScope: CoroutineScope
) {
    private var currentSnackBarJob: Job? = null

    init {
        viewModel.memoUpdatedEvent.observe(lifecycleOwner) { taskUpdated ->
            if (taskUpdated) onTaskUpdate()
        }

        viewModel.snackBarText.observe(lifecycleOwner) { snackBarMessage ->
            if (snackBarMessage != null) {
                currentSnackBarJob?.cancel()
                val snackBarText = context.getString(snackBarMessage)
                currentSnackBarJob = coroutineScope.launch {
                    scaffoldState.showSnackbar(snackBarText)
                    viewModel.snackBarMessageShown()
                }
            }
        }

        viewModel.start(taskId)
    }

    fun onFabClick() {
        viewModel.saveMemo()
    }

    fun onTitleChanged(newTitle: String) {
        viewModel.title.value = newTitle
    }

    fun onContentChanged(newContent: TextFieldValue) {
        viewModel.content.value = newContent
    }
}
