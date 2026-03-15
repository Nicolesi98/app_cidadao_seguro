package br.com.fiap.afirmamais.data.repository

import br.com.fiap.afirmamais.data.local.PreferencesStorage
import br.com.fiap.afirmamais.domain.model.Application
import br.com.fiap.afirmamais.domain.model.UserProfile
import br.com.fiap.afirmamais.domain.repository.UserDataRepository

class UserDataRepositoryImpl(
    private val storage: PreferencesStorage,
) : UserDataRepository {

    override suspend fun getProfile(email: String, name: String): UserProfile {
        val userData = storage.getUserData(email = email, defaultName = name)
        val profile = userData.profile

        return profile.copy(
            name = profile.name.ifBlank { name },
            email = profile.email.ifBlank { email },
        )
    }

    override suspend fun saveProfile(email: String, profile: UserProfile) {
        val userData = storage.getUserData(email = email, defaultName = profile.name)
        storage.updateUserData(email, userData.copy(profile = profile))
    }

    override suspend fun getFavorites(email: String): List<Int> {
        return storage.getUserData(email = email, defaultName = "").favorites
    }

    override suspend fun isFavorite(email: String, jobId: Int): Boolean {
        return getFavorites(email).contains(jobId)
    }

    override suspend fun toggleFavorite(email: String, jobId: Int): Boolean {
        val userData = storage.getUserData(email = email, defaultName = "")
        val favorites = userData.favorites.toMutableList()

        val isFavorite = favorites.contains(jobId)
        if (isFavorite) {
            favorites.remove(jobId)
        } else {
            favorites.add(jobId)
        }

        storage.updateUserData(email, userData.copy(favorites = favorites))
        return !isFavorite
    }

    override suspend fun removeFavorite(email: String, jobId: Int) {
        val userData = storage.getUserData(email = email, defaultName = "")
        val favorites = userData.favorites.filterNot { it == jobId }
        storage.updateUserData(email, userData.copy(favorites = favorites))
    }

    override suspend fun getApplications(email: String): List<Application> {
        return storage.getUserData(email = email, defaultName = "").applications
    }

    override suspend fun isApplied(email: String, jobId: Int): Boolean {
        return getApplications(email).any { it.jobId == jobId }
    }

    override suspend fun addApplication(email: String, application: Application) {
        val userData = storage.getUserData(email = email, defaultName = "")
        val exists = userData.applications.any { it.jobId == application.jobId }
        if (exists) {
            return
        }

        val updated = userData.applications + application
        storage.updateUserData(email, userData.copy(applications = updated))
    }

    override suspend fun removeApplication(email: String, jobId: Int) {
        val userData = storage.getUserData(email = email, defaultName = "")
        val updated = userData.applications.filterNot { it.jobId == jobId }
        storage.updateUserData(email, userData.copy(applications = updated))
    }
}