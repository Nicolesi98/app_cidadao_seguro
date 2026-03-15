package br.com.fiap.afirmamais.presentation.screen.opportunities

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.fiap.afirmamais.core.theme.PurplePrimary
import br.com.fiap.afirmamais.core.util.OnResumeEffect
import br.com.fiap.afirmamais.core.util.formatIsoDate
import br.com.fiap.afirmamais.core.util.formatSalary
import br.com.fiap.afirmamais.presentation.state.scheduleLabelOf

@Composable
fun OpportunitiesScreen(
    email: String,
    viewModel: OpportunitiesViewModel,
    onOpenJob: (Int) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    OnResumeEffect {
        viewModel.refresh()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Minhas Oportunidades",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF0F172A),
                )
                Text(
                    text = "Dados vinculados a $email",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF94A3B8),
                )
            }
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                SummaryCard(
                    modifier = Modifier.weight(1f),
                    value = uiState.favoriteJobs.size.toString(),
                    label = "Vagas favoritas",
                    colors = listOf(Color(0xFFEC4899), Color(0xFFF43F5E)),
                    icon = { Icon(Icons.Default.Favorite, contentDescription = null, tint = Color.White) },
                )
                SummaryCard(
                    modifier = Modifier.weight(1f),
                    value = uiState.applications.size.toString(),
                    label = "Candidaturas",
                    colors = listOf(PurplePrimary, Color(0xFF6D28D9)),
                    icon = { Icon(Icons.Default.Work, contentDescription = null, tint = Color.White) },
                )
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF1F5F9), RoundedCornerShape(16.dp))
                    .padding(4.dp),
            ) {
                TabButton(
                    selected = uiState.tab == OpportunitiesTab.FAVORITES,
                    label = "Favoritas (${uiState.favoriteJobs.size})",
                    onClick = { viewModel.setTab(OpportunitiesTab.FAVORITES) },
                    modifier = Modifier.weight(1f),
                )
                TabButton(
                    selected = uiState.tab == OpportunitiesTab.APPLICATIONS,
                    label = "Candidaturas (${uiState.applications.size})",
                    onClick = { viewModel.setTab(OpportunitiesTab.APPLICATIONS) },
                    modifier = Modifier.weight(1f),
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
                    ) {
                        CircularProgressIndicator(color = PurplePrimary)
                        Text("Carregando...", color = Color(0xFF94A3B8), modifier = Modifier.padding(top = 8.dp))
                    }
                }
            }

            uiState.tab == OpportunitiesTab.FAVORITES && uiState.favoriteJobs.isEmpty() -> {
                item {
                    EmptyState(
                        title = "Nenhuma vaga favorita ainda",
                        description = "Toque no ícone de coração para salvar vagas.",
                    )
                }
            }

            uiState.tab == OpportunitiesTab.APPLICATIONS && uiState.applications.isEmpty() -> {
                item {
                    EmptyState(
                        title = "Nenhuma candidatura enviada",
                        description = "Candidate-se às vagas e acompanhe por aqui.",
                    )
                }
            }

            uiState.tab == OpportunitiesTab.FAVORITES -> {
                items(uiState.favoriteJobs, key = { it.id }) { job ->
                    FavoriteItem(
                        company = job.company,
                        title = job.title,
                        city = job.city,
                        schedule = scheduleLabelOf(job.schedule),
                        salary = formatSalary(job.salary),
                        onRemove = { viewModel.removeFavorite(job.id) },
                        onOpen = { onOpenJob(job.id) },
                    )
                }
            }

            else -> {
                items(uiState.applications, key = { it.jobId }) { application ->
                    AppliedItem(
                        title = application.jobTitle,
                        company = application.company,
                        appliedAt = formatIsoDate(application.appliedAt),
                        onRemove = { viewModel.removeApplication(application.jobId) },
                        onOpen = { onOpenJob(application.jobId) },
                    )
                }
            }
        }
    }
}

@Composable
private fun SummaryCard(
    value: String,
    label: String,
    colors: List<Color>,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(Brush.linearGradient(colors), RoundedCornerShape(18.dp))
            .padding(14.dp),
    ) {
        icon()
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = Color.White,
            modifier = Modifier.padding(top = 8.dp),
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFFF5D0FE),
        )
    }
}

@Composable
private fun TabButton(
    selected: Boolean,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier
            .background(
                color = if (selected) Color.White else Color.Transparent,
                shape = RoundedCornerShape(12.dp),
            ),
    ) {
        Text(
            text = label,
            color = if (selected) PurplePrimary else Color(0xFF64748B),
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            ),
        )
    }
}

@Composable
private fun FavoriteItem(
    company: String,
    title: String,
    city: String,
    schedule: String,
    salary: String,
    onRemove: () -> Unit,
    onOpen: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(18.dp))
            .padding(14.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(company, style = MaterialTheme.typography.labelSmall, color = Color(0xFF94A3B8))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = Color(0xFF0F172A),
                )
            }

            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFEF4444))
            }
        }

        Row(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            SmallTag(icon = { Icon(Icons.Outlined.LocationOn, null, modifier = Modifier.padding(end = 2.dp)) }, text = city)
            SmallTag(icon = { Icon(Icons.Outlined.Schedule, null, modifier = Modifier.padding(end = 2.dp)) }, text = schedule)
            SmallTag(icon = { Icon(Icons.Default.Work, null, modifier = Modifier.padding(end = 2.dp)) }, text = "R$ $salary")
        }

        OutlinedButton(
            onClick = onOpen,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            shape = RoundedCornerShape(12.dp),
        ) {
            Text("Ver detalhes", color = PurplePrimary)
            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = PurplePrimary)
        }
    }
}

@Composable
private fun AppliedItem(
    title: String,
    company: String,
    appliedAt: String,
    onRemove: () -> Unit,
    onOpen: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(18.dp))
            .padding(14.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Candidatura enviada",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = Color(0xFF15803D),
                    modifier = Modifier
                        .background(Color(0xFFDCFCE7), RoundedCornerShape(999.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = Color(0xFF0F172A),
                    modifier = Modifier.padding(top = 8.dp),
                )
                Text(company, style = MaterialTheme.typography.labelSmall, color = Color(0xFF94A3B8))
            }

            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFEF4444))
            }
        }

        Row(
            modifier = Modifier.padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = Color(0xFF94A3B8))
            Text(
                text = appliedAt,
                style = MaterialTheme.typography.labelMedium,
                color = Color(0xFF94A3B8),
                modifier = Modifier.padding(start = 4.dp),
            )
        }

        OutlinedButton(
            onClick = onOpen,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            shape = RoundedCornerShape(12.dp),
        ) {
            Text("Ver detalhes", color = PurplePrimary)
            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = PurplePrimary)
        }
    }
}

@Composable
private fun SmallTag(
    icon: @Composable () -> Unit,
    text: String,
) {
    Row(
        modifier = Modifier
            .background(Color(0xFFF1F5F9), RoundedCornerShape(999.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        icon()
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF475569),
        )
    }
}

@Composable
private fun EmptyState(
    title: String,
    description: String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 56.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = Color(0xFF334155),
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF94A3B8),
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}
