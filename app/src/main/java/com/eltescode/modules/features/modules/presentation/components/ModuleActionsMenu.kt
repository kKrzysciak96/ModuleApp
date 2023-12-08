package com.eltescode.modules.features.modules.presentation.components

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.eltescode.modules.features.modules.presentation.model.ModuleDisplayable
import com.eltescode.modules.features.modules.presentation.utils.MainScreenEvents
import com.eltescode.modules.features.modules.presentation.utils.ModuleActions
import com.maxkeppeker.sheets.core.models.base.UseCaseState


@Composable
fun ModuleActionsMenu(
    module: ModuleDisplayable,
    onDismissRequest: () -> Unit,
    onEvent: (MainScreenEvents) -> Unit,
    calendarState: UseCaseState,
    onFromDateClick: (ModuleDisplayable) -> Unit
) {
    val context = LocalContext.current
    DropdownMenu(
        expanded = module.isModuleDropdownMenuVisible,
        onDismissRequest = { onDismissRequest() }) {
        ModuleActions.values().forEach { action ->
            DropdownMenuItem(
                onClick = {
                    when (action) {
                        ModuleActions.ActionDelete -> {
                            onEvent(MainScreenEvents.OnDeleteModuleClick(module))
                        }

                        ModuleActions.ActionEditIncrementation -> {
                            onEvent(MainScreenEvents.ActionEditIncrementation(module))
                        }

                        ModuleActions.ActionAddNewIncrementation -> {
                            onEvent(MainScreenEvents.ActionAddNewIncrementation(module))
                        }

                        ModuleActions.ActionAddNewIncrementationFromDate -> {
                            onEvent(MainScreenEvents.OnModuleActionsMenuDismiss)
                            onEvent(MainScreenEvents.OnPickModuleAddNewIncrementationFromDate(module))
                            calendarState.show()
                        }

                        ModuleActions.ActionToggleSkipped -> {
                            onEvent(MainScreenEvents.ToggleSkipped(module))
                        }
                    }
                },
                text = { Text(text = action.actionName.asString(context)) })
        }
    }
}