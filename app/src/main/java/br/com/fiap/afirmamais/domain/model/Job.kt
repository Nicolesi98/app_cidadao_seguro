package br.com.fiap.afirmamais.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Job(
    val id: Int,
    val title: String,
    val company: String,
    @SerialName("company_website") val companyWebsite: String,
    @SerialName("number_of_positions") val numberOfPositions: Int,
    val city: String,
    val schedule: String,
    val salary: Int,
    val description: String,
    val requirements: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String,
)