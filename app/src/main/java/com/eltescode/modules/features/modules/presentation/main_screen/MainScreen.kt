package com.eltescode.modules.features.modules.presentation.main_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eltescode.modules.core.utils.UiEvent
import com.eltescode.modules.features.modules.presentation.components.AddModuleDialog
import com.eltescode.modules.features.modules.presentation.components.CardSelector
import com.eltescode.modules.features.modules.presentation.components.DayRow
import com.eltescode.modules.features.modules.presentation.model.ModuleDisplayable
import com.eltescode.modules.features.modules.presentation.utils.MainScreenEvents
import java.util.UUID

@Composable
fun MainScreen(
    snackBarHostState: SnackbarHostState,
    viewModel: MainScreenViewModel = hiltViewModel()
) {

    val state by viewModel.state
    val context = LocalContext.current

    LaunchedEffect(
        key1 = Unit,
        block = {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is UiEvent.OnNextScreen -> {
                    }

                    is UiEvent.ShowSnackBar -> {
                        Log.d("EVENT", "DZIAŁA Show")
                        snackBarHostState.currentSnackbarData?.dismiss()
                        snackBarHostState.showSnackbar(event.message.asString(context))
                    }
                }

            }
        })
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CardSelector(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.dropDatabase() },
                onNextCardClick = { viewModel.onEvent(MainScreenEvents.OnNextClick) },
                onPreviousCardClick = { viewModel.onEvent(MainScreenEvents.OnPreviousClick) })
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                items(13) { days ->
                    val date = state.date.plusDays(days.toLong())
                    val epochDate = date.toEpochDay()
                    DayRow(
                        date = date,
                        modifier = Modifier
                            .fillMaxWidth(),
                        modules = state.modules.filter { module -> module.epochDay == epochDate },
                        onAddButtonClick = {
                            val module = ModuleDisplayable(
                                name = "",
                                comment = "",
                                incrementation = "",
                                epochDay = epochDate,
                                id = UUID.randomUUID()
                            )
                            viewModel.onEvent(MainScreenEvents.OnAddButtonClick(module))
                        },
                        onDropdownMenuDismiss = { viewModel.onEvent(MainScreenEvents.OnModuleActionsMenuDismiss) },
                        onIncrementationDropdownMenuDismiss = { viewModel.onEvent(MainScreenEvents.OnAddNewIncrementMenuDismiss) },
                        onLongPress = { viewModel.onEvent(MainScreenEvents.OnLongModulePress(it)) },
                        onEvent = viewModel::onEvent
                    )
                }

            }
        }
        if (state.isAddModuleDialogVisible) {
            state.newModuleToInsert?.let {
                AddModuleDialog(
                    module = it,
                    modifier = Modifier
                        .size(300.dp)
                        .background(Color.White),
                    onNameTextEntered = { text ->
                        viewModel.onEvent(MainScreenEvents.OnNameTextEntered(text))
                    },
                    onCommentTextEntered = { text ->
                        viewModel.onEvent(MainScreenEvents.OnCommentTextEntered(text))
                    },
                    onIncrementationTextEntered = { number ->
                        viewModel.onEvent(MainScreenEvents.OnIncrementationEntered(number))
                    },
                    onDismissRequest = { viewModel.onEvent(MainScreenEvents.OnAddModuleDialogDismiss) },
                    onSaveButtonClick = {
                        viewModel.onEvent(MainScreenEvents.OnSaveButtonClick(it))
                    }
                )
            }
        }
    }
}




