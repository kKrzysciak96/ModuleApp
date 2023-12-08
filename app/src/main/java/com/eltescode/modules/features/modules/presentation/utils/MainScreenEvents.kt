package com.eltescode.modules.features.modules.presentation.utils

import com.eltescode.modules.features.modules.presentation.model.ModuleDisplayable
import java.time.LocalDate
import java.util.UUID

sealed interface MainScreenEvents {
    object OnAddModuleDialogDismiss : MainScreenEvents
    data class OnLongModulePress(val moduleId: UUID) : MainScreenEvents
    object OnModuleActionsMenuDismiss : MainScreenEvents
    data class OnAddButtonClick(val module: ModuleDisplayable) : MainScreenEvents
    data class OnSaveButtonClick(val module: ModuleDisplayable) : MainScreenEvents
    data class OnNameTextEntered(val name: String) : MainScreenEvents
    data class OnCommentTextEntered(val comment: String) : MainScreenEvents
    data class OnIncrementationEntered(val incrementationNumber: String) : MainScreenEvents
    data class OnDeleteModuleClick(val module: ModuleDisplayable) : MainScreenEvents

    data class ActionEditIncrementation(val module: ModuleDisplayable) : MainScreenEvents
    data class ActionAddNewIncrementation(val module: ModuleDisplayable) : MainScreenEvents
    data class OnPickDate(val date: LocalDate) : MainScreenEvents
    data class OnPickModuleAddNewIncrementationFromDate(val module: ModuleDisplayable) :
        MainScreenEvents

    object OnDropDownMenuDismiss : MainScreenEvents
    data class OnAddNewIncrementation(val module: ModuleDisplayable) : MainScreenEvents
    data class OnEditIncrementation(val module: ModuleDisplayable) : MainScreenEvents
    data class OnModuleClick(val id: UUID) : MainScreenEvents
    data class ToggleSkipped(val module: ModuleDisplayable) : MainScreenEvents
    data class OnSearchedTextEntered(val text: String) : MainScreenEvents
    object OnSearchTextClick : MainScreenEvents
    object OnCloseSearchIconClick : MainScreenEvents

}