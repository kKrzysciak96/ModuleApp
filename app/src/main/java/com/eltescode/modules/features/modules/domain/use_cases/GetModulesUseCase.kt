package com.eltescode.modules.features.modules.domain.use_cases

import com.eltescode.modules.features.modules.domain.model.Module
import com.eltescode.modules.features.modules.domain.repository.ModuleRepository
import kotlinx.coroutines.flow.Flow


class GetModulesUseCase(private val repository: ModuleRepository) {

    operator fun invoke(): Flow<List<Module>> {
        return repository.geModules()
    }
}