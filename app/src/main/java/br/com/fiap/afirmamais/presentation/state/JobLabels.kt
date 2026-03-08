package br.com.fiap.afirmamais.presentation.state

val scheduleLabels: Map<String, String> = mapOf(
    "full-time" to "Tempo integral",
    "part-time" to "Meio periodo",
)

fun scheduleLabelOf(value: String): String = scheduleLabels[value] ?: value