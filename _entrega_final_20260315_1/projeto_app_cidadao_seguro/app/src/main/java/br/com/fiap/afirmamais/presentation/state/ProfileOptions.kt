package br.com.fiap.afirmamais.presentation.state

data class Option(
    val value: String,
    val label: String,
)

val genderOptions = listOf(
    Option("mulher-cis", "Mulher cisgênero"),
    Option("mulher-trans", "Mulher transgênero"),
    Option("homem-cis", "Homem cisgênero"),
    Option("homem-trans", "Homem transgênero"),
    Option("nao-binario", "Não binário"),
    Option("outro", "Outro"),
    Option("prefiro-nao-dizer", "Prefiro não dizer"),
)

val raceOptions = listOf(
    Option("preta", "Preta"),
    Option("parda", "Parda"),
    Option("branca", "Branca"),
    Option("amarela", "Amarela"),
    Option("indigena", "Indígena"),
    Option("prefiro-nao-dizer", "Prefiro não dizer"),
)

fun genderLabel(value: String): String =
    genderOptions.firstOrNull { it.value == value }?.label ?: "-"

fun raceLabel(value: String): String =
    raceOptions.firstOrNull { it.value == value }?.label ?: "-"
