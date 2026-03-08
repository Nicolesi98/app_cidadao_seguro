package br.com.fiap.afirmamais.presentation.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.afirmamais.domain.model.AuthResult
import br.com.fiap.afirmamais.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AuthMode {
    LOGIN,
    REGISTER,
}

data class LoginUiState(
    val mode: AuthMode = AuthMode.LOGIN,
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val showPassword: Boolean = false,
    val loading: Boolean = false,
    val error: String = "",
)

sealed interface LoginEvent {
    data object Authenticated : LoginEvent
}

class LoginViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<LoginEvent>()
    val events: SharedFlow<LoginEvent> = _events.asSharedFlow()

    fun switchMode(mode: AuthMode) {
        _uiState.update {
            it.copy(
                mode = mode,
                name = "",
                email = "",
                password = "",
                error = "",
                loading = false,
            )
        }
    }

    fun updateName(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun updateEmail(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(showPassword = !it.showPassword) }
    }

    fun submit() {
        val current = _uiState.value

        if (current.loading) {
            return
        }

        if (current.email.isBlank() || current.password.isBlank()) {
            _uiState.update { it.copy(error = "Preencha todos os campos.") }
            return
        }

        if (current.mode == AuthMode.REGISTER && current.name.isBlank()) {
            _uiState.update { it.copy(error = "Informe seu nome completo.") }
            return
        }

        if (current.password.length < 6) {
            _uiState.update { it.copy(error = "A senha deve ter pelo menos 6 caracteres.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, error = "") }
            delay(500)

            val result = when (_uiState.value.mode) {
                AuthMode.LOGIN -> authRepository.login(
                    email = _uiState.value.email,
                    password = _uiState.value.password,
                )

                AuthMode.REGISTER -> authRepository.register(
                    name = _uiState.value.name,
                    email = _uiState.value.email,
                    password = _uiState.value.password,
                )
            }

            _uiState.update { it.copy(loading = false) }

            when (result) {
                is AuthResult.Success -> _events.emit(LoginEvent.Authenticated)
                is AuthResult.Failure -> _uiState.update { state -> state.copy(error = result.message) }
            }
        }
    }
}