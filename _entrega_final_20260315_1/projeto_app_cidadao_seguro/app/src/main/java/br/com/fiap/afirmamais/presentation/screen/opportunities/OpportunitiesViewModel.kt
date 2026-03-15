package br.com.fiap.afirmamais.presentation.screen.opportunities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.afirmamais.domain.model.Application
import br.com.fiap.afirmamais.domain.model.AuthUser
import br.com.fiap.afirmamais.domain.model.Job
import br.com.fiap.afirmamais.domain.repository.JobRepository
import br.com.fiap.afirmamais.domain.repository.UserDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class OpportunitiesTab {
    FAVORITES,
    APPLICATIONS,
}

data class OpportunitiesUiState(
    val loading: Boolean = true,
    val tab: OpportunitiesTab = OpportunitiesTab.FAVORITES,
    val allJobs: List<Job> = emptyList(),
    val favoriteJobs: List<Job> = emptyList(),
    val applications: List<Application> = emptyList(),
)

class OpportunitiesViewModel(
    private val currentUser: AuthUser,
    private val jobRepository: JobRepository,
    private val userDataRepository: UserDataRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(OpportunitiesUiState())
    val uiState: StateFlow<OpportunitiesUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun setTab(tab: OpportunitiesTab) {
        _uiState.update { it.copy(tab = tab) }
    }

    fun refresh() {
        viewModelScope.launch {
            applyUserData(_uiState.value.allJobs)
        }
    }

    fun removeFavorite(jobId: Int) {
        viewModelScope.launch {
            userDataRepository.removeFavorite(currentUser.email, jobId)
            val jobs = _uiState.value.allJobs
            applyUserData(jobs)
        }
    }

    fun removeApplication(jobId: Int) {
        viewModelScope.launch {
            userDataRepository.removeApplication(currentUser.email, jobId)
            val jobs = _uiState.value.allJobs
            applyUserData(jobs)
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }
            val jobs = jobRepository.fetchAllJobs()
            _uiState.update { it.copy(allJobs = jobs) }
            applyUserData(jobs)
            _uiState.update { it.copy(loading = false) }
        }
    }

    private suspend fun applyUserData(jobs: List<Job>) {
        val favorites = userDataRepository.getFavorites(currentUser.email)
        val applications = userDataRepository.getApplications(currentUser.email)

        _uiState.update {
            it.copy(
                favoriteJobs = jobs.filter { job -> favorites.contains(job.id) },
                applications = applications,
            )
        }
    }
}