package br.com.fiap.afirmamais.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Application(
    val jobId: Int,
    val jobTitle: String,
    val company: String,
    val appliedAt: String,
)