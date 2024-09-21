package com.example.android.memo.addeditmemo

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android.memo.R
import com.example.android.memo.util.AddEditTaskTopAppBar
import com.example.android.memo.util.getViewModelFactory
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun AddEditMemoScreen(
    @StringRes topBarTitle: Int,
    memoId: String?,
    onMemoUpdate: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddEditMemoViewModel = viewModel(factory = getViewModelFactory()),
    state: AddEditMemoState = rememberAddEditTaskState(memoId, viewModel, onMemoUpdate)
) {
    var isChecked by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = state.scaffoldState) },
        topBar = {
            AddEditTaskTopAppBar(
                title = topBarTitle,
                onBack = onBack,
                isChecked = isChecked,
                onChecked = { isChecked = it }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = state::onFabClick) {
                Icon(Icons.Filled.Done, stringResource(id = R.string.cd_save_Memo))
            }
        }
    ) { paddingValues ->
        val title by viewModel.title.observeAsState(initial = "")
        val content by viewModel.content.observeAsState(initial = "")

        if (isChecked) {
            PreviewContent(
                title = title,
                content = if (content.equals("")) "# Content" else content,
                onTitleChanged = state::onTitleChanged,
                modifier = Modifier.padding(paddingValues)
            )
        } else {
            AddEditMemoContent(
                title = title,
                content = content,
                onTitleChanged = state::onTitleChanged,
                onContentChanged = state::onContentChanged,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
private fun PreviewContent(
    title: String,
    content: String,
    onTitleChanged: (String) -> Unit,
    modifier: Modifier
) {
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
                onValueChange = onTitleChanged,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.title_hint),
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                textStyle = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                maxLines = 1,
                colors = textFieldColors
            )
            MarkdownText(markdown = content)
        }
    }
}

@Composable
private fun AddEditMemoContent(
    title: String,
    content: String,
    onTitleChanged: (String) -> Unit,
    onContentChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(all = dimensionResource(id = R.dimen.horizontal_margin))
            .verticalScroll(rememberScrollState())
    ) {
        val textFieldColors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.primary
        )
        OutlinedTextField(
            value = title,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = onTitleChanged,
            placeholder = {
                Text(
                    text = stringResource(id = R.string.title_hint),
                    style = MaterialTheme.typography.headlineSmall
                )
            },
            textStyle = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            maxLines = 1,
            colors = textFieldColors
        )

        OutlinedTextField(
            value = content,
            onValueChange = onContentChanged,
            placeholder = { Text(stringResource(id = R.string.content_hint)) },
            modifier = Modifier
                .height(350.dp)
                .fillMaxWidth(),
            colors = textFieldColors
        )
    }
}
