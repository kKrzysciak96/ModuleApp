package com.eltescode.modules.features.modules.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Redo
import androidx.compose.material.icons.filled.ToggleOff
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.eltescode.modules.core.ui.theme.Purple40
import com.eltescode.modules.features.modules.presentation.utils.MainScreenEvents

@Composable
fun UndoBottomBar(
    modifier: Modifier = Modifier,
    isUndoButtonEnabled: () -> Boolean,
    isRedoButtonEnabled: () -> Boolean,
    onEvent: (MainScreenEvents) -> Unit
) {
    CustomBottomAppBar(
        modifier = modifier,
        containerColor = Purple40
    ) {
        IconButton(
            enabled = isUndoButtonEnabled(),
            modifier = Modifier.weight(0.5f),
            colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White),
            onClick = {
                onEvent(MainScreenEvents.OnUndoClick)
            }) {
            Icon(imageVector = Icons.Default.Undo, contentDescription = null)
        }
        IconButton(
            modifier = Modifier.weight(0.5f),
            colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White),
            onClick = {
                onEvent(MainScreenEvents.OnToggleBottomBar)
            }) {
            Icon(
                imageVector = Icons.Default.ToggleOff,
                contentDescription = null
            )
        }
        IconButton(
            enabled = isRedoButtonEnabled(),
            modifier = Modifier.weight(0.5f),
            colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White),
            onClick = {
                onEvent(MainScreenEvents.OnRedoClick)
            }) {
            Icon(imageVector = Icons.Default.Redo, contentDescription = null)
        }
    }
}