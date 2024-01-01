package com.eltescode.modules.features.modules.presentation.utils

import com.eltescode.modules.features.modules.domain.model.Module

data class UndoHelper(
    var undoList: List<List<Pair<PerformedActionMarker, Module>>> = emptyList(),
    var undoIndex: Int? = null
) {

    fun updateUndoList(newSlot: List<Pair<PerformedActionMarker, Module>>) {
        undoIndex.let { currentUndoIndex ->

            val newList = createNewList(
                currentListPair = undoList.map { list ->
                    list.map {
                        Pair(it.first, it.second)
                    }
                },
                newSlot = newSlot.map {
                    Pair(it.first, it.second)
                },
                currentIndex = currentUndoIndex
            )
            undoList = newList.map { list ->
                list.map {
                    Pair(it.first, it.second)
                }
            }
            undoIndex = newList.size - 1

        }
        updateUndoIndex(undoList.size - 1)
    }

    private fun updateUndoIndex(newIndex: Int?) {
        undoIndex = newIndex

    }

    private fun createNewList(
        currentListPair: List<List<Pair<PerformedActionMarker, Module>>>,
        currentIndex: Int?,
        newSlot: List<Pair<PerformedActionMarker, Module>>
    ): List<List<Pair<PerformedActionMarker, Module>>> {
        val newList: MutableList<List<Pair<PerformedActionMarker, Module>>>
        val currentSize = currentListPair.size
        when (currentIndex) {
            null -> {
                newList = listOf(newSlot).toMutableList()
            }

            currentSize - 1 -> {
                newList = currentListPair.toMutableList()
                newList.add(newSlot)
            }

            else -> {
                newList = currentListPair.take(currentIndex + 1).toMutableList()
                newList.add(newSlot)
            }
        }
        return newList
    }

}