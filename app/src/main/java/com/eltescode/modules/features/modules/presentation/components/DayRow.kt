package com.eltescode.modules.features.modules.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eltescode.modules.features.modules.presentation.model.ModuleDisplayable
import com.eltescode.modules.features.modules.presentation.utils.MainScreenEvents
import java.time.LocalDate
import java.util.UUID

@Composable
fun DayRow(
    date: LocalDate,
    modifier: Modifier = Modifier,
    modules: List<ModuleDisplayable>,
    onAddButtonClick: (LocalDate) -> Unit,
    onDropdownMenuDismiss: () -> Unit,
    onIncrementationDropdownMenuDismiss: () -> Unit,
    onLongPress: (UUID) -> Unit,
    onEvent: (MainScreenEvents) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.width(100.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "${date.dayOfMonth}.${date.monthValue}.${date.year.toString().takeLast(2)}")
        }
        LazyRow(
            modifier = Modifier
                .weight(1F),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            items(modules) { module ->
                ModuleItem(
                    module = module,
                    modifier = Modifier
                        .height(IntrinsicSize.Max)
                        .width(IntrinsicSize.Max)
                        .padding(vertical = 2.dp, horizontal = 4.dp),
                    onDropdownMenuDismiss = onDropdownMenuDismiss,
                    onIncrementationDropdownMenuDismiss = onIncrementationDropdownMenuDismiss,
                    onLongPress = onLongPress,
                    onEvent = onEvent
                )
            }
        }
        IconButton(onClick = { onAddButtonClick(date) }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ModuleItem(
    module: ModuleDisplayable,
    modifier: Modifier = Modifier,
    onDropdownMenuDismiss: () -> Unit,
    onIncrementationDropdownMenuDismiss: () -> Unit,
    onLongPress: (UUID) -> Unit,
    onEvent: (MainScreenEvents) -> Unit
) {
    Box(
        modifier = modifier.combinedClickable(
            onClick = {},
            onLongClick = { onLongPress(module.id) }),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = module.incrementation.toString(), modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(2.dp), fontSize = 10.sp
        )
        Text(
            text = if (module.newIncrementation != null) {
                module.newIncrementation.toString() + "_" + module.name + " ".repeat(module.incrementation.length + 1)
            } else {
                module.name + " ".repeat(module.incrementation.length + 1)
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
        ModuleActionsMenu(
            onDismissRequest = onDropdownMenuDismiss,
            module = module,
            onEvent = onEvent
        )

        AddNewIncrementationDropdownMenu(
            module = module,
            onDismissRequest = onIncrementationDropdownMenuDismiss,
            onEvent = onEvent
        )
    }
}

