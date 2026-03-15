package br.com.fiap.afirmamais.presentation.screen.job

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.afirmamais.core.util.nowIsoString
import br.com.fiap.afirmamais.domain.model.Application
import br.com.fiap.afirmamais.domain.model.AuthUser
import br.com.fiap.afirmamais.domain.model.Job
import br.com.fiap.afirmamais.domain.repository.JobRepository
import br.com.fiap.afirmamais.domain.repository.UserDataRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class JobDetailsUiState(
    val loading: Boolean = true,
    val job: Job? = null,
    val isFavorited: Boolean = false,
    val isApplied: Boolean = false,
    val justApplied: Boolean = false,
)

class JobDetailsViewModel(
    private val currentUser: AuthUser,
    private val jobId: Int,
    private val jobRepository: JobRepository,
    private val userDataRepository: UserDataRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(JobDetailsUiState())
    val uiState: StateFlow<JobDetailsUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun toggleFavorite() {
        val job = _uiState.value.job ?: return

        viewModelScope.launch {
            val next = userDataRepository.toggleFavorite(currentUser.email, job.id)
            _uiState.update { it.copy(isFavorited = next) }
        }
    }

    fun apply() {
        val currentState = _uiState.value
        val job = currentState.job ?: return
        if (currentState.isApplied) {
            return
        }

        viewModelScope.launch {
            userDataRepository.addApplication(
                email = currentUser.email,
                application = Application(
                    jobId = job.id,
                    jobTitle = job.title,
                    company = job.company,
                    appliedAt = nowIsoString(),
                ),
            )

            _uiState.update {
                it.copy(
                    isApplied = true,
                    justApplied = true,
                )
            }

            delay(3_000)
            _uiState.update { it.copy(justApplied = false) }
        }
    }

    fun refreshFlags() {
        val job = _uiState.value.job ?: return

        viewModelScope.launch {
            val favorited = userDataRepository.isFavorite(currentUser.email, job.id)
            val applied = userDataRepository.isApplied(currentUser.email, job.id)
            _uiState.update {
                it.copy(
                    isFavorited = favorited,
                    isApplied = applied,
                )
            }
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }
            val job = jobRepository.fetchJobById(jobId)
            val favorited = job?.let { userDataRepository.isFavorite(currentUser.email, it.id) } ?: false
            val applied = job?.let { userDataRepository.isApplied(currentUser.email, it.id) } ?: false
            _uiState.update {
                it.copy(
                    loading = false,
                    job = job,
                    isFavorited = favorited,
                    isApplied = applied,
                )
            }
        }
    }
}