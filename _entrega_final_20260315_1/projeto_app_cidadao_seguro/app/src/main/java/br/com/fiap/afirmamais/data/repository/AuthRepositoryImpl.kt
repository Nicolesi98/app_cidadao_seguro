package br.com.fiap.afirmamais.data.repository

import br.com.fiap.afirmamais.data.local.PreferencesStorage
import br.com.fiap.afirmamais.data.local.StoredUser
import br.com.fiap.afirmamais.domain.model.AuthResult
import br.com.fiap.afirmamais.domain.model.AuthUser
import br.com.fiap.afirmamais.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class AuthRepositoryImpl(
    private val storage: PreferencesStorage,
) : AuthRepository {

    override val session: Flow<AuthUser?> = storage.sessionFlow

    override suspend fun login(email: String, password: String): AuthResult {
        val normalizedEmail = email.trim().lowercase()
        val users = storage.getStoredUsers()

        val found = users.firstOrNull {
            it.email.lowercase() == normalizedEmail && it.password == password
        }

        if (found == null) {
            return AuthResult.Failure("E-mail ou senha incorretos.")
        }

        storage.setSession(AuthUser(name = found.name, email = found.email))
        return AuthResult.Success
    }

    override suspend fun register(name: String, email: String, password: String): AuthResult {
        val normalizedEmail = email.trim().lowercase()
        val users = storage.getStoredUsers().toMutableList()

        val alreadyExists = users.any { it.email.lowercase() == normalizedEmail }
        if (alreadyExists) {
            return AuthResult.Failure("Ja existe uma conta com este e-mail.")
        }

        users += StoredUser(
            name = name.trim(),
            email = email.trim(),
            password = password,
        )
        storage.saveStoredUsers(users)
        storage.setSession(AuthUser(name = name.trim(), email = email.trim()))
        return AuthResult.Success
    }

    override suspend fun logout() {
        storage.setSession(null)
    }
}