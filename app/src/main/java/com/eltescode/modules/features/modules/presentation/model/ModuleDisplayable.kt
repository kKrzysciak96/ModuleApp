package com.eltescode.modules.features.modules.presentation.model

import com.eltescode.modules.features.modules.domain.model.Module
import java.util.UUID

data class ModuleDisplayable(
    val name: String,
    val comment: String,
    val incrementation: String,
    val newIncrementation: Int? = null,
    val epochDay: Long,
    val id: UUID,
    val isModuleDropdownMenuVisible: Boolean = false,
    val isAddNewIncrementDropdownMenuVisible: Boolean = false,
    val isAddNewIncrementFromDateDropdownMenuVisible: Boolean = false,
    val isEditIncrementDropdownMenuVisible: Boolean = false,
    val isSkipped: Boolean = false
) {
    constructor(module: Module) : this(
        name = module.name,
        comment = module.comment,
        incrementation = module.incrementation.toString(),
        newIncrementation = module.newIncrementation,
        epochDay = module.epochDay,
        id = module.id,
        isSkipped = module.isSkipped
    )

    fun toModule() = Module(
        name = name,
        comment = comment,
        incrementation = incrementation.toInt(),
        newIncrementation = newIncrementation,
        epochDay = epochDay,
        id = id,
        isSkipped = isSkipped
    )

    fun prepareDescriptionText(): String {

        return if (newIncrementation != null) {
            newIncrementation.toString() + "_" + name + " "
        } else {
            "$name "
        }
    }
}