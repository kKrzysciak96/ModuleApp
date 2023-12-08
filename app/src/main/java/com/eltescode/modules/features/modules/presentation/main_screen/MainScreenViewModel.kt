package com.eltescode.modules.features.modules.presentation.main_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltescode.modules.R
import com.eltescode.modules.core.navigation.Routes
import com.eltescode.modules.core.preferences.Preferences
import com.eltescode.modules.core.utils.UiEvent
import com.eltescode.modules.core.utils.UiText
import com.eltescode.modules.features.modules.domain.model.Module
import com.eltescode.modules.features.modules.domain.use_cases.ModuleUseCases
import com.eltescode.modules.features.modules.presentation.model.ModuleDisplayable
import com.eltescode.modules.features.modules.presentation.utils.CustomException
import com.eltescode.modules.features.modules.presentation.utils.MainScreenEvents
import com.eltescode.modules.features.modules.presentation.utils.MainScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val useCases: ModuleUseCases, private val preferences: Preferences
) : ViewModel() {

    var lastPage = preferences.loadLastCard()

    private val _state = mutableStateOf(MainScreenState(currentPage = lastPage))
    val state: State<MainScreenState> = _state

    private var job: Job? = null

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        getModules()
    }

    fun onEvent(event: MainScreenEvents) {

        when (event) {

            is MainScreenEvents.OnAddButtonClick -> {
                _state.value = state.value.copy(newModuleToInsert = event.module)
                _state.value = state.value.copy(isAddModuleDialogVisible = true)
            }

            MainScreenEvents.OnAddModuleDialogDismiss -> {
                _state.value = state.value.copy(newModuleToInsert = null)
                _state.value = state.value.copy(isAddModuleDialogVisible = false)
            }

            is MainScreenEvents.OnSaveButtonClick -> {
                val module = event.module
                try {
                    if (module.name.isBlank()) {
                        throw CustomException(uiTextMessage = UiText.StringResource(R.string.name_error))
                    }
                    if (!module.incrementation.isDigitsOnly() || module.incrementation.isEmpty()) {

                        throw CustomException(uiTextMessage = UiText.StringResource(R.string.number_error))
                    }
                    onEvent(MainScreenEvents.OnAddModuleDialogDismiss)
                    addModule(event.module.toModule())

                } catch (e: CustomException) {
                    job = null
                    job = viewModelScope.launch {
                        _uiEvent.send(
                            UiEvent.ShowSnackBar(
                                message = e.uiTextMessage
                            )
                        )
                    }
                }
            }

            is MainScreenEvents.OnDeleteModuleClick -> {
                deleteModule(event.module.toModule())
            }

            is MainScreenEvents.OnCommentTextEntered -> {
                _state.value =
                    state.value.copy(newModuleToInsert = state.value.newModuleToInsert?.copy(comment = event.comment))
            }

            is MainScreenEvents.OnIncrementationEntered -> {
                _state.value = state.value.copy(
                    newModuleToInsert = state.value.newModuleToInsert?.copy(incrementation = event.incrementationNumber)
                )
            }

            is MainScreenEvents.OnNameTextEntered -> {
                _state.value =
                    state.value.copy(newModuleToInsert = state.value.newModuleToInsert?.copy(name = event.name))
            }

            is MainScreenEvents.OnLongModulePress -> {
                _state.value = state.value.copy(modules = state.value.modules.map {
                    if (it.id == event.moduleId) {
                        it.copy(isModuleDropdownMenuVisible = true)
                    } else {
                        it
                    }
                })
            }

            MainScreenEvents.OnModuleActionsMenuDismiss -> {
                _state.value = state.value.copy(modules = state.value.modules.map {
                    it.copy(isModuleDropdownMenuVisible = false)
                })
            }

            is MainScreenEvents.ActionAddNewIncrementation -> {
                _state.value = state.value.copy(modules = state.value.modules.map {
                    it.copy(isModuleDropdownMenuVisible = false)
                })

                _state.value = state.value.copy(modules = state.value.modules.map {
                    if (it.id == event.module.id) {
                        it.copy(isAddNewIncrementDropdownMenuVisible = true)
                    } else {
                        it
                    }
                })
            }

            MainScreenEvents.OnDropDownMenuDismiss -> {
                _state.value = state.value.copy(
                    modules = state.value.modules.map {
                        it.copy(
                            isAddNewIncrementDropdownMenuVisible = false,
                            isEditIncrementDropdownMenuVisible = false
                        )
                    }
                )
            }

            is MainScreenEvents.ActionEditIncrementation -> {
                _state.value = state.value.copy(modules = state.value.modules.map {
                    it.copy(isModuleDropdownMenuVisible = false)
                })
                _state.value = state.value.copy(
                    modules = state.value.modules.map {
                        if (it.id == event.module.id) {
                            it.copy(isEditIncrementDropdownMenuVisible = true)
                        } else {
                            it
                        }
                    }
                )
            }

            is MainScreenEvents.OnModuleClick -> {
                job = null
                job = viewModelScope.launch {
                    val route = Routes.MODULE_SCREEN + "/${event.id}"
                    _uiEvent.send(UiEvent.OnNextScreen(route))
                }
            }

            is MainScreenEvents.OnAddNewIncrementation -> {
                job = null
                job = viewModelScope.launch {
                    if (event.module.newIncrementation != null) {
                        state.value.newModuleToInsert?.let { newModuleToInsert ->
                            val skippedModuleToUpdate =
                                event.module.copy(isSkipped = true, newIncrementation = null)
                                    .toModule()
                            val skippedModuleToInsert = newModuleToInsert.copy(
                                id = UUID.randomUUID(),
                                newIncrementation = event.module.newIncrementation
                            ).toModule()

                            val newIncrementation =
                                skippedModuleToInsert.incrementation + skippedModuleToInsert.newIncrementation!!

                            val newEpochDay = LocalDate.ofEpochDay(skippedModuleToInsert.epochDay)
                                .plusDays(newIncrementation.toLong()).toEpochDay()

                            val moduleToAdd = skippedModuleToInsert.copy(
                                newIncrementation = null,
                                incrementation = newIncrementation,
                                epochDay = newEpochDay,
                                id = UUID.randomUUID()
                            )
                            useCases.addModulesUseCase(
                                listOf(
                                    skippedModuleToUpdate,
                                    skippedModuleToInsert,
                                    moduleToAdd
                                )
                            )
                            _state.value = state.value.copy(newModuleToInsert = null)
                            return@launch
                        }
                    }

                    val moduleToUpdate = event.module.toModule()
                    if (moduleToUpdate.newIncrementation != null) {
                        val newIncrementation =
                            moduleToUpdate.incrementation + moduleToUpdate.newIncrementation
                        val newEpochDay = LocalDate.ofEpochDay(moduleToUpdate.epochDay)
                            .plusDays(newIncrementation.toLong()).toEpochDay()
                        val moduleToAdd = moduleToUpdate.copy(
                            newIncrementation = null,
                            incrementation = newIncrementation,
                            epochDay = newEpochDay,
                            id = UUID.randomUUID()
                        )
                        useCases.addModulesUseCase(listOf(moduleToUpdate, moduleToAdd))
                    } else {
                        useCases.addModuleUseCase(moduleToUpdate)
                    }
                }
            }

            is MainScreenEvents.OnEditIncrementation -> {
                job = null
                job = viewModelScope.launch {
                    val moduleToUpdate = event.module.toModule()
                    useCases.addModuleUseCase(moduleToUpdate)
                }
            }


            is MainScreenEvents.OnPickDate -> {

                _state.value = state.value.copy(
                    newModuleToInsert = state.value.newModuleToInsert?.copy(epochDay = event.date.toEpochDay())
                )

                _state.value = state.value.copy(modules = state.value.modules.map {
                    if (it.id == state.value.newModuleToInsert?.id) {
                        it.copy(isAddNewIncrementDropdownMenuVisible = true)
                    } else {
                        it
                    }
                })
            }

            is MainScreenEvents.OnPickModuleAddNewIncrementationFromDate -> {
                _state.value = state.value.copy(newModuleToInsert = event.module)
            }

            is MainScreenEvents.ToggleSkipped -> {
                job = null
                job = viewModelScope.launch {
                    useCases.addModuleUseCase(
                        event.module.copy(isSkipped = !event.module.isSkipped).toModule()
                    )
                }
            }

            MainScreenEvents.OnCloseSearchIconClick -> {
                _state.value = state.value.copy(isSearchActive = false)
            }

            MainScreenEvents.OnSearchTextClick -> {
                _state.value = state.value.copy(isSearchActive = true)
            }

            is MainScreenEvents.OnSearchedTextEntered -> {
                _state.value = state.value.copy(searchedText = event.text)
            }
        }
    }

    private fun getModules() {
        viewModelScope.launch {
            useCases.getModulesUseCase().collectLatest { list ->
                _state.value = state.value.copy(modules = list.map { ModuleDisplayable(it) })
            }
        }
    }

    private fun addModule(module: Module) {
        job = null
        job = viewModelScope.launch {
            useCases.addModuleUseCase(module)
        }
    }

    private fun deleteModule(module: Module) {
        job = null
        job = viewModelScope.launch {
            useCases.deleteModuleUseCase(module)
        }
    }

    fun dropDatabase() {
        job = null
        job = viewModelScope.launch {
            useCases.dropDataBase()
        }
    }

    fun updateLastCard(page: Int) {
        preferences.saveLastCard(page)
        _state.value = state.value.copy(currentPage = page)
    }


}