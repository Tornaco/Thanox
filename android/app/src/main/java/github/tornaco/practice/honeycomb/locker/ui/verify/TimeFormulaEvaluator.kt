package github.tornaco.practice.honeycomb.locker.ui.verify

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun evaluateTimeFormula(formula: String): String {
    val now = LocalDateTime.now()
    var result = formula
    // Limit to minute precision. Seconds are not supported.
    val tokens = listOf("yyyy", "MM", "dd", "HH", "hh", "mm")
    tokens.forEach { token ->
        val fmt = DateTimeFormatter.ofPattern(token)
        val value = now.format(fmt)
        result = result.replace(token, value)
    }
    return result
}