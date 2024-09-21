package com.example.android.memo.memodetail

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun rememberMemoDetailState(
    memoId: String,
    viewModel: MemoDetailViewModel,
    onDeleteMemo: () -> Unit,
    scaffoldState: SnackbarHostState = remember { SnackbarHostState() },
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    context: Context = LocalContext.current,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): MemoDetailState {
    val onDeleteMemoUpdateState by rememberUpdatedState(onDeleteMemo)
    return remember(memoId, viewModel, scaffoldState, lifecycleOwner, context, coroutineScope) {
        MemoDetailState(
            scaffoldState, viewModel, coroutineScope, onDeleteMemoUpdateState, memoId,
            lifecycleOwner, context,
        )
    }
}

@Stable
class MemoDetailState(
    val scaffoldState: SnackbarHostState,
    private val viewModel: MemoDetailViewModel,
    private val coroutineScope: CoroutineScope,
    private val onDeleteMemo: () -> Unit,
    memoId: String,
    lifecycleOwner: LifecycleOwner,
    context: Context
) {
    private var currentSnackBarJob: Job? = null

    init {
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

        // Start loading data
        viewModel.start(memoId)
    }

    fun onDelete() {
        coroutineScope.launch { viewModel.deleteMemo() }
        onDeleteMemo()
    }
}
