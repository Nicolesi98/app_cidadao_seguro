package br.com.fiap.afirmamais.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import br.com.fiap.afirmamais.domain.model.Application
import br.com.fiap.afirmamais.domain.model.AuthUser
import br.com.fiap.afirmamais.domain.model.UserProfile
import br.com.fiap.afirmamais.domain.model.defaultProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "afirma_preferences")

@Serializable
data class StoredUser(
    val name: String,
    val email: String,
    val password: String,
)

@Serializable
data class PersistedUserData(
    val profile: UserProfile,
    val favorites: List<Int> = emptyList(),
    val applications: List<Application> = emptyList(),
)

class PreferencesStorage(
    private val context: Context,
    private val json: Json,
) {
    private val usersKey = stringPreferencesKey("afirma_users")
    private val sessionKey = stringPreferencesKey("afirma_session")
    private val userDataKey = stringPreferencesKey("afirma_user_data")

    val sessionFlow: Flow<AuthUser?> = context.dataStore.data.map { preferences ->
        preferences[sessionKey]?.let { raw ->
            decodeOrNull<AuthUser>(raw)
        }
    }

    suspend fun getStoredUsers(): List<StoredUser> {
        val raw = context.dataStore.data.first()[usersKey] ?: return emptyList()
        return decodeOrNull<List<StoredUser>>(raw) ?: emptyList()
    }

    suspend fun saveStoredUsers(users: List<StoredUser>) {
        context.dataStore.edit { preferences ->
            preferences[usersKey] = json.encodeToString<List<StoredUser>>(users)
        }
    }

    suspend fun setSession(user: AuthUser?) {
        context.dataStore.edit { preferences ->
            if (user == null) {
                preferences.remove(sessionKey)
            } else {
                preferences[sessionKey] = json.encodeToString<AuthUser>(user)
            }
        }
    }

    suspend fun getUserData(email: String, defaultName: String): PersistedUserData {
        val normalizedEmail = email.normalizeEmail()
        val allData = getAllUserDataMutable()
        val existing = allData[normalizedEmail]

        if (existing != null) {
            return existing
        }

        val created = PersistedUserData(profile = defaultProfile(email, defaultName))
        allData[normalizedEmail] = created
        saveAllUserData(allData)
        return created
    }

    suspend fun updateUserData(email: String, userData: PersistedUserData) {
        val normalizedEmail = email.normalizeEmail()
        val allData = getAllUserDataMutable()
        allData[normalizedEmail] = userData
        saveAllUserData(allData)
    }

    private suspend fun getAllUserDataMutable(): MutableMap<String, PersistedUserData> {
        val raw = context.dataStore.data.first()[userDataKey] ?: return mutableMapOf()
        return decodeOrNull<Map<String, PersistedUserData>>(raw)?.toMutableMap() ?: mutableMapOf()
    }

    private suspend fun saveAllUserData(allData: Map<String, PersistedUserData>) {
        context.dataStore.edit { preferences ->
            preferences[userDataKey] = json.encodeToString<Map<String, PersistedUserData>>(allData)
        }
    }

    private inline fun <reified T> decodeOrNull(raw: String): T? {
        return try {
            json.decodeFromString<T>(raw)
        } catch (_: Exception) {
            null
        }
    }

    private fun String.normalizeEmail(): String = trim().lowercase()
}
