package com.eltescode.modules.features.modules.data.repository

import com.eltescode.modules.features.modules.data.local.ModuleDao
import com.eltescode.modules.features.modules.domain.model.Module
import com.eltescode.modules.features.modules.domain.repository.ModuleRepository
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class ModuleRepositoryImpl(private val dao: ModuleDao) : ModuleRepository {
    override suspend fun editModule(module: Module) {
        editModule(module)
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

    override fun getModules(): Flow<List<Module>> {
        return dao.geModules()
    }

    override suspend fun getModule(id: UUID): Module {
        return dao.geModule(id)
    }

    override suspend fun dropDatabase() {
        dao.dropDatabase()
    }

}