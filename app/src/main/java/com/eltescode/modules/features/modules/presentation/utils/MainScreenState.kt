package com.eltescode.modules.features.modules.presentation.utils

import com.eltescode.modules.features.modules.presentation.model.ModuleDisplayable
import java.time.LocalDate

data class MainScreenState(
    val modules: List<ModuleDisplayable> = emptyList(),
    val date: LocalDate,
    val newModuleToInsert: ModuleDisplayable? = null,
    val isAddModuleDialogVisible: Boolean = false,
)
