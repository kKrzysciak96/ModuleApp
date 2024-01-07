package com.eltescode.modules.features.modules.data.repository

import com.eltescode.modules.core.utils.ApiResult
import com.eltescode.modules.features.modules.data.local.ModuleDao
import com.eltescode.modules.features.modules.data.remote.RemoteDataBase
import com.eltescode.modules.features.modules.data.remote.SupabaseSpecificModule
import com.eltescode.modules.features.modules.domain.model.Module
import com.eltescode.modules.features.modules.domain.repository.ModuleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID

class ModuleRepositoryImpl(
    private val dao: ModuleDao,
    private val remoteDataBase: RemoteDataBase<SupabaseSpecificModule>
) : ModuleRepository {
    override suspend fun editModule(module: Module) {
        editModule(module)
    }

    override suspend fun pushModulesToRemote(): Flow<ApiResult<Unit>> {
        return flow {
            emit(ApiResult.Loading)
            try {
                val modulesToPush = dao.getModules().map { SupabaseSpecificModule(it) }
                remoteDataBase.pushModulesToRemote(modulesToPush)
                emit(ApiResult.Success(Unit))
            } catch (e: Exception) {
                emit(ApiResult.Error(e.message))
            }
        }
    }

    override suspend fun fetchModulesFromRemote(): Flow<ApiResult<Unit>> {
        return flow {
            emit(ApiResult.Loading)
            try {
                val remoteData = remoteDataBase.fetchModulesFromRemote()
                remoteData.map { it.toModule() }.also {
                    dao.addModules(*it.toTypedArray())
                }
                emit(ApiResult.Success(Unit))
            } catch (e: Exception) {
                emit(ApiResult.Error(e.message))
            }
        }
    }

    override suspend fun addModule(module: Module) {
        dao.addModule(module)
    }

    override suspend fun addModules(modules: List<Module>) {
        dao.addModules(*modules.toTypedArray())
    }

    override suspend fun deleteModule(module: Module) {
        dao.deleteModule(module)
    }

    override suspend fun deleteModules(modules: List<Module>) {
        dao.deleteModules(modules)
    }


    override fun getModules(): Flow<List<Module>> {
        return dao.getModulesFlow()
    }

    override suspend fun getModule(id: UUID): Module {
        return dao.geModule(id)
    }

    override suspend fun dropDatabase() {
        dao.dropDatabase()
    }

}