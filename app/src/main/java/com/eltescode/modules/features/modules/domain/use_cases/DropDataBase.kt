package com.eltescode.modules.features.modules.domain.use_cases

import com.eltescode.modules.features.modules.domain.repository.ModuleRepository

class DropDataBase(private val repository: ModuleRepository) {

    suspend operator fun invoke() {
        repository.dropDatabase()
    }
}