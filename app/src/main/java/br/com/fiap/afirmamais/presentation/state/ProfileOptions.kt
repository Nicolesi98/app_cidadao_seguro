package br.com.fiap.afirmamais.presentation.state

data class Option(
    val value: String,
    val label: String,
)

val genderOptions = listOf(
    Option("mulher-cis", "Mulher cisgenero"),
    Option("mulher-trans", "Mulher transgenero"),
    Option("homem-cis", "Homem cisgenero"),
    Option("homem-trans", "Homem transgenero"),
    Option("nao-binario", "Nao-binario"),
    Option("outro", "Outro"),
    Option("prefiro-nao-dizer", "Prefiro nao dizer"),
)

val raceOptions = listOf(
    Option("preta", "Preta"),
    Option("parda", "Parda"),
    Option("branca", "Branca"),
    Option("amarela", "Amarela"),
    Option("indigena", "Indigena"),
    Option("prefiro-nao-dizer", "Prefiro nao dizer"),
)

fun genderLabel(value: String): String =
    genderOptions.firstOrNull { it.value == value }?.label ?: "-"

fun raceLabel(value: String): String =
    raceOptions.firstOrNull { it.value == value }?.label ?: "-"