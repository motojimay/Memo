package com.example.android.memo.menu

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.example.android.memo.R
import com.example.android.memo.util.MenuAppBar

@Composable
fun SettingsScreen(
    @StringRes menuBarTitle: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            MenuAppBar(
                title = menuBarTitle,
                onBack = onBack,
            )
        },
    ) { paddingValues ->
        SettingsContent(
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun SettingsContent(modifier: Modifier) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(all = dimensionResource(id = R.dimen.horizontal_margin))
            .verticalScroll(rememberScrollState())
    ) {
        Column {
            Text(text = "This content is App settings content.\nNow, Not implemented.")
        }
    }
}
