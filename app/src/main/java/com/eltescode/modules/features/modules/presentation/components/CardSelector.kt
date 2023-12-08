package com.eltescode.modules.features.modules.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.eltescode.modules.R

@Composable
fun CardSelector(
    onPreviousCardClick: () -> Unit,
    onNextCardClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSearchActive: Boolean,
    onSearchClick: () -> Unit,
    onSearchTextEntered: (String) -> Unit,
    searchedText: String,
    onCloseSearch: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (!isSearchActive) {
            IconButton(onClick = onPreviousCardClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null
                )
            }
            Text(
                text = stringResource(id = R.string.search),
                modifier = Modifier.clickable {
                    onSearchClick()
                }
            )
            IconButton(onClick = onNextCardClick) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null
                )
            }
        } else {
            TextField(
                value = searchedText,
                onValueChange = onSearchTextEntered,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            onCloseSearch()
                        })
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}