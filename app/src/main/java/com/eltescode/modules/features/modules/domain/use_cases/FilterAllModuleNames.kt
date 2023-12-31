package com.eltescode.modules.features.modules.domain.use_cases

import com.eltescode.modules.features.modules.domain.model.Module

class FilterAllModuleNames() {

    operator fun invoke(modules: List<Module>): List<String> {
        return modules.map { it.name }.distinct()

    }
}