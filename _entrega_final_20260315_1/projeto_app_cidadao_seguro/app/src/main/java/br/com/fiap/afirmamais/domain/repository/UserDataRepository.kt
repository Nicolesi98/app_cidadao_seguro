package br.com.fiap.afirmamais.domain.repository

import br.com.fiap.afirmamais.domain.model.Application
import br.com.fiap.afirmamais.domain.model.UserProfile

interface UserDataRepository {
    suspend fun getProfile(email: String, name: String): UserProfile
    suspend fun saveProfile(email: String, profile: UserProfile)

    suspend fun getFavorites(email: String): List<Int>
    suspend fun isFavorite(email: String, jobId: Int): Boolean
    suspend fun toggleFavorite(email: String, jobId: Int): Boolean
    suspend fun removeFavorite(email: String, jobId: Int)

    suspend fun getApplications(email: String): List<Application>
    suspend fun isApplied(email: String, jobId: Int): Boolean
    suspend fun addApplication(email: String, application: Application)
    suspend fun removeApplication(email: String, jobId: Int)
}