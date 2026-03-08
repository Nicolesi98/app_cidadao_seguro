package br.com.fiap.afirmamais.presentation.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.fiap.afirmamais.core.theme.PinkPrimary
import br.com.fiap.afirmamais.core.theme.PurplePrimary
import br.com.fiap.afirmamais.core.util.OnResumeEffect
import br.com.fiap.afirmamais.presentation.components.JobCard
import br.com.fiap.afirmamais.presentation.state.scheduleLabelOf

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onOpenJob: (Int) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    OnResumeEffect {
        viewModel.refreshFavorites()
    }

    val filteredJobs = remember(
        uiState.jobs,
        uiState.searchTerm,
        uiState.scheduleFilter,
        uiState.cityFilter,
    ) {
        uiState.jobs.filter { job ->
            val matchesSearch = job.title.contains(uiState.searchTerm, ignoreCase = true) ||
                job.company.contains(uiState.searchTerm, ignoreCase = true)
            val matchesSchedule = uiState.scheduleFilter == "all" || job.schedule == uiState.scheduleFilter
            val matchesCity = uiState.cityFilter == "all" || job.city == uiState.cityFilter
            matchesSearch && matchesSchedule && matchesCity
        }
    }

    val cities = remember(uiState.jobs) {
        uiState.jobs.map { it.city }.distinct().sorted()
    }

    val schedules = remember(uiState.jobs) {
        uiState.jobs.map { it.schedule }.distinct()
    }

    val hasFilters = uiState.scheduleFilter != "all" || uiState.cityFilter != "all"

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            HeroCard(totalJobs = if (uiState.loading) null else uiState.jobs.size)
        }

        item {
            SearchAndFilterRow(
                searchTerm = uiState.searchTerm,
                hasFilters = hasFilters,
                onSearchChanged = viewModel::updateSearchTerm,
                onToggleFilters = viewModel::toggleFilters,
            )
        }

        if (uiState.showFilters) {
            item {
                FiltersPanel(
                    schedules = schedules,
                    selectedSchedule = uiState.scheduleFilter,
                    cities = cities,
                    selectedCity = uiState.cityFilter,
                    hasFilters = hasFilters,
                    onSelectSchedule = viewModel::updateScheduleFilter,
                    onSelectCity = viewModel::updateCityFilter,
                    onClearFilters = viewModel::clearFilters,
                )
            }
        }

        if (!uiState.loading) {
            item {
                Text(
                    text = "${filteredJobs.size} vaga${if (filteredJobs.size != 1) "s" else ""} encontrada${if (filteredJobs.size != 1) "s" else ""}",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF94A3B8),
                )
            }
        }

        when {
            uiState.loading -> {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 56.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        CircularProgressIndicator(color = PurplePrimary)
                        Text("Carregando vagas...", color = Color(0xFF94A3B8))
                    }
                }
            }

            filteredJobs.isEmpty() -> {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 56.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text("Nenhuma vaga encontrada", color = Color(0xFF475569))
                        Text("Tente outros filtros ou termos", color = Color(0xFF94A3B8))
                    }
                }
            }

            else -> {
                items(filteredJobs, key = { it.id }) { job ->
                    JobCard(
                        job = job,
                        isFavorited = uiState.favoriteIds.contains(job.id),
                        onFavoriteClick = { viewModel.toggleFavorite(job.id) },
                        onClick = { onOpenJob(job.id) },
                    )
                }
            }
        }
    }
}

@Composable
private fun HeroCard(totalJobs: Int?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(listOf(PurplePrimary, PinkPrimary)),
                shape = RoundedCornerShape(20.dp),
            )
            .padding(18.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "Vagas afirmativas",
                style = MaterialTheme.typography.labelMedium,
                color = Color(0xFFE9D5FF),
            )
            Text(
                text = "Sua proxima oportunidade esta aqui",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.White,
            )
            Text(
                text = "Para mulheres e pessoas pretas que buscam crescimento",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFF5D0FE),
            )
            if (totalJobs != null) {
                Box(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .background(Color.White.copy(alpha = 0.16f), RoundedCornerShape(10.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                ) {
                    Text(
                        text = "$totalJobs vagas disponiveis",
                        color = Color.White,
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchAndFilterRow(
    searchTerm: String,
    hasFilters: Boolean,
    onSearchChanged: (String) -> Unit,
    onToggleFilters: () -> Unit,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = searchTerm,
            onValueChange = onSearchChanged,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Buscar vaga ou empresa...") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null)
            },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
        )

        Box {
            OutlinedButton(
                onClick = onToggleFilters,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.align(Alignment.Center),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (hasFilters) PurplePrimary else Color.White,
                    contentColor = if (hasFilters) Color.White else Color(0xFF64748B),
                ),
            ) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = null,
                    tint = if (hasFilters) Color.White else Color(0xFF64748B),
                )
                Text(
                    text = "Filtros",
                    color = if (hasFilters) Color.White else Color(0xFF64748B),
                    modifier = Modifier.padding(start = 4.dp),
                )
            }
            if (hasFilters) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(10.dp)
                        .background(PinkPrimary, RoundedCornerShape(999.dp)),
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FiltersPanel(
    schedules: List<String>,
    selectedSchedule: String,
    cities: List<String>,
    selectedCity: String,
    hasFilters: Boolean,
    onSelectSchedule: (String) -> Unit,
    onSelectCity: (String) -> Unit,
    onClearFilters: () -> Unit,
) {
    var cityMenuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(20.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Tipo de contrato",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Color(0xFF64748B),
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                AssistChip(
                    onClick = { onSelectSchedule("all") },
                    label = { Text("Todos") },
                    colors = filterChipColors(selectedSchedule == "all"),
                )
                schedules.forEach { schedule ->
                    AssistChip(
                        onClick = { onSelectSchedule(schedule) },
                        label = { Text(scheduleLabelOf(schedule)) },
                        colors = filterChipColors(selectedSchedule == schedule),
                    )
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Cidade",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Color(0xFF64748B),
            )

            Box {
                OutlinedButton(
                    onClick = { cityMenuExpanded = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                ) {
                    Text(
                        text = if (selectedCity == "all") "Todas as cidades" else selectedCity,
                        modifier = Modifier.weight(1f),
                        color = Color(0xFF334155),
                    )
                }

                DropdownMenu(
                    expanded = cityMenuExpanded,
                    onDismissRequest = { cityMenuExpanded = false },
                ) {
                    DropdownMenuItem(
                        text = { Text("Todas as cidades") },
                        onClick = {
                            onSelectCity("all")
                            cityMenuExpanded = false
                        },
                    )
                    cities.forEach { city ->
                        DropdownMenuItem(
                            text = { Text(city) },
                            onClick = {
                                onSelectCity(city)
                                cityMenuExpanded = false
                            },
                        )
                    }
                }
            }
        }

        if (hasFilters) {
            TextButton(
                onClick = onClearFilters,
                modifier = Modifier.align(Alignment.Start),
            ) {
                Icon(Icons.Default.Close, contentDescription = null, tint = Color(0xFFEF4444))
                Text(
                    text = "Limpar filtros",
                    color = Color(0xFFEF4444),
                    modifier = Modifier.padding(start = 4.dp),
                )
            }
        }
    }
}

@Composable
private fun filterChipColors(selected: Boolean) = AssistChipDefaults.assistChipColors(
    containerColor = if (selected) PurplePrimary else Color(0xFFF1F5F9),
    labelColor = if (selected) Color.White else Color(0xFF475569),
)
