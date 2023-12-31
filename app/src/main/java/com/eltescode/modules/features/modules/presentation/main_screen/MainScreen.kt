package com.eltescode.modules.features.modules.presentation.main_screen


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.eltescode.modules.core.utils.UiEvent

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    snackBarHostState: SnackbarHostState,
    viewModel: MainScreenViewModel = hiltViewModel(),
    onNextScreen: (String) -> Unit
) {
    val state by viewModel.state
    val pagerState = rememberPagerState(
        initialPage = viewModel.lastPage,
        initialPageOffsetFraction = 0f
    ) {
        Int.MAX_VALUE
    }

    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    LaunchedEffect(
        key1 = Unit,
        block = {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is UiEvent.OnNextScreen -> {
                        onNextScreen(event.route)
                    }

                    is UiEvent.ShowSnackBar -> {
                        snackBarHostState.currentSnackbarData?.dismiss()
                        snackBarHostState.showSnackbar(event.message.asString(context))
                    }

                    UiEvent.OnBack -> {
                    }
                }
            }
        })

    if (state.isSearchActive) {
        SearchView(
            onEvent = viewModel::onEvent,
            state = state,
            uiEvent = viewModel.uiEvent,
            snackBarHostState = snackBarHostState,

            )
    } else {
        ModulesView(
            onEvent = viewModel::onEvent,
            state = state,
            uiEvent = viewModel.uiEvent,
            scope = scope,
            snackBarHostState = snackBarHostState,
            pagerState = pagerState,
            dropDatabase = viewModel::dropDatabase,
            isRedoButtonEnabled = viewModel::isRedoButtonEnabled,
            isUndoButtonEnabled = viewModel::isUndoButtonEnabled
        )
    }
}




