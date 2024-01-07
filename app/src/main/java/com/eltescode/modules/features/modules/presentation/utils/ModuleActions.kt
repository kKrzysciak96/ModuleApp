package com.eltescode.modules.features.modules.presentation.utils

import com.eltescode.modules.R
import com.eltescode.modules.core.utils.UiText

enum class ModuleActions(val actionName: UiText) {
    ActionDelete(UiText.StringResource(R.string.delete_action)),
    ActionEditIncrementation(UiText.StringResource(R.string.edit_increment_action)),
    ActionAddNewIncrementation(UiText.StringResource(R.string.add_new_increment_action)),
    ActionAddNewIncrementationFromDate(UiText.StringResource(R.string.add_new_increment_from_date_action)),
    ActionToggleSkipped(UiText.StringResource(R.string.toggle_skipped)),
}
