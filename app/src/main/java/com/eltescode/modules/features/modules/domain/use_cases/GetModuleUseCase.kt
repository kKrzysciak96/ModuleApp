package com.eltescode.modules.features.modules.domain.use_cases

import com.eltescode.modules.features.modules.domain.model.Module
import com.eltescode.modules.features.modules.domain.repository.ModuleRepository
import java.util.UUID

class GetModuleUseCase(private val repository: ModuleRepository) {

    suspend operator fun invoke(id: UUID): Module {
        return repository.getModule(id)
    }
}