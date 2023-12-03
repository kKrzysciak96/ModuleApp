package com.eltescode.modules.core.utils

import android.content.Context

sealed interface UiText {
    data class StringResource(val stringResId: Int) : UiText
    data class DynamicString(val text: String) : UiText

    fun asString(context: Context): String {
        return when (this) {
            is StringResource -> context.getString(this.stringResId)
            is DynamicString -> this.text
        }
    }
}