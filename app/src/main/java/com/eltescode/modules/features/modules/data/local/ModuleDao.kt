package com.eltescode.modules.features.modules.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.eltescode.modules.features.modules.domain.model.Module
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface ModuleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addModule(module: Module)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addModules(vararg module: Module)

    @Delete
    suspend fun deleteModule(module: Module)

    @Query("SELECT * FROM Module")
    fun geModules(): Flow<List<Module>>

    @Query("SELECT * FROM Module WHERE id = :id")
    suspend fun geModule(id: UUID): Module

    @Query("DELETE FROM Module")
    suspend fun dropDatabase()
}