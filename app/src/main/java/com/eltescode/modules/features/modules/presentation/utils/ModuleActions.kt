package com.eltescode.modules.features.modules.presentation.utils

import com.eltescode.modules.R
import com.eltescode.modules.core.utils.UiText

enum class ModuleActions(val actionName: UiText) {
    ActionDelete(UiText.StringResource(R.string.delete_action)),
    ActionEditName(UiText.StringResource(R.string.edit_name_action)),
    ActionEditComment(UiText.StringResource(R.string.edit_comment_action)),
    ActionEditIncrementation(UiText.StringResource(R.string.edit_increment_action)),
    ActionAddNewIncrementation(UiText.StringResource(R.string.add_new_increment_action)),


}
