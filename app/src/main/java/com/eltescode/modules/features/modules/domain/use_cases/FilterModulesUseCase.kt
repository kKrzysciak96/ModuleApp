package com.eltescode.modules.features.modules.domain.use_cases

import com.eltescode.modules.features.modules.domain.model.Module
import java.time.LocalDate

class FilterModulesUseCase {

    operator fun invoke(modules: List<Module>, date: LocalDate): List<Module> {
        val startRange = date.toEpochDay()
        val endRange = date.plusDays(13L).toEpochDay()
        return modules.filter { module -> module.epochDay in startRange until endRange }
    }
}