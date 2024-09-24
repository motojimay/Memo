package com.example.android.memo.addeditmemo

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment

enum class MenuState {
    Main, Header, Quote, Emphasis
}

@Composable
fun MarkdownEditorDialog(onDismiss: () -> Unit, onOptionSelected: (String) -> Unit) {
    var currentMenu by remember { mutableStateOf(MenuState.Main) }
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Select Menu") },
        text = {
            when (currentMenu) {
                MenuState.Main -> {
                    MainMenu(
                        onHeaderClick = { currentMenu = MenuState.Header },
                        onQuoteClick = { currentMenu = MenuState.Quote },
                        onEmphasisClick = { currentMenu = MenuState.Emphasis }
                    )
                }
                MenuState.Header -> {
                    HeaderMenu(
                        onDismiss = onDismiss,
                        onOptionSelected = onOptionSelected
                    )
                }
                MenuState.Quote -> {
                    QuoteMenu(
                        onDismiss = onDismiss,
                        onOptionSelected = onOptionSelected
                    )
                }
                MenuState.Emphasis -> {
                    EmphasisMenu(
                        onDismiss = onDismiss,
                        onOptionSelected = onOptionSelected
                    )
                }
            }
        },
        confirmButton = {
            if (MenuState.Main == currentMenu) {
                TextButton(onClick = { onDismiss() }) {
                    Text("Close")
                }
            } else {
                TextButton(onClick = { currentMenu = MenuState.Main }) {
                    Text("Back")
                }
            }
        }
    )
}

@Composable
fun MainMenu(
    onHeaderClick: () -> Unit,
    onQuoteClick: () -> Unit,
    onEmphasisClick: () -> Unit
) {
    Column {
        Button(onClick = onHeaderClick, modifier = Modifier.fillMaxWidth()) {
            Text("Header")
        }
        Button(onClick = onQuoteClick, modifier = Modifier.fillMaxWidth()) {
            Text("Quote")
        }
        Button(onClick = onEmphasisClick, modifier = Modifier.fillMaxWidth()) {
            Text("Emphasis")
        }
    }
}

@Composable
fun HeaderMenu(onDismiss: () -> Unit, onOptionSelected: (String) -> Unit) {
    Column {
        Button(
            onClick = {
                onOptionSelected("# ")
                onDismiss() },
            modifier = Modifier.fillMaxWidth()) {
            Text("h1")
        }
        Button(
            onClick = {
                onOptionSelected("## ")
                onDismiss() },
            modifier = Modifier.fillMaxWidth()) {
            Text("h2")
        }
        Button(
            onClick = {
                onOptionSelected("### ")
                onDismiss() },
            modifier = Modifier.fillMaxWidth()) {
            Text("h3")
        }
        Button(
            onClick = {
                onOptionSelected("#### ")
                onDismiss() },
            modifier = Modifier.fillMaxWidth()) {
            Text("h4")
        }
        Button(
            onClick = {
                onOptionSelected("###### ")
                onDismiss() },
            modifier = Modifier.fillMaxWidth()) {
            Text("h5")
        }
        Button(
            onClick = {
                onOptionSelected("####### ")
                onDismiss() },
            modifier = Modifier.fillMaxWidth()) {
            Text("h6")
        }
    }
}

@Composable
fun QuoteMenu(onDismiss: () -> Unit, onOptionSelected: (String) -> Unit) {
    Column {
         Button(
            onClick = {
                onOptionSelected("> ")
                onDismiss() },
            modifier = Modifier.fillMaxWidth()) {
            Text("quotation")
        }
        Button(
            onClick = {
                onOptionSelected(">> ")
                onDismiss() },
            modifier = Modifier.fillMaxWidth()) {
            Text("reply")
        }
    }
}

@Composable
fun EmphasisMenu(onDismiss: () -> Unit, onOptionSelected: (String) -> Unit) {
    Column {
        Button(
            onClick = {
                onOptionSelected("* *")
                onDismiss() },
            modifier = Modifier.fillMaxWidth()) {
            Text("italic")
        }
        Button(
            onClick = {
                onOptionSelected("** **")
                onDismiss() },
            modifier = Modifier.fillMaxWidth()) {
            Text("bold")
        }
        Button(
            onClick = {
                onOptionSelected("* *")
                onDismiss() },
            modifier = Modifier.fillMaxWidth()) {
            Text("italic & bold")
        }
    }
}

// TODO Now Not Use
@Composable
fun DialogOption(text: String, onClick: () -> Unit) {
    TextButton(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Text(text = text)
    }
}


