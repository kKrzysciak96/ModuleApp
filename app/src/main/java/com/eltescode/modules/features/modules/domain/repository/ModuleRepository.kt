package com.eltescode.modules.features.modules.domain.repository

import com.eltescode.modules.core.utils.ApiResult
import com.eltescode.modules.features.modules.domain.model.Module
import kotlinx.coroutines.flow.Flow
import java.util.UUID


interface ModuleRepository {

    suspend fun editModule(module: Module)

    suspend fun addModule(module: Module)

    suspend fun addModules(modules: List<Module>)

    suspend fun deleteModule(module: Module)

    suspend fun deleteModules(modules: List<Module>)

    fun getModules(): Flow<List<Module>>

    suspend fun getModule(id: UUID): Module

    suspend fun dropDatabase()

    suspend fun pushModulesToRemote(): Flow<ApiResult<Unit>>

    suspend fun fetchModulesFromRemote(): Flow<ApiResult<Unit>>

}