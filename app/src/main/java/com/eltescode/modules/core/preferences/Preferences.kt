package com.eltescode.modules.core.preferences

interface Preferences {

    fun saveLastCardDate(date: Long)
    fun loadLastCardDate(): Long

    companion object {
        const val KEY_LAST_CARD_DATE = "KEY_LAST_CARD_DATE"
    }

}