package br.com.fiap.afirmamais.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.afirmamais.domain.model.AuthUser
import br.com.fiap.afirmamais.domain.model.Job
import br.com.fiap.afirmamais.domain.repository.JobRepository
import br.com.fiap.afirmamais.domain.repository.UserDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val loading: Boolean = true,
    val jobs: List<Job> = emptyList(),
    val searchTerm: String = "",
    val scheduleFilter: String = "all",
    val cityFilter: String = "all",
    val showFilters: Boolean = false,
    val favoriteIds: Set<Int> = emptySet(),
)

class HomeViewModel(
    private val currentUser: AuthUser,
    private val jobRepository: JobRepository,
    private val userDataRepository: UserDataRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadJobs()
    }

    fun updateSearchTerm(term: String) {
        _uiState.update { it.copy(searchTerm = term) }
    }

    fun updateScheduleFilter(schedule: String) {
        _uiState.update { it.copy(scheduleFilter = schedule) }
    }

    fun updateCityFilter(city: String) {
        _uiState.update { it.copy(cityFilter = city) }
    }

    fun clearFilters() {
        _uiState.update { it.copy(scheduleFilter = "all", cityFilter = "all") }
    }

    fun toggleFilters() {
        _uiState.update { it.copy(showFilters = !it.showFilters) }
    }

    fun refreshFavorites() {
        viewModelScope.launch {
            val favorites = userDataRepository.getFavorites(currentUser.email).toSet()
            _uiState.update { it.copy(favoriteIds = favorites) }
        }
    }

    fun toggleFavorite(jobId: Int) {
        viewModelScope.launch {
            val isFavorite = userDataRepository.toggleFavorite(currentUser.email, jobId)
            _uiState.update { state ->
                val current = state.favoriteIds.toMutableSet()
                if (isFavorite) {
                    current.add(jobId)
                } else {
                    current.remove(jobId)
                }
                state.copy(favoriteIds = current)
            }
        }
    }

    private fun loadJobs() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }
            val jobs = jobRepository.fetchAllJobs()
            val favorites = userDataRepository.getFavorites(currentUser.email).toSet()
            _uiState.update {
                it.copy(
                    loading = false,
                    jobs = jobs,
                    favoriteIds = favorites,
                )
            }
        }
    }
}