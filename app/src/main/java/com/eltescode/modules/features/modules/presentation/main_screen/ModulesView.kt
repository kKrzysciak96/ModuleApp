package com.eltescode.modules.features.modules.presentation.main_screen

import android.util.Log
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Redo
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eltescode.modules.R
import com.eltescode.modules.core.extensions.calculateDateUponGivenHorizontalPagerPage
import com.eltescode.modules.core.ui.theme.Purple40
import com.eltescode.modules.core.utils.UiEvent
import com.eltescode.modules.features.modules.presentation.components.AddModuleDialog
import com.eltescode.modules.features.modules.presentation.components.CardSelector
import com.eltescode.modules.features.modules.presentation.components.CustomBottomAppBar
import com.eltescode.modules.features.modules.presentation.components.DayRow
import com.eltescode.modules.features.modules.presentation.components.SupaBaseDialog
import com.eltescode.modules.features.modules.presentation.utils.MainScreenEvents
import com.eltescode.modules.features.modules.presentation.utils.MainScreenState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDate


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ModulesView(
    onEvent: (MainScreenEvents) -> Unit,
    state: MainScreenState,
    uiEvent: Flow<UiEvent>,
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    pagerState: PagerState,
    dropDatabase: () -> Unit
) {
    LaunchedEffect(key1 = pagerState.currentPage, block = {
        onEvent(MainScreenEvents.OnNewCardVisible(pagerState.currentPage))

    })
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        if (state.isApiRequestLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CardSelector(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
//                        dropDatabase()
                    },
                onNextCardClick = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) } },
                onPreviousCardClick = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) } },
                onSearchClick = { onEvent(MainScreenEvents.OnSearchTextClick) },
                onShowAllTypeOfModules = { onEvent(MainScreenEvents.OnShowAllModulesPreviewIconClick) }
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.padding(bottom = 60.dp),
                flingBehavior = PagerDefaults.flingBehavior(
                    state = pagerState,
                    snapVelocityThreshold = 800.dp,
                    snapPositionalThreshold = 1f,
                    snapAnimationSpec = spring(stiffness = Spring.StiffnessMediumLow)
                ),
            ) { page ->
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    val date = page.calculateDateUponGivenHorizontalPagerPage()
                    items(13) { days ->
                        val rowDate = date.plusDays(days.toLong())
                        val epochDate = rowDate.toEpochDay()
                        DayRow(
                            date = rowDate,
                            modifier = Modifier
                                .defaultMinSize(minHeight = 80.dp)
                                .fillMaxWidth(),
                            modules = state.modules
                                .filter { module -> module.epochDay == epochDate }
                                .sortedByDescending { it.incrementation.toInt() },
                            onAddButtonClick = {

                                onEvent(MainScreenEvents.OnAddButtonClick(epochDate))
                            },
                            onDropdownMenuDismiss = { onEvent(MainScreenEvents.OnModuleActionsMenuDismiss) },
                            onAddNewIncrementationDropdownMenuDismiss = {
                                onEvent(
                                    MainScreenEvents.OnAddNewIncrementDropDownMenuDismiss
                                )
                            },
                            onEditIncrementationDropdownMenuDismiss = {
                                onEvent(
                                    MainScreenEvents.OnEditIncrementDropDownMenuDismiss
                                )
                            },
                            onLongPress = {
                                onEvent(
                                    MainScreenEvents.OnLongModulePress(
                                        it
                                    )
                                )
                            },
                            onEditIncrementation = { newModule, oldModule ->
                                onEvent(
                                    MainScreenEvents.OnEditIncrementation(
                                        newModule, oldModule
                                    )
                                )
                            },
                            onAddNewIncrementation = { newModule ->

                                val onAddNewIncrementationFromDateCond =
                                    newModule.newIncrementation != null && state.newModuleToInsert != null
                                val onAddNewIncrementCond =
                                    newModule.newIncrementation != null && state.newModuleToInsert == null

                                when {
                                    onAddNewIncrementationFromDateCond -> {
                                        onEvent(
                                            MainScreenEvents.OnAddNewIncrementationFromDate(
                                                newModule
                                            )
                                        )
                                    }

                                    onAddNewIncrementCond -> {
                                        onEvent(
                                            MainScreenEvents.OnAddNewIncrementation(newModule)
                                        )
                                    }
                                }
                            },
                            onResetNewIncrementation = { newModule, oldModule ->
                                onEvent(
                                    MainScreenEvents.OnUpdateModule(
                                        newModule, oldModule
                                    )
                                )
                            },
                            onModuleClick = { moduleId ->
                                onEvent(MainScreenEvents.OnModuleClick(moduleId))
                            },
                            onActionDeleteClick = { module ->
                                onEvent(MainScreenEvents.OnDeleteModuleClick(module))
                            },
                            onActionEditIncrementationClick = { module ->
                                onEvent(
                                    MainScreenEvents.ActionEditIncrementation(
                                        module
                                    )
                                )
                            },
                            onActionAddNewIncrementationClick = { module ->
                                onEvent(
                                    MainScreenEvents.ActionAddNewIncrementation(
                                        module
                                    )
                                )
                            },
                            onActionAddNewIncrementationFromDateClick = { module ->
                                onEvent(MainScreenEvents.OnModuleActionsMenuDismiss)
                                onEvent(
                                    MainScreenEvents.ActionAddNewIncrementationFromDate(
                                        module
                                    )
                                )
                                state.calendarState.show()
                            },
                            onActionToggleSkippedClick = { module ->
                                onEvent(MainScreenEvents.OnToggleSkipped(module))
                            }
                        )
                    }
                }
            }
        }

        CalendarDialog(
            state = state.calendarState,
            selection = CalendarSelection.Date(
                onNegativeClick = {
                    onEvent(MainScreenEvents.OnCalendarDialogDismiss)
                },
                onSelectDate = { date ->
                    onEvent(MainScreenEvents.OnPickDate(date))
                },
                selectedDate = state.newModuleToInsert?.epochDay?.let { LocalDate.ofEpochDay(it) }
            ),
            config = CalendarConfig(
                monthSelection = true,
                yearSelection = true,
            )
        )
        if (state.isAddModuleDialogVisible) {
            state.newModuleToInsert?.let {
                AddModuleDialog(
                    module = it,
                    modifier = Modifier
                        .size(300.dp)
                        .background(Color.White),
                    onNameTextEntered = { text ->
                        onEvent(MainScreenEvents.OnNameTextEntered(text))
                    },
                    onCommentTextEntered = { text ->
                        onEvent(MainScreenEvents.OnCommentTextEntered(text))
                    },
                    onIncrementationTextEntered = { number ->
                        onEvent(MainScreenEvents.OnIncrementationEntered(number))
                    },
                    onDismissRequest = {
                        onEvent(MainScreenEvents.OnAddModuleDialogDismiss)
                    },
                    onSaveButtonClick = {
                        onEvent(MainScreenEvents.OnSaveButtonClick(it))
                    }
                )
            }
        }
//        CustomBottomAppBar(
//            modifier = Modifier.align(Alignment.BottomCenter),
//            containerColor = Purple40
//        ) {
//            IconButton(
//                modifier = Modifier.weight(0.5f),
//                colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White),
//                onClick = {
//                    onEvent(MainScreenEvents.OnPushButtonClick)
//                }) {
//                Icon(imageVector = Icons.Default.Publish, contentDescription = null)
//            }
//            IconButton(
//                modifier = Modifier.weight(0.5f),
//                colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White),
//                onClick = {
//                    onEvent(MainScreenEvents.OnFetchButtonClick)
//                }) {
//                Icon(imageVector = Icons.Default.GetApp, contentDescription = null)
//            }
//        }

        CustomBottomAppBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            containerColor = Purple40
        ) {
            IconButton(
                enabled = state.undoIndex != null,
                modifier = Modifier.weight(0.5f),
                colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White),
                onClick = {
                    Log.d("UNDO Undo Click", state.undoIndex.toString())
                    onEvent(MainScreenEvents.OnUndoClick)
                }) {
                Icon(imageVector = Icons.Default.Undo, contentDescription = null)
            }
            IconButton(
                enabled = state.undoList.isNotEmpty() && state.undoIndex != state.undoList.size - 1,
                modifier = Modifier.weight(0.5f),
                colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White),
                onClick = {
                    Log.d("UNDO Redo Click", state.undoIndex.toString())
                    onEvent(MainScreenEvents.OnRedoClick)
                }) {
                Icon(imageVector = Icons.Default.Redo, contentDescription = null)
            }
        }

        if (state.isPushDataDialogVisible) {
            SupaBaseDialog(
                confirm = { onEvent(MainScreenEvents.OnConfirmPushClick) },
                onDismiss = { onEvent(MainScreenEvents.OnPushDialogDismiss) },
                question = stringResource(id = R.string.push_question),
                confirmText = stringResource(id = R.string.confirm_text),
                denyText = stringResource(id = R.string.deny_text),
            )
        }
        if (state.isFetchDataDialogVisible) {
            SupaBaseDialog(
                confirm = { onEvent(MainScreenEvents.OnConfirmFetchClick) },
                onDismiss = { onEvent(MainScreenEvents.OnFetchDialogDismiss) },
                question = stringResource(id = R.string.fetch_question),
                confirmText = stringResource(id = R.string.confirm_text),
                denyText = stringResource(id = R.string.deny_text),
            )
        }
    }
}