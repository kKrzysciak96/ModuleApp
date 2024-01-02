package com.eltescode.modules.features.modules.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun ModuleNameTextRow(
    modifier: Modifier = Modifier,
    singleLine: Boolean = false,
    label: String,
    text: String,
    isIconEnabled: Boolean,
    onValueChange: (String) -> Unit,
    onIconClick: () -> Unit
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "$label:")

        if (isIconEnabled) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = null,
                modifier = Modifier.clickable { onIconClick() })
        } else {
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = null,
                modifier = Modifier.clickable { onIconClick() })
        }
    }
    if (isIconEnabled) {
        TextField(
            value = text,
            onValueChange = onValueChange,
            singleLine = singleLine
        )
    } else {
        Text(text = text)
    }
}