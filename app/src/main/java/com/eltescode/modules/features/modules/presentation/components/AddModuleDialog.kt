package com.eltescode.modules.features.modules.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.eltescode.modules.R
import com.eltescode.modules.features.modules.presentation.model.ModuleDisplayable

@Composable
fun AddModuleDialog(
    modifier: Modifier = Modifier,
    module: ModuleDisplayable,
    onDismissRequest: () -> Unit,
    onSaveButtonClick: () -> Unit,
    onNameTextEntered: (String) -> Unit,
    onCommentTextEntered: (String) -> Unit,
    onIncrementationTextEntered: (String) -> Unit,

    ) {
    Dialog(onDismissRequest = onDismissRequest) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            CustomIcon(
                imageVector = Icons.Default.Save,
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = onSaveButtonClick
            )
            CustomIcon(
                imageVector = Icons.Default.Close,
                modifier = Modifier.align(Alignment.TopStart),
                onClick = onDismissRequest
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                CustomModuleDialogRow(
                    label = stringResource(id = R.string.name),
                    text = module.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp, start = 16.dp, end = 16.dp),
                    onValueChange = onNameTextEntered,
                    placeholderText = stringResource(id = R.string.enter_name_hint)
                )
                CustomModuleDialogRow(
                    label = stringResource(id = R.string.comment),
                    text = module.comment,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp, start = 16.dp, end = 16.dp),
                    onValueChange = onCommentTextEntered,
                    placeholderText = stringResource(id = R.string.enter_comment_hint)
                )
                CustomModuleDialogRow(
                    label = stringResource(id = R.string.incrementation),
                    text = module.incrementation.toString(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp, start = 16.dp, end = 16.dp),
                    onValueChange = onIncrementationTextEntered,
                    placeholderText = stringResource(id = R.string.enter_incrementation_hint),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
            }
        }
    }
}


