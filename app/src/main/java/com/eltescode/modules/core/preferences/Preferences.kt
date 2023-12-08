package com.eltescode.modules.core.preferences

interface Preferences {

    fun saveLastCard(page: Int)
    fun loadLastCard(): Int

    companion object {
        const val KEY_LAST_CARD = "KEY_LAST_CARD"
    }

}