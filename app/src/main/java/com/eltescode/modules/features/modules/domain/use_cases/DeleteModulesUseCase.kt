package com.eltescode.modules.features.modules.domain.use_cases

import com.eltescode.modules.features.modules.domain.model.Module
import com.eltescode.modules.features.modules.domain.repository.ModuleRepository


class DeleteModulesUseCase(private val repository: ModuleRepository) {

    suspend operator fun invoke(module: List<Module>) {
        repository.deleteModules(module)
    }
}