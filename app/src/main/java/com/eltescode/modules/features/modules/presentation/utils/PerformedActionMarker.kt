package com.eltescode.modules.features.modules.presentation.utils

sealed interface PerformedActionMarker {
    object ActionDeleted : PerformedActionMarker
    object ActionAdded : PerformedActionMarker
    object ActionUpdated : PerformedActionMarker
}
