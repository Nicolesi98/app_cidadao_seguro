package br.com.fiap.afirmamais.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val name: String,
    val email: String,
    val phone: String = "",
    val city: String = "",
    val profession: String = "",
    val bio: String = "",
    val gender: String = "",
    val race: String = "",
)

fun defaultProfile(email: String, name: String): UserProfile = UserProfile(
    name = name,
    email = email,
)