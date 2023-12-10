package com.eltescode.modules.features.modules.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eltescode.modules.R
import com.eltescode.modules.features.modules.presentation.utils.SearchOptions
import com.eltescode.modules.features.modules.presentation.utils.SearchOrder

@Composable
fun SearchOptionsSection(
    modifier: Modifier = Modifier,
    searchOptions: SearchOptions,
    onSearchOptionsChange: (SearchOptions) -> Unit
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            DefaultRadioButton(
                text = stringResource(id = R.string.exact_search),
                selected = searchOptions is SearchOptions.Exact,
                onSelect = { onSearchOptionsChange(SearchOptions.Exact(searchOptions.order)) }
            )

            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = stringResource(id = R.string.contains_search),
                selected = searchOptions is SearchOptions.Contains,
                onSelect = { onSearchOptionsChange(SearchOptions.Contains(searchOptions.order)) }
            )

        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            DefaultRadioButton(
                text = stringResource(id = R.string.ascending_search),
                selected = searchOptions.order is SearchOrder.Ascending,
                onSelect = { onSearchOptionsChange(searchOptions.copy(SearchOrder.Ascending)) })
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = stringResource(id = R.string.descending_search),
                selected = searchOptions.order is SearchOrder.Descending,
                onSelect = { onSearchOptionsChange(searchOptions.copy(SearchOrder.Descending)) })
        }
    }
}