package com.eltescode.modules.features.modules.presentation.utils


sealed class SearchOptions(val order: SearchOrder) {
    class Exact(order: SearchOrder) : SearchOptions(order)

    class Contains(order: SearchOrder) : SearchOptions(order)

    fun copy(order: SearchOrder): SearchOptions {
        return when (this) {
            is Exact -> Exact(order)
            is Contains -> Contains(order)
        }
    }
}

sealed interface SearchOrder {
    object Descending : SearchOrder

    object Ascending : SearchOrder
}