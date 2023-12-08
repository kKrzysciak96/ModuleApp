package com.eltescode.modules.core.extensions

import java.time.LocalDate
import kotlin.math.absoluteValue

fun Int.calculateDateUponGivenHorizontalPagerPage(): LocalDate {
    val dayCorrection = this - 36524
    return if (dayCorrection < 0) {
        LocalDate.of(2023, 12, 24)
            .minusDays((dayCorrection * 13).absoluteValue.toLong())
    } else {
        LocalDate.of(2023, 12, 24).plusDays((dayCorrection * 13).toLong())
    }
}