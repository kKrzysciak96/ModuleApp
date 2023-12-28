package com.eltescode.modules.features.modules.domain.use_cases

data class ModuleUseCases(
    val addModuleUseCase: AddModuleUseCase,
    val deleteModuleUseCase: DeleteModuleUseCase,
    val getModulesUseCase: GetModulesUseCase,
    val dropDataBase: DropDataBase,
    val filterModulesUseCase: FilterModulesUseCase,
    val addModulesUseCase: AddModulesUseCase,
    val getModuleUseCase: GetModuleUseCase,
    val fetchModulesFromRemoteUseCase: FetchModulesFromRemoteUseCase,
    val pushModulesToRemoteUseCase: PushModulesToRemoteUseCase,
    val filterAllModuleNames: FilterAllModuleNames,
    val deleteModulesUseCase: DeleteModulesUseCase,
    val updateUndoListUseCase: UpdateUndoListUseCase,
)
