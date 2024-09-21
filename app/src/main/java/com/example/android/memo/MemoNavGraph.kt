package com.example.android.memo

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.android.memo.MemoDestinationsArgs.MEMO_ID_ARG
import com.example.android.memo.MemoDestinationsArgs.TITLE_ARG
import com.example.android.memo.MemoDestinationsArgs.USER_MESSAGE_ARG
import com.example.android.memo.addeditmemo.AddEditMemoScreen
import com.example.android.memo.memodetail.MemosDetailScreen
import com.example.android.memo.memos.MemosScreen
import com.example.android.memo.menu.AboutMarkdownScreen
import com.example.android.memo.menu.SettingsScreen

@Composable
fun MemoNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = MemoDestinations.MEMOS_ROUTE,
    navActions: MemoNavigationActions = remember(navController) {
        MemoNavigationActions(navController)
    }
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(
            MemoDestinations.MEMOS_ROUTE,
            arguments = listOf(
                navArgument(USER_MESSAGE_ARG) { type = NavType.IntType; defaultValue = 0 }
            )
        ) { entry ->
            MemosScreen(
                userMessage = entry.arguments?.getInt(USER_MESSAGE_ARG)!!,
                onAddMemo = { navActions.navigateToAddMemoTask(R.string.add_Memo, null) },
                onMemoClick = { memo -> navActions.navigateToMemoDetail(memo.id) },
                onClickSettings = { navActions.navigateToSettings() },
                onAboutMarkdown = { navActions.navigateToAboutMarkdown() }
            )
        }
        composable(
            MemoDestinations.ADD_EDIT_MEMO_ROUTE,
            arguments = listOf(
                navArgument(TITLE_ARG) { type = NavType.IntType },
                navArgument(MEMO_ID_ARG) { type = NavType.StringType; nullable = true },
            )
        ) { entry ->
            val memoId = entry.arguments?.getString(MEMO_ID_ARG)
            AddEditMemoScreen(
                topBarTitle = entry.arguments?.getInt(TITLE_ARG)!!,
                memoId = memoId,
                onMemoUpdate = {
                    navActions.navigateToMemos(
                        if (memoId == null) ADD_EDIT_RESULT_OK else EDIT_RESULT_OK
                    )
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(MemoDestinations.MEMOS_DETAIL_ROUTE) { entry ->
            MemosDetailScreen(
                memoId = entry.arguments?.getString(MEMO_ID_ARG)!!,
                onEditMemo = { memoId ->
                    navActions.navigateToAddMemoTask(R.string.edit_Memo, memoId)
                },
                onBack = { navController.popBackStack() },
                onDeleteMemo = { navActions.navigateToMemos(DELETE_RESULT_OK) }
            )
        }
        composable(MemoDestinations.SETTINGS_ROUTE) { entry ->
            SettingsScreen(
                menuBarTitle = R.string.settings_title,
                onBack = { navController.popBackStack() },
            )
        }
        composable(MemoDestinations.ABOUT_MARKDOWN_ROUTE) {entry ->
            AboutMarkdownScreen(
                menuBarTitle = R.string.about_markdown_title,
                onBack = { navController.popBackStack() },
            )
        }
    }
}

// Keys for navigation
const val ADD_EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 1
const val DELETE_RESULT_OK = Activity.RESULT_FIRST_USER + 2
const val EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 3