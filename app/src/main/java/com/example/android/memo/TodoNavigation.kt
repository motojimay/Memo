package com.example.android.memo

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.android.memo.MemoDestinations.ABOUT_MARKDOWN_ROUTE
import com.example.android.memo.MemoDestinations.SETTINGS_ROUTE
import com.example.android.memo.MemoDestinationsArgs.MEMO_ID_ARG
import com.example.android.memo.MemoDestinationsArgs.TITLE_ARG
import com.example.android.memo.MemoDestinationsArgs.USER_MESSAGE_ARG
import com.example.android.memo.MemoScreens.ABOUT_MARKDOWN_SCREEN
import com.example.android.memo.MemoScreens.ADD_EDIT_MEMO_SCREEN
import com.example.android.memo.MemoScreens.MEMOS_SCREEN
import com.example.android.memo.MemoScreens.MEMO_DETAIL_SCREEN
import com.example.android.memo.MemoScreens.SETTINGS_SCREEN

private object MemoScreens {
    const val MEMOS_SCREEN = "memos"
    const val MEMO_DETAIL_SCREEN = "memo"
    const val ADD_EDIT_MEMO_SCREEN = "addEditMemo"
    const val SETTINGS_SCREEN = "settings"
    const val ABOUT_MARKDOWN_SCREEN = "aboutMarkdown"
}

object MemoDestinationsArgs {
    const val USER_MESSAGE_ARG = "userMessage"
    const val MEMO_ID_ARG = "memoId"
    const val TITLE_ARG = "title"
}

object MemoDestinations {
    const val MEMOS_ROUTE = "$MEMOS_SCREEN?$USER_MESSAGE_ARG={$USER_MESSAGE_ARG}"
    const val MEMOS_DETAIL_ROUTE = "$MEMO_DETAIL_SCREEN/{$MEMO_ID_ARG}"
    const val ADD_EDIT_MEMO_ROUTE = "$ADD_EDIT_MEMO_SCREEN/{$TITLE_ARG}?$MEMO_ID_ARG={$MEMO_ID_ARG}"
    const val SETTINGS_ROUTE = SETTINGS_SCREEN
    const val ABOUT_MARKDOWN_ROUTE = ABOUT_MARKDOWN_SCREEN
}

class MemoNavigationActions(private val navController: NavHostController) {

    fun navigateToMemos(userMessage: Int = 0) {
        val navigatesFromDrawer = userMessage == 0
        navController.navigate(
            MEMOS_SCREEN.let {
                if (userMessage != 0) "$it?$USER_MESSAGE_ARG=$userMessage" else it
            }
        ) {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = !navigatesFromDrawer
                saveState = navigatesFromDrawer
            }
            launchSingleTop = true
            restoreState = navigatesFromDrawer
        }
    }

    fun navigateToMemoDetail(memoId: String) {
        navController.navigate("$MEMO_DETAIL_SCREEN/$memoId")
    }

    fun navigateToAddMemoTask(title: Int, memoId: String?) {
        navController.navigate(
            "$ADD_EDIT_MEMO_SCREEN/$title".let {
                if (memoId != null) "$it?$MEMO_ID_ARG=$memoId" else it
            }
        )
    }

    fun navigateToSettings() {
        navController.navigate(SETTINGS_ROUTE)
    }

    fun navigateToAboutMarkdown() {
        navController.navigate(ABOUT_MARKDOWN_ROUTE)
    }
}
