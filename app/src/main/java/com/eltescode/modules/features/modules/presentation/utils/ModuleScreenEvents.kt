package com.eltescode.modules.features.modules.presentation.utils

import com.eltescode.modules.features.modules.domain.model.Module

sealed interface ModuleScreenEvents {

    object OnEditNameToggle : ModuleScreenEvents
    data class OnNameEntered(val name: String) : ModuleScreenEvents
    object OnEditCommentToggle : ModuleScreenEvents
    data class OnCommentEntered(val comment: String) : ModuleScreenEvents
    data class OnModuleSaveButtonClick(val module: Module) : ModuleScreenEvents
    object OnGroupSaveButtonClick : ModuleScreenEvents
    object OnBackButtonPress : ModuleScreenEvents
    object OnToggleColorsClick : ModuleScreenEvents
    object OnColorMenuDismissRequest : ModuleScreenEvents
    object OnToggleFontSizeClick : ModuleScreenEvents
    object OnFontSizeMenuDismissRequest : ModuleScreenEvents
    object OnGroupToggleButtonClick : ModuleScreenEvents
}