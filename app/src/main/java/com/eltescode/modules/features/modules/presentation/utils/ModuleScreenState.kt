package com.eltescode.modules.features.modules.presentation.utils

import com.eltescode.modules.features.modules.domain.model.Module

data class ModuleScreenState(
    val module: Module? = null,
    val isNameEditEnabled: Boolean = false,
    val isCommentEditEnabled: Boolean = false,
)