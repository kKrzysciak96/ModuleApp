package com.eltescode.modules.features.modules.domain.repository

import com.eltescode.modules.features.modules.domain.model.Module
import kotlinx.coroutines.flow.Flow


interface ModuleRepository {

    suspend fun editModule(module: Module)

    suspend fun addModule(module: Module)
    suspend fun addModules(modules: List<Module>)

    suspend fun deleteModule(module: Module)

    fun geModules(): Flow<List<Module>>

    suspend fun dropDatabase()
}