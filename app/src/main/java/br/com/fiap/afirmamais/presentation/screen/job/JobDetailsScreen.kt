package br.com.fiap.afirmamais.presentation.screen.job

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.fiap.afirmamais.core.theme.PinkPrimary
import br.com.fiap.afirmamais.core.theme.PurplePrimary
import br.com.fiap.afirmamais.core.util.OnResumeEffect
import br.com.fiap.afirmamais.core.util.formatIsoDateShort
import br.com.fiap.afirmamais.core.util.formatSalary
import br.com.fiap.afirmamais.presentation.state.scheduleLabelOf

@Composable
fun JobDetailsScreen(
    viewModel: JobDetailsViewModel,
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    OnResumeEffect {
        viewModel.refreshFlags()
    }

    when {
        uiState.loading -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 120.dp),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = PurplePrimary)
                    Text("Carregando vaga...", color = Color(0xFF94A3B8), modifier = Modifier.padding(top = 10.dp))
                }
            }
        }

        uiState.job == null -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text("Vaga nao encontrada", color = Color(0xFF475569))
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
                ) {
                    Text("Ver todas as vagas")
                }
            }
        }

        else -> {
            val job = uiState.job ?: return
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.horizontalGradient(listOf(PurplePrimary, PinkPrimary)),
                            )
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = onBack,
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.15f)),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                shape = RoundedCornerShape(12.dp),
                            ) {
                                Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                                Text("Voltar", color = Color.White, modifier = Modifier.padding(start = 4.dp))
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top,
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(job.company, color = Color(0xFFE9D5FF), style = MaterialTheme.typography.bodySmall)
                                    Text(
                                        text = job.title,
                                        color = Color.White,
                                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                                    )
                                }

                                IconButton(
                                    onClick = viewModel::toggleFavorite,
                                    modifier = Modifier
                                        .background(
                                            color = if (uiState.isFavorited) {
                                                Color.White.copy(alpha = 0.26f)
                                            } else {
                                                Color.White.copy(alpha = 0.15f)
                                            },
                                            shape = RoundedCornerShape(12.dp),
                                        ),
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Favorite,
                                        contentDescription = null,
                                        tint = if (uiState.isFavorited) Color.White else Color(0xFFE9D5FF),
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            InfoCard(
                                title = "Localizacao",
                                value = job.city,
                                icon = { Icon(Icons.Default.LocationOn, null, tint = PurplePrimary) },
                                modifier = Modifier.weight(1f),
                            )
                            InfoCard(
                                title = "Contrato",
                                value = scheduleLabelOf(job.schedule),
                                icon = { Icon(Icons.Outlined.AccessTime, null, tint = Color(0xFF2563EB)) },
                                modifier = Modifier.weight(1f),
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            InfoCard(
                                title = "Salario",
                                value = "R$ ${formatSalary(job.salary)}",
                                icon = { Icon(Icons.Default.AttachMoney, null, tint = Color(0xFF16A34A)) },
                                modifier = Modifier.weight(1f),
                            )
                            InfoCard(
                                title = "Vagas",
                                value = "${job.numberOfPositions} posicao${if (job.numberOfPositions > 1) "es" else ""}",
                                icon = { Icon(Icons.Default.People, null, tint = Color(0xFFDB2777)) },
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                }

                item {
                    SectionCard(
                        title = "Sobre a vaga",
                        content = {
                            Text(job.description, color = Color(0xFF64748B), style = MaterialTheme.typography.bodyMedium)
                        },
                    )
                }

                item {
                    SectionCard(
                        title = "Requisitos",
                        content = {
                            val requirements = job.requirements.split("\n").map { it.trim() }.filter { it.isNotBlank() }
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                requirements.forEach { requirement ->
                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Text("-", color = PurplePrimary)
                                        Text(
                                            text = requirement.removePrefix("-").removePrefix("*").trim(),
                                            color = Color(0xFF64748B),
                                            style = MaterialTheme.typography.bodyMedium,
                                        )
                                    }
                                }
                            }
                        },
                    )
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = Color(0xFF94A3B8), modifier = Modifier.size(16.dp))
                        Text(
                            text = "Publicada em ${formatIsoDateShort(job.createdAt)}",
                            color = Color(0xFF94A3B8),
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(start = 6.dp),
                        )
                    }
                }

                item {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Button(
                            onClick = viewModel::apply,
                            enabled = !uiState.isApplied,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (uiState.isApplied) Color(0xFF22C55E) else PurplePrimary,
                                disabledContainerColor = Color(0xFF22C55E),
                            ),
                        ) {
                            if (uiState.isApplied) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null)
                                Text(
                                    text = if (uiState.justApplied) "Candidatura enviada!" else "Ja candidatada(o)",
                                    modifier = Modifier.padding(start = 8.dp),
                                )
                            } else {
                                Text("Candidatar-se agora")
                            }
                        }

                        OutlinedButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(job.companyWebsite))
                                runCatching { context.startActivity(intent) }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                        ) {
                            Icon(Icons.Default.Language, contentDescription = null, tint = PurplePrimary)
                            Text("Site da empresa", color = PurplePrimary, modifier = Modifier.padding(start = 8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoCard(
    title: String,
    value: String,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(14.dp))
            .padding(12.dp),
    ) {
        icon()
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF94A3B8),
            modifier = Modifier.padding(top = 6.dp),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = Color(0xFF334155),
        )
    }
}

@Composable
private fun SectionCard(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(Color.White, RoundedCornerShape(18.dp))
            .padding(14.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
            color = Color(0xFF1E293B),
            modifier = Modifier.padding(bottom = 10.dp),
        )
        content()
    }
}
