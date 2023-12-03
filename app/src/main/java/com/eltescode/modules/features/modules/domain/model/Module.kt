package com.eltescode.modules.features.modules.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID


@Entity
data class Module(
    val name: String,
    val comment: String,
    val incrementation: Int,
    val newIncrementation: Int? = null,
    val epochDay: Long,
    @PrimaryKey
    val id: UUID,
)