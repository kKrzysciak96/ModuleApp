package com.eltescode.modules.features.modules.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eltescode.modules.features.modules.presentation.model.ModuleDisplayable
import com.eltescode.modules.features.modules.presentation.utils.MainScreenEvents
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import java.time.LocalDate
import java.util.UUID

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DayRow(
    date: LocalDate,
    modifier: Modifier = Modifier,
    modules: List<ModuleDisplayable>,
    onAddButtonClick: (LocalDate) -> Unit,
    onDropdownMenuDismiss: () -> Unit,
    onIncrementationDropdownMenuDismiss: () -> Unit,
    onLongPress: (UUID) -> Unit,
    onEvent: (MainScreenEvents) -> Unit,
    calendarState: UseCaseState,
    onFromDateClick: (ModuleDisplayable) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.width(100.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "${date.dayOfMonth}.${date.monthValue}.${date.year.toString().takeLast(2)}")
        }
        FlowRow(
            modifier = Modifier
                .weight(1F),
        ) {
            modules.forEach {
                ModuleItem(
                    module = it,
                    modifier = Modifier
                        .height(IntrinsicSize.Max)
                        .width(IntrinsicSize.Max)
                        .padding(vertical = 2.dp, horizontal = 4.dp),
                    onDropdownMenuDismiss = onDropdownMenuDismiss,
                    onIncrementationDropdownMenuDismiss = onIncrementationDropdownMenuDismiss,
                    onLongPress = onLongPress,
                    onEvent = onEvent,
                    calendarState = calendarState,
                    onFromDateClick = onFromDateClick
                )
            }
        }
        IconButton(onClick = { onAddButtonClick(date) }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ModuleItem(
    module: ModuleDisplayable,
    modifier: Modifier = Modifier,
    onDropdownMenuDismiss: () -> Unit,
    onIncrementationDropdownMenuDismiss: () -> Unit,
    onLongPress: (UUID) -> Unit,
    onEvent: (MainScreenEvents) -> Unit,
    calendarState: UseCaseState,
    onFromDateClick: (ModuleDisplayable) -> Unit
) {
    val height: Dp = (LocalConfiguration.current.screenHeightDp / 2).dp
    Box(
        modifier = modifier.combinedClickable(
            onClick = { onEvent(MainScreenEvents.OnModuleClick(module.id)) },
            onLongClick = { onLongPress(module.id) }),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = buildAnnotatedString {
                append(module.prepareDescriptionText())
                withStyle(
                    SpanStyle(
                        baselineShift = BaselineShift.Superscript,
                        fontSize = 10.sp,
                    )
                ) {
                    append(module.incrementation)
                }
            },
            fontSize = 20.sp
        )

        if (module.newIncrementation != null) {
            Divider(
                modifier = Modifier
                    .fillMaxWidth(),
                thickness = 2.dp,
                color = Color.Black
            )
        }
        if (module.isSkipped) {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    thickness = 2.dp,
                    color = Color.Black
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    thickness = 2.dp,
                    color = Color.Black
                )
            }
        }
        ModuleActionsMenu(
            onDismissRequest = onDropdownMenuDismiss,
            module = module,
            onEvent = onEvent,
            calendarState = calendarState,
            onFromDateClick = onFromDateClick
        )

        IncrementationDropdownMenu(
            expanded = module.isAddNewIncrementDropdownMenuVisible,
            onDismissRequest = onIncrementationDropdownMenuDismiss,
            dropDownMenuHeight = height,
            onClick = { number ->
                val newModule = module.copy(newIncrementation = number)
                onEvent(MainScreenEvents.OnAddNewIncrementation(newModule))
            },
            onResetClick = {
                module.newIncrementation?.let {
                    val newModule = module.copy(newIncrementation = null)
                    onEvent(MainScreenEvents.OnAddNewIncrementation(newModule))
                }
            }
        )

        IncrementationDropdownMenu(
            expanded = module.isEditIncrementDropdownMenuVisible,
            onDismissRequest = onIncrementationDropdownMenuDismiss,
            dropDownMenuHeight = height,
            onClick = { number ->
                val newModule = module.copy(incrementation = number.toString())
                onEvent(MainScreenEvents.OnEditIncrementation(newModule))
            },
            isResetAvailable = false
        )
    }
}

