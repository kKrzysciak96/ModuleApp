package com.eltescode.modules.features.modules.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.eltescode.modules.core.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID


@Entity
data class Module(
    val name: String,
    val comment: String,
    val incrementation: Int,
    val newIncrementation: Int? = null,
    val epochDay: Long,
    val isSkipped: Boolean,
    val timeStamp: Long,
    @PrimaryKey
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
)





