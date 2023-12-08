package com.eltescode.modules.features.modules.presentation.module_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltescode.modules.R
import com.eltescode.modules.core.utils.UiEvent
import com.eltescode.modules.core.utils.UiText
import com.eltescode.modules.features.modules.domain.model.Module
import com.eltescode.modules.features.modules.domain.use_cases.ModuleUseCases
import com.eltescode.modules.features.modules.presentation.utils.ModuleScreenEvents
import com.eltescode.modules.features.modules.presentation.utils.ModuleScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ModuleScreenViewModel @Inject constructor(private val useCases: ModuleUseCases) :
    ViewModel() {

    private val _state = mutableStateOf(ModuleScreenState())
    val state: State<ModuleScreenState> = _state

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var job: Job? = null
    fun onIdPassed(id: UUID) {
        if (state.value.module == null) {
            viewModelScope.launch {
                _state.value = state.value.copy(module = useCases.getModuleUseCase(id))
            }
        }
    }

    fun onEvent(event: ModuleScreenEvents) {
        when (event) {
            ModuleScreenEvents.OnBackButtonPress -> {
                job = null
                job = viewModelScope.launch {
                    _uiEvent.send(UiEvent.OnBack)
                }
            }

            is ModuleScreenEvents.OnCommentEntered -> {
                _state.value =
                    _state.value.copy(module = state.value.module?.copy(comment = event.comment))
            }

            ModuleScreenEvents.OnEditCommentToggle -> {
                _state.value =
                    _state.value.copy(isCommentEditEnabled = !state.value.isCommentEditEnabled)
            }

            ModuleScreenEvents.OnEditNameToggle -> {
                _state.value = _state.value.copy(isNameEditEnabled = !state.value.isNameEditEnabled)
            }

            is ModuleScreenEvents.OnModuleSaveButtonClick -> {
                _state.value = state.value.copy(
                    isCommentEditEnabled = false, isNameEditEnabled = false
                )
                save(event.module)
                job = null
                job = viewModelScope.launch {
                    _uiEvent.send(
                        UiEvent.ShowSnackBar(
                            UiText.StringResource(R.string.module_saved)
                        )
                    )
                }
            }

            is ModuleScreenEvents.OnNameEntered -> {
                _state.value =
                    _state.value.copy(module = _state.value.module?.copy(name = event.name))
            }
        }
    }

    private fun save(module: Module) {
        job = null
        job = viewModelScope.launch {
            useCases.addModuleUseCase(module)
        }
    }
}