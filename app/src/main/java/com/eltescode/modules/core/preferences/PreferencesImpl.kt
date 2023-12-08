package com.eltescode.modules.core.preferences

import android.content.SharedPreferences

class PreferencesImpl(private val sharedPreferences: SharedPreferences) : Preferences {


    override fun saveLastCard(page: Int) {
        sharedPreferences.edit()
            .putInt(Preferences.KEY_LAST_CARD, page)
            .apply()
    }

    override fun loadLastCard(): Int {
        return sharedPreferences.getInt(
            Preferences.KEY_LAST_CARD,
            36524
        )
    }
}