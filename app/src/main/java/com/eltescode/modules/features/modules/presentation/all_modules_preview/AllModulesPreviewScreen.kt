package com.eltescode.modules.features.modules.presentation.all_modules_preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eltescode.modules.R
import com.eltescode.modules.features.modules.presentation.components.DeleteDialog
import com.eltescode.modules.features.modules.presentation.utils.AllModulesPreviewScreenEvents
import java.util.Locale


@Composable
fun AllModulesPreviewScreen(
    viewModel: AllModulesPreviewScreenViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val state by viewModel.state

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Row(Modifier.fillMaxWidth()) {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
                Text(
                    text = stringResource(id = R.string.all_modules_names),
                    style = MaterialTheme.typography.headlineLarge
                )
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, top = 16.dp, bottom = 60.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(state.allModules) {
                    Text(text = it, fontSize = 32.sp)
                    Divider(color = Color.LightGray)
                }
            }
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .align(Alignment.BottomCenter),
            shape = RectangleShape,
            onClick = {
                viewModel.onEvent(AllModulesPreviewScreenEvents.OnDeleteButtonClick)
            }) {
            Text(text = stringResource(id = R.string.delete_action).uppercase(Locale.ROOT))
        }

        if (state.isDeleteAllDialogVisible) {
            DeleteDialog(
                onDismiss = { viewModel.onEvent(AllModulesPreviewScreenEvents.OnDeleteDialogDismiss) },
                delete = { viewModel.onEvent(AllModulesPreviewScreenEvents.OnConfirmDeleteButtonClick) }
            )
        }
    }
}