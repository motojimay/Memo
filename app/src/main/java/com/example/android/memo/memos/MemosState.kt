package com.example.android.memo.memos

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun rememberMemosState(
    @StringRes userMessage: Int,
    viewModel: MemosViewModel,
    scaffoldState: SnackbarHostState = remember { SnackbarHostState() },
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    context: Context = LocalContext.current,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): MemosState {
    return remember(
        userMessage, viewModel, scaffoldState, lifecycleOwner, context, coroutineScope
    ) {
        MemosState(scaffoldState, coroutineScope, viewModel, userMessage, lifecycleOwner, context)
    }
}

@Stable
class MemosState(
    val scaffoldState: SnackbarHostState,
    private val coroutineScope: CoroutineScope,
    viewModel: MemosViewModel,
    @StringRes userMessage: Int,
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

        if (userMessage != 0) {
            viewModel.showEditResultMessage(userMessage)
        }
    }
}
