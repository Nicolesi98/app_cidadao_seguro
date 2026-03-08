package br.com.fiap.afirmamais.core.util

import java.text.NumberFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private val brazil = Locale("pt", "BR")
private val salaryFormatter = NumberFormat.getIntegerInstance(brazil)
private val dateFormatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", brazil)
private val shortDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", brazil)

fun formatSalary(value: Int): String = salaryFormatter.format(value)

fun formatIsoDate(date: String): String {
    return try {
        val instant = Instant.parse(date)
        dateFormatter.format(instant.atZone(ZoneId.systemDefault()))
    } catch (_: Exception) {
        date
    }
}

fun formatIsoDateShort(date: String): String {
    return try {
        val instant = Instant.parse(date)
        shortDateFormatter.format(instant.atZone(ZoneId.systemDefault()))
    } catch (_: Exception) {
        date
    }
}

fun nowIsoString(): String = Instant.now().toString()