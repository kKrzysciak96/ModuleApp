package com.eltescode.modules.features.modules.presentation.main_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eltescode.modules.R
import com.eltescode.modules.core.utils.UiEvent
import com.eltescode.modules.features.modules.presentation.components.DayRow
import com.eltescode.modules.features.modules.presentation.components.SearchOptionsSection
import com.eltescode.modules.features.modules.presentation.utils.MainScreenEvents
import com.eltescode.modules.features.modules.presentation.utils.MainScreenState
import com.eltescode.modules.features.modules.presentation.utils.SearchOptions
import com.eltescode.modules.features.modules.presentation.utils.SearchOrder
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(
    onEvent: (MainScreenEvents) -> Unit,
    state: MainScreenState,
    uiEvent: Flow<UiEvent>,
    snackBarHostState: SnackbarHostState,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = { onEvent(MainScreenEvents.OnSearchViewClose) },
                    content = {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                )
                IconButton(
                    onClick = { onEvent(MainScreenEvents.OnSearchOptionSectionToggle) },
                    content = { Icon(imageVector = Icons.Default.Sort, contentDescription = null) }
                )
            }
            AnimatedVisibility(
                visible = state.isSearchOptionsSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                SearchOptionsSection(
                    modifier = Modifier.fillMaxWidth(),
                    searchOptions = state.searchOptions,
                    onSearchOptionsChange = { onEvent(MainScreenEvents.OnSearchOptionChange(it)) })
            }
            TextField(
                value = state.searchedText,
                onValueChange = { onEvent(MainScreenEvents.OnSearchedTextEntered(it)) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = stringResource(id = R.string.enter_text_hint)) },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Gray,
                    focusedContainerColor = Color.LightGray,
                    unfocusedPlaceholderColor = Color.White
                )
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                val searchedModules = if (state.searchOptions is SearchOptions.Contains) {
                    state.modules.filter { it.name.contains(state.searchedText) }
                } else {
                    state.modules.filter { it.name == state.searchedText }
                }.run {
                    if (state.searchOptions.order is SearchOrder.Descending) {
                        sortedByDescending { it.epochDay }
                    } else {
                        sortedBy { it.epochDay }
                    }
                }
                val groupedModulesByDate = searchedModules.groupBy { it.epochDay }
                val epochKeys = groupedModulesByDate.keys.toList()
                items(epochKeys) { epochKey ->
                    val rowDate = LocalDate.ofEpochDay(epochKey)
                    DayRow(
                        date = rowDate,
                        modifier = Modifier
                            .fillMaxWidth(),
                        modules = groupedModulesByDate[epochKey]
                            ?.sortedByDescending { it.incrementation } ?: emptyList(),
                        onAddButtonClick = {
                            onEvent(MainScreenEvents.OnAddButtonClick(epochKey))
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
                        onEditIncrementation = { newModule ->
                            onEvent(
                                MainScreenEvents.OnEditIncrementation(
                                    newModule
                                )
                            )
                        },
                        onAddNewIncrementation = { newModule ->
                            onEvent(
                                MainScreenEvents.OnAddNewIncrementation(
                                    newModule
                                )
                            )
                        },
                        onResetNewIncrementation = { newModule ->
                            onEvent(
                                MainScreenEvents.OnAddNewIncrementation(
                                    newModule
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
                            onEvent(MainScreenEvents.ToggleSkipped(module))
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
}