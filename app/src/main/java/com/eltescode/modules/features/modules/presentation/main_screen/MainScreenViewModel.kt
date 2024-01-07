package com.eltescode.modules.features.modules.presentation.main_screen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltescode.modules.R
import com.eltescode.modules.core.navigation.Routes
import com.eltescode.modules.core.preferences.Preferences
import com.eltescode.modules.core.utils.ApiResult
import com.eltescode.modules.core.utils.UiEvent
import com.eltescode.modules.core.utils.UiText
import com.eltescode.modules.features.modules.domain.use_cases.ModuleUseCases
import com.eltescode.modules.features.modules.presentation.model.ModuleDisplayable
import com.eltescode.modules.features.modules.presentation.utils.CustomException
import com.eltescode.modules.features.modules.presentation.utils.MainScreenEvents
import com.eltescode.modules.features.modules.presentation.utils.MainScreenState
import com.eltescode.modules.features.modules.presentation.utils.PerformedActionMarker
import com.eltescode.modules.features.modules.presentation.utils.SearchOptions
import com.eltescode.modules.features.modules.presentation.utils.SearchOrder
import com.eltescode.modules.features.modules.presentation.utils.UndoHelper
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val useCases: ModuleUseCases,
    private val preferences: Preferences,
    private val undoHelper: UndoHelper
) : ViewModel() {

    var lastPage = preferences.loadLastCard()

    private val _state = mutableStateOf(
        MainScreenState(
            currentPage = lastPage,
            calendarState = UseCaseState(
                onDismissRequest = { onEvent(MainScreenEvents.OnCalendarDialogDismiss) },
            ),
            searchOptions = SearchOptions.Contains(SearchOrder.Descending)
        )
    )
    val state: State<MainScreenState> = _state

    private var job: Job? = null

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        getModules()
    }

    fun onEvent(event: MainScreenEvents) {

        when (event) {

            is MainScreenEvents.OnCalendarDialogDismiss -> {
                _state.value = state.value.copy(newModuleToInsert = null)
            }

            is MainScreenEvents.OnAddButtonClick -> {
                val module = ModuleDisplayable(
                    name = "",
                    comment = "",
                    incrementation = "",
                    epochDay = event.epochDate,
                    id = UUID.randomUUID(),
                    timeStamp = System.currentTimeMillis()
                )
                _state.value = state.value.copy(newModuleToInsert = module)
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
                    job = null
                    job = viewModelScope.launch {
                        addModule(event.module)
                        undoHelper.updateUndoList(
                            listOf(
                                Pair(
                                    PerformedActionMarker.ActionAdded,
                                    event.module.toModule()
                                )
                            )
                        )

                    }
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
                deleteModule(event.module)
                undoHelper.updateUndoList(
                    listOf(
                        Pair(
                            PerformedActionMarker.ActionDeleted,
                            event.module.toModule()
                        )
                    )
                )
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

            MainScreenEvents.OnAddNewIncrementDropDownMenuDismiss -> {
                _state.value = state.value.copy(
                    modules = state.value.modules.map {
                        it.copy(
                            isAddNewIncrementDropdownMenuVisible = false,
                        )
                    }
                )
            }

            MainScreenEvents.OnEditIncrementDropDownMenuDismiss -> {
                _state.value = state.value.copy(
                    modules = state.value.modules.map {
                        it.copy(
                            isEditIncrementDropdownMenuVisible = false,
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

            is MainScreenEvents.OnEditIncrementation -> {
                job = null
                job = viewModelScope.launch {

                    addModule(event.newModule)
                    undoHelper.updateUndoList(
                        listOf(
                            Pair(
                                PerformedActionMarker.ActionUpdated(event.oldModule.toModule()),
                                event.newModule.toModule()
                            )
                        )
                    )
                }
            }

            is MainScreenEvents.OnUpdateModule -> {
                job = null
                job = viewModelScope.launch {
                    onUpdateModule(event.newModule, event.oldModule)
                }
            }

            is MainScreenEvents.OnAddNewIncrementationFromDate -> {
                job = null
                job = viewModelScope.launch {
                    onAddNewIncrementationFromDate(event.module, state.value.newModuleToInsert!!)
                }
            }

            is MainScreenEvents.OnAddNewIncrementation -> {
                job = null
                job = viewModelScope.launch {
                    onAddNewIncrementation(event.module)
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

            is MainScreenEvents.ActionAddNewIncrementationFromDate -> {
                _state.value = state.value.copy(newModuleToInsert = event.module)
            }

            is MainScreenEvents.OnToggleSkipped -> {
                job = null
                job = viewModelScope.launch {
                    val module = event.module.copy(isSkipped = !event.module.isSkipped)
                    addModule(module)
                    undoHelper.updateUndoList(
                        listOf(
                            Pair(
                                PerformedActionMarker.ActionUpdated(
                                    module.toModule().copy(
                                        isSkipped = !module.isSkipped
                                    )
                                ), module.toModule()
                            )
                        )
                    )
                }
            }

            is MainScreenEvents.OnModuleClick -> {
                job = null
                job = viewModelScope.launch {
                    val route = Routes.MODULE_SCREEN + "/${event.id}"
                    _uiEvent.send(UiEvent.OnNextScreen(route))
                }
            }

            MainScreenEvents.OnSearchTextClick -> {

                _state.value = state.value.copy(isSearchActive = true)
            }

            MainScreenEvents.OnSearchViewClose -> {
                _state.value = state.value.copy(isSearchActive = false)
            }

            is MainScreenEvents.OnSearchedTextEntered -> {
                _state.value = state.value.copy(searchedText = event.text)
            }

            is MainScreenEvents.OnSearchOptionChange -> {
                _state.value = state.value.copy(searchOptions = event.searchOption)
            }

            is MainScreenEvents.OnSearchOptionSectionToggle -> {
                _state.value =
                    state.value.copy(isSearchOptionsSectionVisible = !state.value.isSearchOptionsSectionVisible)
            }

            MainScreenEvents.OnConfirmFetchClick -> {
                job = null
                job = viewModelScope.launch {
                    fetchModulesFromRemote().collect { apiResult ->
                        when (apiResult) {
                            is ApiResult.Error -> {
                                _state.value = state.value.copy(isApiRequestLoading = false)
                                apiResult.message?.let {
                                    _uiEvent.send(
                                        UiEvent.ShowSnackBar(
                                            UiText.DynamicString(
                                                it
                                            )
                                        )
                                    )
                                }
                            }

                            ApiResult.Loading -> {
                                _state.value = state.value.copy(isApiRequestLoading = true)
                            }

                            is ApiResult.Success -> {
                                _state.value = state.value.copy(isApiRequestLoading = false)
                                _uiEvent.send(UiEvent.ShowSnackBar(UiText.StringResource(R.string.fetch_success)))
                            }
                        }
                    }
                }
            }

            MainScreenEvents.OnConfirmPushClick -> {
                job = null
                job = viewModelScope.launch {
                    pushModulesToRemote().collect { apiResult ->
                        when (apiResult) {
                            is ApiResult.Error -> {
                                _state.value = state.value.copy(isApiRequestLoading = false)
                                apiResult.message?.let {
                                    _uiEvent.send(
                                        UiEvent.ShowSnackBar(
                                            UiText.DynamicString(
                                                it
                                            )
                                        )
                                    )
                                }
                            }

                            ApiResult.Loading -> {
                                _state.value = state.value.copy(isApiRequestLoading = true)
                            }

                            is ApiResult.Success -> {
                                _state.value = state.value.copy(isApiRequestLoading = false)
                                _uiEvent.send(UiEvent.ShowSnackBar(UiText.StringResource(R.string.push_success)))
                            }
                        }
                    }
                }
            }

            is MainScreenEvents.OnNewCardVisible -> {
                updateLastCard(event.cardNumber)
            }

            is MainScreenEvents.OnShowAllModulesPreviewIconClick -> {
                job = null
                job = viewModelScope.launch {
                    val route = Routes.ALL_MODULES_PREVIEW_SCREEN
                    _uiEvent.send(UiEvent.OnNextScreen(route))
                }
            }

            MainScreenEvents.OnFetchButtonClick -> {
                _state.value = state.value.copy(isFetchDataDialogVisible = true)
            }

            MainScreenEvents.OnFetchDialogDismiss -> {
                _state.value = state.value.copy(isFetchDataDialogVisible = false)
            }

            MainScreenEvents.OnPushButtonClick -> {
                _state.value = state.value.copy(isPushDataDialogVisible = true)
            }

            MainScreenEvents.OnPushDialogDismiss -> {
                _state.value = state.value.copy(isPushDataDialogVisible = false)
            }

            MainScreenEvents.OnRedoClick -> {

                undoHelper.undoIndex = undoHelper.undoIndex?.plus(1) ?: 0
                undoHelper.undoIndex?.let { listIndexToAdd ->
                    undoHelper.undoList.getOrNull(listIndexToAdd)?.let { listOfPerformedActions ->
                        job = null
                        job = viewModelScope.launch {
                            listOfPerformedActions.forEach {
                                when (it.first) {
                                    PerformedActionMarker.ActionAdded -> {
                                        addModule(ModuleDisplayable(it.second))
                                    }

                                    PerformedActionMarker.ActionDeleted -> {
                                        deleteModule(ModuleDisplayable(it.second))
                                    }

                                    is PerformedActionMarker.ActionUpdated -> {
                                        addModule(ModuleDisplayable(it.second))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            MainScreenEvents.OnUndoClick -> {

                undoHelper.undoIndex?.let { listIndexToDelete ->
                    undoHelper.undoList.getOrNull(listIndexToDelete)
                        ?.let { listOfPerformedActions ->
                            job = null
                            job = viewModelScope.launch {
                                listOfPerformedActions.forEach {
                                    when (val action = it.first) {
                                        PerformedActionMarker.ActionAdded -> {
                                            deleteModule(ModuleDisplayable(it.second))
                                        }

                                        PerformedActionMarker.ActionDeleted -> {
                                            addModule(ModuleDisplayable(it.second))
                                        }

                                        is PerformedActionMarker.ActionUpdated -> {
                                            addModule(ModuleDisplayable(action.oldModule))
                                        }
                                    }
                                }
                            }
                        }
                    undoHelper.undoIndex =
                        undoHelper.undoIndex?.let { if (it == 0) null else it.minus(1) }

                    Log.d("UNDO Undo", undoHelper.undoIndex.toString())
                }

            }

            MainScreenEvents.OnToggleBottomBar -> {
                _state.value = state.value.copy(bottomBarState = !state.value.bottomBarState)
            }
        }
    }

    private suspend fun onUpdateModule(
        moduleToUpdate: ModuleDisplayable,
        oldModule: ModuleDisplayable
    ) {
        addModule(moduleToUpdate)
        undoHelper.updateUndoList(
            listOf(
                Pair(
                    PerformedActionMarker.ActionUpdated(oldModule.toModule()),
                    moduleToUpdate.toModule()
                )
            )
        )
    }

    private fun getModules() {
        viewModelScope.launch {
            useCases.getModulesUseCase().collectLatest { list ->
                _state.value = state.value.copy(modules = list.map { ModuleDisplayable(it) })
            }
        }
    }

    private suspend fun addModule(module: ModuleDisplayable) {

        useCases.addModuleUseCase(module.toModule().copy(timeStamp = System.currentTimeMillis()))
    }

    private suspend fun addModules(modules: List<ModuleDisplayable>) {

        useCases.addModulesUseCase(modules.map { it.toModule() })
    }

    private fun deleteModule(module: ModuleDisplayable) {
        job = null
        job = viewModelScope.launch {
            useCases.deleteModuleUseCase(module.toModule())
        }
    }

    private suspend fun deleteModules(module: List<ModuleDisplayable>) {
        useCases.deleteModulesUseCase(module.map { it.toModule() })
    }

    private suspend fun fetchModulesFromRemote(): Flow<ApiResult<Unit>> {
        return useCases.fetchModulesFromRemoteUseCase()
    }

    private suspend fun pushModulesToRemote(): Flow<ApiResult<Unit>> {
        return useCases.pushModulesToRemoteUseCase()
    }

    private suspend fun onAddNewIncrementationFromDate(
        eventModule: ModuleDisplayable,
        newModuleToInsert: ModuleDisplayable
    ) {

        val skippedModuleToUpdate =
            eventModule.copy(isSkipped = true, newIncrementation = null)

        val skippedModuleToInsert = newModuleToInsert.copy(
            id = UUID.randomUUID(),
            newIncrementation = eventModule.newIncrementation
        )
        if (skippedModuleToInsert.newIncrementation != null) {

            val newIncrementation =
                skippedModuleToInsert.incrementation.toInt() + skippedModuleToInsert.newIncrementation

            val newEpochDay = LocalDate.ofEpochDay(skippedModuleToInsert.epochDay)
                .plusDays(newIncrementation.toLong()).toEpochDay()

            val moduleToAdd = skippedModuleToInsert.copy(
                newIncrementation = null,
                incrementation = newIncrementation.toString(),
                epochDay = newEpochDay,
                id = UUID.randomUUID()
            )
            val modules = listOf(
                skippedModuleToUpdate,
                skippedModuleToInsert,
                moduleToAdd
            )
            addModules(modules)
            undoHelper.updateUndoList(
                listOf(
                    Pair(
                        PerformedActionMarker.ActionUpdated(
                            skippedModuleToUpdate.copy(isSkipped = !skippedModuleToUpdate.isSkipped)
                                .toModule()
                        ),
                        skippedModuleToUpdate.toModule()
                    ),
                    Pair(PerformedActionMarker.ActionAdded, skippedModuleToInsert.toModule()),
                    Pair(PerformedActionMarker.ActionAdded, moduleToAdd.toModule()),
                )
            )
        }
        _state.value = state.value.copy(newModuleToInsert = null)

    }

    private suspend fun onAddNewIncrementation(moduleToUpdate: ModuleDisplayable) {
        if (moduleToUpdate.newIncrementation != null) {
            val newIncrementation =
                moduleToUpdate.incrementation.toInt() + moduleToUpdate.newIncrementation
            val newEpochDay = LocalDate.ofEpochDay(moduleToUpdate.epochDay)
                .plusDays(newIncrementation.toLong()).toEpochDay()
            val moduleToAdd = moduleToUpdate.copy(
                newIncrementation = null,
                incrementation = newIncrementation.toString(),
                epochDay = newEpochDay,
                id = UUID.randomUUID()
            )
            val modules = listOf(moduleToUpdate, moduleToAdd)
            addModules(modules)
            undoHelper.updateUndoList(
                listOf(
                    Pair(
                        PerformedActionMarker.ActionUpdated(
                            moduleToUpdate.copy(newIncrementation = null).toModule()
                        ),
                        moduleToUpdate.toModule()
                    ),
                    Pair(PerformedActionMarker.ActionAdded, moduleToAdd.toModule())
                )
            )
        }
    }

    fun dropDatabase() {
        job = null
        job = viewModelScope.launch {
            useCases.dropDataBase()
        }
    }

    private fun updateLastCard(cardNumber: Int) {
        preferences.saveLastCard(cardNumber)
        _state.value = state.value.copy(currentPage = cardNumber)
    }

    fun isUndoButtonEnabled() = undoHelper.undoIndex != null

    fun isRedoButtonEnabled() =
        undoHelper.undoList.isNotEmpty() && undoHelper.undoIndex != undoHelper.undoList.size - 1

}