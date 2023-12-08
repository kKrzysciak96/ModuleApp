package com.eltescode.modules.features.modules.presentation.components

import androidx.compose.foundation.layout.height
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.eltescode.modules.R


@Composable
fun IncrementationDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onClick: (Int) -> Unit,
    onResetClick: () -> Unit = {},
    dropDownMenuHeight: Dp,
    isResetAvailable: Boolean = true
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onDismissRequest() },
        modifier = Modifier.height(dropDownMenuHeight)
    ) {
        if (isResetAvailable) {
            DropdownMenuItem(onClick = {
                onResetClick()
            }, text = {
                Text(text = stringResource(id = R.string.reset))
            })
        }
        (0..50).forEach { number ->
            DropdownMenuItem(onClick = {
                onClick(number)
            }, text = {
                Text(text = number.toString())
            })
        }
    }
}