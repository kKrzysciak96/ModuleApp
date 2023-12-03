package com.eltescode.modules.features.modules.domain.use_cases

import com.eltescode.modules.features.modules.domain.model.Module
import com.eltescode.modules.features.modules.domain.repository.ModuleRepository


class AddModulesUseCase(private val repository: ModuleRepository) {

    suspend operator fun invoke(modules: List<Module>) {
        repository.addModules(modules)
    }
}