package com.eltescode.modules.features.modules.presentation.utils

import com.eltescode.modules.features.modules.domain.model.Module

sealed interface PerformedActionMarker {
    object ActionDeleted : PerformedActionMarker
    object ActionAdded : PerformedActionMarker
    data class ActionUpdated(val oldModule: Module) : PerformedActionMarker
}
