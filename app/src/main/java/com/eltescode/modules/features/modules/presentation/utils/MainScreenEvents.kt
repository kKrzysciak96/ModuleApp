package com.eltescode.modules.features.modules.presentation.utils

import com.eltescode.modules.features.modules.presentation.model.ModuleDisplayable
import java.util.UUID

sealed interface MainScreenEvents {
    object OnPreviousClick : MainScreenEvents
    object OnNextClick : MainScreenEvents
    object OnAddModuleDialogDismiss : MainScreenEvents
    data class OnLongModulePress(val moduleId: UUID) : MainScreenEvents
    object OnModuleActionsMenuDismiss : MainScreenEvents
    data class OnAddButtonClick(val module: ModuleDisplayable) : MainScreenEvents
    data class OnSaveButtonClick(val module: ModuleDisplayable) : MainScreenEvents
    data class OnNameTextEntered(val name: String) : MainScreenEvents
    data class OnCommentTextEntered(val comment: String) : MainScreenEvents
    data class OnIncrementationEntered(val incrementationNumber: String) : MainScreenEvents
    data class OnDeleteModuleClick(val module: ModuleDisplayable) : MainScreenEvents
    data class OnActionEditName(val module: ModuleDisplayable) : MainScreenEvents
    data class ActionEditComment(val module: ModuleDisplayable) : MainScreenEvents
    data class ActionEditIncrementation(val module: ModuleDisplayable) : MainScreenEvents
    data class ActionAddNewIncrementation(val module: ModuleDisplayable) : MainScreenEvents
    object OnAddNewIncrementMenuDismiss : MainScreenEvents
    data class OnAddNewIncrementation(val module: ModuleDisplayable) : MainScreenEvents
    data class OnModuleClick(val id: UUID) : MainScreenEvents

}