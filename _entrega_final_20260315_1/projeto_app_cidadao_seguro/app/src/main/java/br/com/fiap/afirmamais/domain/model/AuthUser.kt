package br.com.fiap.afirmamais.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthUser(
    val name: String,
    val email: String,
)

sealed interface AuthResult {
    data object Success : AuthResult
    data class Failure(val message: String) : AuthResult
}