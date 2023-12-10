package com.eltescode.modules.features.modules.data.remote

import com.eltescode.modules.core.utils.UUIDSerializer
import com.eltescode.modules.features.modules.domain.model.Module
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class SupabaseSpecificModule(
    val name: String,
    val comment: String,
    val incrementation: String,
    val newIncrementation: String? = null,
    val epochDay: String,
    val isSkipped: Boolean,
    val timeStamp: String,
    val reset: Boolean,
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,

    ) {
    constructor(module: Module) : this(
        name = module.name,
        comment = module.comment,
        incrementation = module.incrementation.toString(),
        newIncrementation = module.newIncrementation.toString(),
        epochDay = module.epochDay.toString(),
        id = module.id,
        reset = true,
        timeStamp = module.timeStamp.toString(),
        isSkipped = module.isSkipped
    )

    fun toModule() = Module(
        name = name,
        comment = comment,
        incrementation = incrementation.toInt(),
        newIncrementation = newIncrementation?.toIntOrNull(),
        epochDay = epochDay.toLong(),
        id = id,
        isSkipped = isSkipped,
        timeStamp = timeStamp.toLong()
    )

}