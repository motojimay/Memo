package com.example.android.memo.util

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.android.memo.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemosTopAppBar(
    onClickSettings: () -> Unit,
    onAboutMarkdown: () -> Unit
) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
        actions = {
            MoreMemosMenu(onClickSettings, onAboutMarkdown)
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun FilterMemosMenu(
    onFilterAllMemos: () -> Unit,
    onFilterActiveMemos: () -> Unit,
    onFilterCompletedMemos: () -> Unit
) {
    TopAppBarDropdownMenu(
        iconContent = {
            Icon(
                painterResource(id = R.drawable.ic_filter_list),
                stringResource(id = R.string.menu_filter)
            )
        }
    ) { closeMenu ->
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.nav_all)) },
            onClick = { onFilterAllMemos(); closeMenu() }
        )
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.nav_active)) },
            onClick = { onFilterActiveMemos(); closeMenu() }
        )
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.nav_completed)) },
            onClick = { onFilterCompletedMemos(); closeMenu() }
        )
    }
}

@Composable
private fun MoreMemosMenu(
    onClickSettings: () -> Unit,
    onAboutMarkdown: () -> Unit
) {
    TopAppBarDropdownMenu(
        iconContent = {
            Icon(Icons.Filled.MoreVert, stringResource(id = R.string.menu_more))
        }
    ) {
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.settings)) },
            onClick = {
                onClickSettings()
            }
        )
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.about_markdown)) },
            onClick = {
                onAboutMarkdown()
            }
        )
    }
}

@Composable
private fun TopAppBarDropdownMenu(
    iconContent: @Composable () -> Unit,
    content: @Composable ColumnScope.(() -> Unit) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
        IconButton(onClick = { expanded = !expanded }) {
            iconContent()
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.wrapContentSize(Alignment.TopEnd)
        ) {
            content { expanded = !expanded }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoDetailTopAppBar(onBack: () -> Unit, onDelete: () -> Unit) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.Memo_details))
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(id = R.string.menu_back))
            }
        },
        actions = {
            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, stringResource(id = R.string.menu_delete_Memo))
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskTopAppBar(@StringRes title: Int, onBack: () -> Unit, isChecked: Boolean, onChecked: (Boolean) -> Unit) {
    TopAppBar(
        title = { Text(text = stringResource(title)) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(id = R.string.menu_back))
            }
        },
        actions = {
            Switch(
                checked = isChecked,
                onCheckedChange = onChecked,
                modifier = Modifier
                    .padding(end = 8.dp)
            )
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuAppBar(@StringRes title: Int, onBack: () -> Unit) {
    TopAppBar(
        title = { Text(text = stringResource(title)) },
        actions = {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.Close, "Close")
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}
