package com.eltescode.modules.features.modules.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.eltescode.modules.core.ui.theme.Purple40
import com.eltescode.modules.features.modules.presentation.utils.MainScreenEvents

@Composable
fun SyncBottomBar(modifier: Modifier = Modifier, onEvent: (MainScreenEvents) -> Unit) {
    CustomBottomAppBar(
        modifier = modifier,
        containerColor = Purple40
    ) {

        IconButton(
            modifier = Modifier.weight(0.5f),
            colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White),
            onClick = {
                onEvent(MainScreenEvents.OnPushButtonClick)

            }) {
            Icon(imageVector = Icons.Default.Upload, contentDescription = null)
        }
        IconButton(
            modifier = Modifier.weight(0.5f),
            colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White),
            onClick = {
                onEvent(MainScreenEvents.OnToggleBottomBar)
            }) {
            Icon(
                imageVector = Icons.Default.ToggleOn,
                contentDescription = null
            )
        }
        IconButton(
            modifier = Modifier.weight(0.5f),
            colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White),
            onClick = {
                onEvent(MainScreenEvents.OnFetchButtonClick)
            }) {
            Icon(imageVector = Icons.Default.Download, contentDescription = null)
        }

    }
}