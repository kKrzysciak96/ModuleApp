package com.eltescode.modules.features.modules.presentation.components

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.eltescode.modules.features.modules.presentation.model.ModuleDisplayable
import com.eltescode.modules.features.modules.presentation.utils.MainScreenEvents
import com.eltescode.modules.features.modules.presentation.utils.ModuleActions


@Composable
fun ModuleActionsMenu(
    module: ModuleDisplayable,
    onDismissRequest: () -> Unit,
    onEvent: (MainScreenEvents) -> Unit
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

                        ModuleActions.ActionEditName -> {
                            onEvent(MainScreenEvents.OnActionEditName(module))
                        }

                        ModuleActions.ActionEditComment -> {
                            onEvent(MainScreenEvents.ActionEditComment(module))
                        }

                        ModuleActions.ActionEditIncrementation -> {
                            onEvent(MainScreenEvents.ActionEditIncrementation(module))
                        }

                        ModuleActions.ActionAddNewIncrementation -> {
                            onEvent(MainScreenEvents.ActionAddNewIncrementation(module))
                        }
                    }
                },
                text = { Text(text = action.actionName.asString(context)) })
        }
    }
}