package com.eltescode.modules.features.modules.presentation.all_modules_preview

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltescode.modules.features.modules.domain.model.Module
import com.eltescode.modules.features.modules.domain.use_cases.ModuleUseCases
import com.eltescode.modules.features.modules.presentation.utils.AllModulesPreviewScreenEvents
import com.eltescode.modules.features.modules.presentation.utils.AllModulesPreviewState
import com.eltescode.modules.features.modules.presentation.utils.PerformedActionMarker
import com.eltescode.modules.features.modules.presentation.utils.UndoHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllModulesPreviewScreenViewModel @Inject constructor(
    private val useCases: ModuleUseCases,
    private val undoHelper: UndoHelper
) :
    ViewModel() {

    private val _state = mutableStateOf(AllModulesPreviewState(emptyList()))
    val state: State<AllModulesPreviewState> = _state

    private var oldModules: List<Module> = emptyList()

    private var job: Job? = null

    init {
        getAllModuleNames()
    }

    fun onEvent(event: AllModulesPreviewScreenEvents) {
        when (event) {
            AllModulesPreviewScreenEvents.OnConfirmDeleteButtonClick -> {
                if (oldModules.isNotEmpty()) {
                    undoHelper.updateUndoList(oldModules.map {
                        Pair(PerformedActionMarker.ActionDeleted, it)
                    }
                    )
                }

                dropDatabase()
            }

            AllModulesPreviewScreenEvents.OnDeleteButtonClick -> {
                _state.value = state.value.copy(isDeleteAllDialogVisible = true)
            }

            AllModulesPreviewScreenEvents.OnDeleteDialogDismiss -> {
                _state.value = state.value.copy(isDeleteAllDialogVisible = false)
            }
        }
    }


    fun dropDatabase() {
        job = null
        job = viewModelScope.launch {

            useCases.dropDataBase()
        }
    }

    private fun getAllModuleNames() {
        viewModelScope.launch {
            useCases.getModulesUseCase().collect { modules ->
                oldModules = modules
                _state.value = state.value.copy(
                    allModules = useCases.filterAllModuleNames(modules).sortedBy { it })
            }
        }
    }
}