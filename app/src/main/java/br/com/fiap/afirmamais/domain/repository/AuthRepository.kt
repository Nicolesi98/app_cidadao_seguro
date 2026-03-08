package br.com.fiap.afirmamais.domain.repository

import br.com.fiap.afirmamais.domain.model.AuthResult
import br.com.fiap.afirmamais.domain.model.AuthUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val session: Flow<AuthUser?>

    suspend fun login(email: String, password: String): AuthResult
    suspend fun register(name: String, email: String, password: String): AuthResult
    suspend fun logout()
}