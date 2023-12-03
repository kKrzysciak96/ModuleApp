package com.eltescode.modules.features.modules.presentation.components

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.eltescode.modules.R
import com.eltescode.modules.features.modules.presentation.model.ModuleDisplayable
import com.eltescode.modules.features.modules.presentation.utils.MainScreenEvents


@Composable
fun AddNewIncrementationDropdownMenu(
    module: ModuleDisplayable,
    onDismissRequest: () -> Unit,
    onEvent: (MainScreenEvents) -> Unit
) {
    DropdownMenu(
        expanded = module.isAddNewIncrementDropdownMenuVisible,
        onDismissRequest = { onDismissRequest() }) {
        DropdownMenuItem(
            onClick = {
                module.newIncrementation?.let {
                    val newModule = module.copy(newIncrementation = null)
                    onEvent(MainScreenEvents.OnAddNewIncrementation(newModule))
                }
            },
            text = { Text(text = stringResource(id = R.string.reset)) })
        (0..25).forEach { number ->
            DropdownMenuItem(
                onClick = {
                    val newModule = module.copy(newIncrementation = number)
                    onEvent(MainScreenEvents.OnAddNewIncrementation(newModule))
                },
                text = { Text(text = number.toString()) })
        }
    }
}