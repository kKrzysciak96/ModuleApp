package com.eltescode.modules.features.modules.presentation.utils

sealed interface AllModulesPreviewScreenEvents {
    object OnDeleteButtonClick : AllModulesPreviewScreenEvents
    object OnDeleteDialogDismiss : AllModulesPreviewScreenEvents
    object OnConfirmDeleteButtonClick : AllModulesPreviewScreenEvents
}