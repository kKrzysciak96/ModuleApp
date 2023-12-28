package com.eltescode.modules.features.modules.presentation.utils

import com.eltescode.modules.features.modules.presentation.model.ModuleDisplayable
import java.time.LocalDate
import java.util.UUID

sealed interface MainScreenEvents {
    object OnAddModuleDialogDismiss : MainScreenEvents
    data class OnLongModulePress(val moduleId: UUID) : MainScreenEvents
    object OnModuleActionsMenuDismiss : MainScreenEvents
    data class OnAddButtonClick(val epochDate: Long) : MainScreenEvents
    data class OnSaveButtonClick(val module: ModuleDisplayable) : MainScreenEvents
    data class OnNameTextEntered(val name: String) : MainScreenEvents
    data class OnCommentTextEntered(val comment: String) : MainScreenEvents
    data class OnIncrementationEntered(val incrementationNumber: String) : MainScreenEvents
    data class OnDeleteModuleClick(val module: ModuleDisplayable) : MainScreenEvents

    data class ActionEditIncrementation(val module: ModuleDisplayable) : MainScreenEvents
    data class ActionAddNewIncrementation(val module: ModuleDisplayable) : MainScreenEvents
    data class OnPickDate(val date: LocalDate) : MainScreenEvents
    data class ActionAddNewIncrementationFromDate(val module: ModuleDisplayable) :
        MainScreenEvents

    object OnAddNewIncrementDropDownMenuDismiss : MainScreenEvents
    object OnEditIncrementDropDownMenuDismiss : MainScreenEvents
    object OnCalendarDialogDismiss : MainScreenEvents
    data class OnUpdateModule(val newModule: ModuleDisplayable, val oldModule: ModuleDisplayable) :
        MainScreenEvents

    data class OnAddNewIncrementation(val module: ModuleDisplayable) : MainScreenEvents
    data class OnAddNewIncrementationFromDate(val module: ModuleDisplayable) : MainScreenEvents
    data class OnEditIncrementation(
        val newModule: ModuleDisplayable,
        val oldModule: ModuleDisplayable
    ) : MainScreenEvents

    data class OnModuleClick(val id: UUID) : MainScreenEvents
    data class OnToggleSkipped(val module: ModuleDisplayable) : MainScreenEvents
    data class OnSearchedTextEntered(val text: String) : MainScreenEvents
    object OnSearchTextClick : MainScreenEvents
    object OnSearchViewClose : MainScreenEvents
    data class OnSearchOptionChange(val searchOption: SearchOptions) : MainScreenEvents
    object OnSearchOptionSectionToggle : MainScreenEvents
    object OnConfirmPushClick : MainScreenEvents
    object OnConfirmFetchClick : MainScreenEvents
    data class OnNewCardVisible(val cardNumber: Int) : MainScreenEvents
    object OnShowAllModulesPreviewIconClick : MainScreenEvents
    object OnPushButtonClick : MainScreenEvents
    object OnFetchButtonClick : MainScreenEvents

    object OnPushDialogDismiss : MainScreenEvents
    object OnFetchDialogDismiss : MainScreenEvents
    object OnUndoClick : MainScreenEvents
    object OnRedoClick : MainScreenEvents


}