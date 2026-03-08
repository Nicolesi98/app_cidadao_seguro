package br.com.fiap.afirmamais.presentation.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.afirmamais.domain.model.AuthUser
import br.com.fiap.afirmamais.domain.model.UserProfile
import br.com.fiap.afirmamais.domain.repository.UserDataRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
    val loading: Boolean = true,
    val profile: UserProfile = UserProfile(name = "", email = ""),
    val favoriteCount: Int = 0,
    val applicationCount: Int = 0,
    val saved: Boolean = false,
)

class ProfileViewModel(
    private val currentUser: AuthUser,
    private val userDataRepository: UserDataRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun refresh() {
        load()
    }

    fun updateField(field: ProfileField, value: String) {
        _uiState.update { state ->
            val profile = state.profile
            val updated = when (field) {
                ProfileField.NAME -> profile.copy(name = value)
                ProfileField.PHONE -> profile.copy(phone = value)
                ProfileField.CITY -> profile.copy(city = value)
                ProfileField.PROFESSION -> profile.copy(profession = value)
                ProfileField.BIO -> profile.copy(bio = value)
                ProfileField.GENDER -> profile.copy(gender = value)
                ProfileField.RACE -> profile.copy(race = value)
            }
            state.copy(profile = updated)
        }
    }

    fun save() {
        viewModelScope.launch {
            val profile = _uiState.value.profile
            userDataRepository.saveProfile(currentUser.email, profile)
            _uiState.update { it.copy(saved = true) }
            delay(3_000)
            _uiState.update { it.copy(saved = false) }
            refreshCounts()
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }

            val profile = userDataRepository.getProfile(
                email = currentUser.email,
                name = currentUser.name,
            )
            val favorites = userDataRepository.getFavorites(currentUser.email)
            val applications = userDataRepository.getApplications(currentUser.email)

            _uiState.update {
                it.copy(
                    loading = false,
                    profile = profile,
                    favoriteCount = favorites.size,
                    applicationCount = applications.size,
                )
            }
        }
    }

    private suspend fun refreshCounts() {
        val favorites = userDataRepository.getFavorites(currentUser.email)
        val applications = userDataRepository.getApplications(currentUser.email)
        _uiState.update {
            it.copy(
                favoriteCount = favorites.size,
                applicationCount = applications.size,
            )
        }
    }
}

enum class ProfileField {
    NAME,
    PHONE,
    CITY,
    PROFESSION,
    BIO,
    GENDER,
    RACE,
}