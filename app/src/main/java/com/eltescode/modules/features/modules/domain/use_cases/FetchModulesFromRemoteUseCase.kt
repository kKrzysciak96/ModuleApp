package com.eltescode.modules.features.modules.domain.use_cases

import com.eltescode.modules.core.utils.ApiResult
import com.eltescode.modules.features.modules.domain.repository.ModuleRepository
import kotlinx.coroutines.flow.Flow

class FetchModulesFromRemoteUseCase(private val repository: ModuleRepository) {
    suspend operator fun invoke(): Flow<ApiResult<Unit>> {
        return repository.fetchModulesFromRemote()
    }
}