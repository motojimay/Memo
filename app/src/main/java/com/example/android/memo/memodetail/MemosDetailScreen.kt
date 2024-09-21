package com.example.android.memo.memodetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android.memo.R
import com.example.android.memo.util.MemoDetailTopAppBar
import com.example.android.memo.util.getViewModelFactory
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun MemosDetailScreen(
    memoId: String,
    onEditMemo: (String) -> Unit,
    onBack: () -> Unit,
    onDeleteMemo: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MemoDetailViewModel = viewModel(factory = getViewModelFactory()),
    state: MemoDetailState = rememberMemoDetailState(memoId, viewModel, onDeleteMemo)
) {
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = state.scaffoldState) },
        modifier = modifier.fillMaxSize(),
        topBar = {
            MemoDetailTopAppBar(onBack = onBack, onDelete = state::onDelete)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onEditMemo(memoId) }) {
                Icon(Icons.Filled.Edit, stringResource(id = R.string.edit_Memo))
            }
        }
    ) { paddingValues ->
        val loading by viewModel.dataLoading.observeAsState(initial = false)
        val dataAvailable by viewModel.isDataAvailable.observeAsState(initial = true)
        val memo by viewModel.memo.observeAsState()

       PreviewContent(
            title = memo?.title ?: "",
            content = memo?.content ?: "",
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun PreviewContent(title: String,
                           content: String,
                           modifier: Modifier) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(all = dimensionResource(id = R.dimen.horizontal_margin))
            .verticalScroll(rememberScrollState())
    ) {
        Column {
            val textFieldColors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.primary
            )
            OutlinedTextField(
                value = title,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {},
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.title_hint),
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                textStyle = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                maxLines = 1,
                colors = textFieldColors,
                readOnly = true
            )
            MarkdownText(markdown = content)
        }
    }
}
