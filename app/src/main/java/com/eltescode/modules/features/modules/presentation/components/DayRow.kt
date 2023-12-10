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
    onAddNewIncrementationDropdownMenuDismiss: () -> Unit,
    onEditIncrementationDropdownMenuDismiss: () -> Unit,
    onLongPress: (UUID) -> Unit,
    onEditIncrementation: (ModuleDisplayable) -> Unit,
    onAddNewIncrementation: (ModuleDisplayable) -> Unit,
    onResetNewIncrementation: (ModuleDisplayable) -> Unit,
    onModuleClick: (UUID) -> Unit,
    onActionDeleteClick: (ModuleDisplayable) -> Unit,
    onActionEditIncrementationClick: (ModuleDisplayable) -> Unit,
    onActionAddNewIncrementationClick: (ModuleDisplayable) -> Unit,
    onActionAddNewIncrementationFromDateClick: (ModuleDisplayable) -> Unit,
    onActionToggleSkippedClick: (ModuleDisplayable) -> Unit,
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.width(100.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${date.dayOfMonth}.${date.monthValue}.${
                        date.year.toString().takeLast(2)
                    }",
                    fontSize = 22.sp
                )
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
                        onAddNewIncrementationDropdownMenuDismiss = onAddNewIncrementationDropdownMenuDismiss,
                        onEditIncrementationDropdownMenuDismiss = onEditIncrementationDropdownMenuDismiss,
                        onLongPress = onLongPress,
                        onEditIncrementation = onEditIncrementation,
                        onAddNewIncrementation = onAddNewIncrementation,
                        onResetNewIncrementation = onResetNewIncrementation,
                        onModuleClick = onModuleClick,
                        onActionDeleteClick = onActionDeleteClick,
                        onActionEditIncrementationClick = onActionEditIncrementationClick,
                        onActionAddNewIncrementationClick = onActionAddNewIncrementationClick,
                        onActionAddNewIncrementationFromDateClick = onActionAddNewIncrementationFromDateClick,
                        onActionToggleSkippedClick = onActionToggleSkippedClick,
                    )
                }
            }
            IconButton(onClick = { onAddButtonClick(date) }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }

        Divider(
            modifier = Modifier.padding(horizontal = 16.dp),
            thickness = 2.dp,
            color = Color.LightGray.copy(alpha = 0.5f)
        )
    }

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ModuleItem(
    module: ModuleDisplayable,
    modifier: Modifier = Modifier,
    onDropdownMenuDismiss: () -> Unit,
    onAddNewIncrementationDropdownMenuDismiss: () -> Unit,
    onEditIncrementationDropdownMenuDismiss: () -> Unit,
    onEditIncrementation: (ModuleDisplayable) -> Unit,
    onAddNewIncrementation: (ModuleDisplayable) -> Unit,
    onResetNewIncrementation: (ModuleDisplayable) -> Unit,
    onModuleClick: (UUID) -> Unit,
    onLongPress: (UUID) -> Unit,
    onActionDeleteClick: (ModuleDisplayable) -> Unit,
    onActionEditIncrementationClick: (ModuleDisplayable) -> Unit,
    onActionAddNewIncrementationClick: (ModuleDisplayable) -> Unit,
    onActionAddNewIncrementationFromDateClick: (ModuleDisplayable) -> Unit,
    onActionToggleSkippedClick: (ModuleDisplayable) -> Unit,
) {
    val height: Dp = (LocalConfiguration.current.screenHeightDp / 2).dp
    Box(
        modifier = modifier.combinedClickable(
            onClick = { onModuleClick(module.id) },
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
            onActionDeleteClick = onActionDeleteClick,
            onActionEditIncrementationClick = onActionEditIncrementationClick,
            onActionAddNewIncrementationClick = onActionAddNewIncrementationClick,
            onActionAddNewIncrementationFromDateClick = onActionAddNewIncrementationFromDateClick,
            onActionToggleSkippedClick = onActionToggleSkippedClick,
        )

        IncrementationDropdownMenu(
            modifier = Modifier.height(height),
            expanded = module.isAddNewIncrementDropdownMenuVisible,
            onDismissRequest = onAddNewIncrementationDropdownMenuDismiss,
            onClick = { number ->
                val newModule = module.copy(newIncrementation = number)
                onAddNewIncrementation(newModule)
            },
            isResetAvailable = true,
            onResetClick = {
                module.newIncrementation?.let {
                    val newModule = module.copy(newIncrementation = null)
                    onResetNewIncrementation(newModule)

                }
            }
        )

        IncrementationDropdownMenu(
            modifier = Modifier.height(height),
            expanded = module.isEditIncrementDropdownMenuVisible,
            onDismissRequest = onEditIncrementationDropdownMenuDismiss,

            onClick = { number ->
                val newModule = module.copy(incrementation = number.toString())
                onEditIncrementation(newModule)
            },
            isResetAvailable = false
        )
    }
}

