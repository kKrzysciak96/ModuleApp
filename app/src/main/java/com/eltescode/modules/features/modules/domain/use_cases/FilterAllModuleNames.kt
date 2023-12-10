package com.eltescode.modules.features.modules.domain.use_cases

import com.eltescode.modules.features.modules.domain.repository.ModuleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FilterAllModuleNames(private val repository: ModuleRepository) {

    operator fun invoke(): Flow<List<String>> {
        return repository.getModules().map { list ->
            list.map { it.name }.distinct()
        }
    }
}