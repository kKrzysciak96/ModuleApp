package com.eltescode.modules.features.modules.presentation.utils

import com.eltescode.modules.features.modules.presentation.model.ModuleDisplayable
import com.maxkeppeker.sheets.core.models.base.UseCaseState

data class MainScreenState constructor(
    val modules: List<ModuleDisplayable> = emptyList(),
    val newModuleToInsert: ModuleDisplayable? = null,
    val isAddModuleDialogVisible: Boolean = false,
    val calendarState: UseCaseState,
    val currentPage: Int,
    val isSearchActive: Boolean = false,
    val searchedText: String = "",
    val searchOptions: SearchOptions,
    val isSearchOptionsSectionVisible: Boolean = false,
    val isApiRequestLoading: Boolean = false

)




