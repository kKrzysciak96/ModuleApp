package com.eltescode.modules.features.modules.presentation.utils

import com.eltescode.modules.features.modules.presentation.model.ModuleDisplayable

sealed interface PerformedActionMarker {
    object ActionDeleted : PerformedActionMarker
    object ActionAdded : PerformedActionMarker
    data class ActionUpdated(val oldModule: ModuleDisplayable) : PerformedActionMarker
}
