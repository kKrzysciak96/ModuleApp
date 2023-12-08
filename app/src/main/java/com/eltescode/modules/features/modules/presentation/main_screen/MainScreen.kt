package com.eltescode.modules.features.modules.presentation.main_screen


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eltescode.modules.core.extensions.calculateDateUponGivenHorizontalPagerPage
import com.eltescode.modules.core.utils.UiEvent
import com.eltescode.modules.features.modules.presentation.components.AddModuleDialog
import com.eltescode.modules.features.modules.presentation.components.CardSelector
import com.eltescode.modules.features.modules.presentation.components.DayRow
import com.eltescode.modules.features.modules.presentation.model.ModuleDisplayable
import com.eltescode.modules.features.modules.presentation.utils.MainScreenEvents
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    snackBarHostState: SnackbarHostState,
    viewModel: MainScreenViewModel = hiltViewModel(),
    onNextScreen: (String) -> Unit
) {
    val state by viewModel.state
    val context = LocalContext.current
    val pagerState = rememberPagerState(
        initialPage = viewModel.lastPage,
        initialPageOffsetFraction = 0f
    ) {
        Int.MAX_VALUE
    }

    val scope = rememberCoroutineScope()

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

    LaunchedEffect(key1 = pagerState.currentPage, block = {
        viewModel.updateLastCard(pagerState.currentPage)

    })
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
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
                onNextCardClick = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) } },
                onPreviousCardClick = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) } },
                onCloseSearch = { viewModel.onEvent(MainScreenEvents.OnCloseSearchIconClick) },
                isSearchActive = state.isSearchActive,
                onSearchClick = { viewModel.onEvent(MainScreenEvents.OnSearchTextClick) },
                onSearchTextEntered = { viewModel.onEvent(MainScreenEvents.OnSearchedTextEntered(it)) },
                searchedText = state.searchedText
            )

            HorizontalPager(
                state = pagerState
            ) { page ->
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    val date = page.calculateDateUponGivenHorizontalPagerPage()
                    if (state.isSearchActive) {
                        val searchedModules =
                            state.modules.filter { it.name.contains(state.searchedText) }
                        items(searchedModules) { searchedModule ->
                            val rowDate = LocalDate.ofEpochDay(searchedModule.epochDay)
                            val epochDate = searchedModule.epochDay
                            DayRow(
                                date = rowDate,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                modules = searchedModules
                                    .filter { module -> module.epochDay == epochDate }
                                    .sortedByDescending { it.incrementation },
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
                                onIncrementationDropdownMenuDismiss = {
                                    viewModel.onEvent(
                                        MainScreenEvents.OnDropDownMenuDismiss
                                    )
                                },
                                onLongPress = {
                                    viewModel.onEvent(
                                        MainScreenEvents.OnLongModulePress(
                                            it
                                        )
                                    )
                                },
                                onEvent = viewModel::onEvent,
                                calendarState = state.calendarState,
                                onFromDateClick = { pickedModule ->
                                }
                            )
                        }
                    } else {
                        items(13) { days ->
                            val rowDate = date.plusDays(days.toLong())
                            val epochDate = rowDate.toEpochDay()
                            DayRow(
                                date = rowDate,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                modules = state.modules
                                    .filter { module -> module.epochDay == epochDate }
                                    .sortedByDescending { it.incrementation },
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
                                onIncrementationDropdownMenuDismiss = {
                                    viewModel.onEvent(
                                        MainScreenEvents.OnDropDownMenuDismiss
                                    )
                                },
                                onLongPress = {
                                    viewModel.onEvent(
                                        MainScreenEvents.OnLongModulePress(
                                            it
                                        )
                                    )
                                },
                                onEvent = viewModel::onEvent,
                                calendarState = state.calendarState,
                                onFromDateClick = { pickedModule ->
                                }
                            )
                        }
                    }

                }
            }

            CalendarDialog(
                state = state.calendarState,
                selection = CalendarSelection.Date { date ->
                    viewModel.onEvent(MainScreenEvents.OnPickDate(date))
                }
            )
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
                    onDismissRequest = {
                        viewModel.onEvent(MainScreenEvents.OnAddModuleDialogDismiss)
                    },
                    onSaveButtonClick = {
                        viewModel.onEvent(MainScreenEvents.OnSaveButtonClick(it))
                    }
                )
            }
        }
    }
}




