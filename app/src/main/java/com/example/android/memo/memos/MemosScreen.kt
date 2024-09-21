package com.example.android.memo.memos

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android.memo.R
import com.example.android.memo.data.Memo
import com.example.android.memo.util.LoadingContent
import com.example.android.memo.util.MemosTopAppBar
import com.example.android.memo.util.getViewModelFactory

@Composable
fun MemosScreen(
    @StringRes userMessage: Int,
    onAddMemo: () -> Unit,
    onMemoClick: (Memo) -> Unit,
    onClickSettings: () -> Unit,
    onAboutMarkdown: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MemosViewModel = viewModel(factory = getViewModelFactory()),
    scaffoldState: SnackbarHostState = remember { SnackbarHostState() },
    state: MemosState = rememberMemosState(userMessage, viewModel, scaffoldState)
) {
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = state.scaffoldState) },
        topBar = {
            MemosTopAppBar(
                onClickSettings = onClickSettings,
                onAboutMarkdown = onAboutMarkdown
            )
        },
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = onAddMemo) {
                Icon(Icons.Filled.Add, stringResource(id = R.string.add_Memo))
            }
        }
    ) { paddingValues ->
        val loading by viewModel.dataLoading.observeAsState(initial = false)
        val items by viewModel.items.observeAsState(initial = emptyList())
        val noMemosLabel by viewModel.noMemosLabel.observeAsState(initial = R.string.no_Memos_all)
        val noMemosIconRes by viewModel.noMemoIconRes.observeAsState(R.drawable.logo_no_fill)

        MemosContent(
            loading = loading,
            memos = items,
            noMemosLabel = noMemosLabel,
            noMemosIconRes = noMemosIconRes,
            onRefresh = viewModel::refresh,
            onMemoClick = onMemoClick,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun MemosContent(
    loading: Boolean,
    memos: List<Memo>,
    @StringRes noMemosLabel: Int,
    @DrawableRes noMemosIconRes: Int,
    onRefresh: () -> Unit,
    onMemoClick: (Memo) -> Unit,
    modifier: Modifier = Modifier
) {
    LoadingContent(
        loading = loading,
        empty = memos.isEmpty(),
        emptyContent = { MemosEmptyContent(noMemosLabel, noMemosIconRes, modifier) },
        onRefresh = onRefresh
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(id = R.dimen.horizontal_margin))
        ) {
            LazyColumn {
                items(memos) { memo ->
                    MemoItem(
                        memo = memo,
                        onMemoClick = onMemoClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun MemoItem(
    memo: Memo,
    onMemoClick: (Memo) -> Unit
) {
    OutlinedCard(
        Modifier
            .fillMaxWidth()
            .clickable{ onMemoClick(memo) },
        border = BorderStroke(1.dp, Color(red = 53, green = 74, blue = 123)),
    ) {
        Column(
            modifier = Modifier.padding(start = 4.dp, end = 4.dp, top = 16.dp, bottom = 16.dp)
        ) {
            Text(
                text = memo.titleForList,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(
                    start = dimensionResource(id = R.dimen.horizontal_margin)
                ),
                textDecoration = if (memo.isFavorite) {
                    TextDecoration.LineThrough
                } else {
                    null
                }
            )
        }
    }
}

@Composable
private fun MemosEmptyContent(
    @StringRes noMemosLabel: Int,
    @DrawableRes noMemosIconRes: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = noMemosIconRes),
            contentDescription = stringResource(R.string.no_Memos_image_content_description),
            modifier = Modifier.size(96.dp)
        )
        Text(stringResource(id = noMemosLabel))
    }
}
