package com.eltescode.modules.core.preferences

import android.content.SharedPreferences
import java.time.LocalDate

class PreferencesImpl(private val sharedPreferences: SharedPreferences) : Preferences {
    override fun saveLastCardDate(date: Long) {
        sharedPreferences.edit()
            .putLong(Preferences.KEY_LAST_CARD_DATE, date)
            .apply()
    }

    override fun loadLastCardDate(): Long {
        return sharedPreferences.getLong(
            Preferences.KEY_LAST_CARD_DATE,
            LocalDate.of(2023, 12, 24).toEpochDay()
        )
    }
}