package com.jedisebas.tconnect.util

import java.time.LocalDate

object PseudoNull {

    fun evaluateToLong(string: String?): Long {
        if (string.isNullOrEmpty()) {
            return 0
        }
        return string.toLong()
    }

    fun evaluateToInt(string: String?): Int {
        if (string.isNullOrEmpty()) {
            return 0
        }
        return string.toInt()
    }

    fun evaluateToLocalDate(string: String?): LocalDate {
        if (string.isNullOrEmpty()) {
            return LocalDate.parse("0000-01-01")
        }
        return LocalDate.parse(string)
    }
}