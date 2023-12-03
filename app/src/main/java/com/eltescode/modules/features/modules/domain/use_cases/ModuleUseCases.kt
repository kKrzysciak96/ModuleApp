package com.eltescode.modules.features.modules.domain.use_cases

data class ModuleUseCases(
    val addModuleUseCase: AddModuleUseCase,
    val deleteModuleUseCase: DeleteModuleUseCase,
    val getModulesUseCase: GetModulesUseCase,
    val dropDataBase: DropDataBase,
    val filterModulesUseCase: FilterModulesUseCase,
    val addModulesUseCase: AddModulesUseCase
)
