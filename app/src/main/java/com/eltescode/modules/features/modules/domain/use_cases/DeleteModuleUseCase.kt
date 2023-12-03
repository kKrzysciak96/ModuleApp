package com.eltescode.modules.features.modules.domain.use_cases

import com.eltescode.modules.features.modules.domain.model.Module
import com.eltescode.modules.features.modules.domain.repository.ModuleRepository

class DeleteModuleUseCase(private val repository: ModuleRepository) {

    suspend operator fun invoke(module: Module) {
        repository.deleteModule(module)
    }
}