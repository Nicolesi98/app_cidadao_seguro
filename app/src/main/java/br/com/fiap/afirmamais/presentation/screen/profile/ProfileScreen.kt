package br.com.fiap.afirmamais.presentation.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import br.com.fiap.afirmamais.presentation.state.genderLabel
import br.com.fiap.afirmamais.presentation.state.genderOptions
import br.com.fiap.afirmamais.presentation.state.raceLabel
import br.com.fiap.afirmamais.presentation.state.raceOptions

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onLogout: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    OnResumeEffect {
        viewModel.refresh()
    }

    if (uiState.loading) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 120.dp),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(color = PurplePrimary)
        }
        return
    }

    val profile = uiState.profile
    val completion = remember(profile) {
        val values = listOf(
            profile.name,
            profile.email,
            profile.phone,
            profile.city,
            profile.profession,
            profile.bio,
            profile.gender,
            profile.race,
        )
        ((values.count { it.isNotBlank() }.toFloat() / values.size.toFloat()) * 100f).toInt()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(listOf(PurplePrimary, PinkPrimary)),
                        shape = RoundedCornerShape(20.dp),
                    )
                    .padding(18.dp),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(16.dp)),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = profile.name.firstOrNull()?.uppercase() ?: "?",
                                    color = Color.White,
                                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                                )
                            }

                            Column(modifier = Modifier.padding(start = 12.dp)) {
                                Text(
                                    text = profile.name,
                                    color = Color.White,
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                )
                                Text(
                                    text = if (profile.profession.isBlank()) {
                                        "Adicione sua profissao"
                                    } else {
                                        profile.profession
                                    },
                                    color = Color(0xFFE9D5FF),
                                    style = MaterialTheme.typography.bodySmall,
                                )
                                Text(
                                    text = profile.email,
                                    color = Color(0xFFD8B4FE),
                                    style = MaterialTheme.typography.labelSmall,
                                )
                            }
                        }

                        IconButton(
                            onClick = onLogout,
                            modifier = Modifier
                                .background(Color.White.copy(alpha = 0.14f), RoundedCornerShape(12.dp)),
                        ) {
                            Icon(Icons.Default.Logout, contentDescription = null, tint = Color.White)
                        }
                    }

                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text("Perfil completo", color = Color(0xFFE9D5FF), style = MaterialTheme.typography.labelSmall)
                            Text("$completion%", color = Color.White, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp)
                                .background(Color.White.copy(alpha = 0.24f), RoundedCornerShape(999.dp)),
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(completion / 100f)
                                    .background(Color.White, RoundedCornerShape(999.dp))
                                    .padding(vertical = 2.dp),
                            )
                        }
                    }
                }
            }
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ProfileStatCard(
                    modifier = Modifier.weight(1f),
                    icon = { Icon(Icons.Default.Favorite, null, tint = PinkPrimary) },
                    value = uiState.favoriteCount.toString(),
                    label = "Favoritas",
                )
                ProfileStatCard(
                    modifier = Modifier.weight(1f),
                    icon = { Icon(Icons.Default.Work, null, tint = PurplePrimary) },
                    value = uiState.applicationCount.toString(),
                    label = "Candidaturas",
                )
                ProfileStatCard(
                    modifier = Modifier.weight(1f),
                    icon = { Icon(Icons.Default.Star, null, tint = Color(0xFFF59E0B)) },
                    value = "$completion%",
                    label = "Completo",
                )
            }
        }

        item {
            SectionCard(
                title = "Informacoes pessoais",
                icon = { Icon(Icons.Default.Person, null, tint = PurplePrimary) },
            ) {
                ProfileField(
                    label = "Nome completo",
                    value = profile.name,
                    onValueChange = { viewModel.updateField(ProfileField.NAME, it) },
                    placeholder = "Seu nome",
                )
                ProfileField(
                    label = "E-mail",
                    value = profile.email,
                    onValueChange = {},
                    placeholder = "",
                    readOnly = true,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ProfileField(
                        label = "Telefone",
                        value = profile.phone,
                        onValueChange = { viewModel.updateField(ProfileField.PHONE, it) },
                        placeholder = "(00) 00000-0000",
                        modifier = Modifier.weight(1f),
                    )
                    ProfileField(
                        label = "Cidade",
                        value = profile.city,
                        onValueChange = { viewModel.updateField(ProfileField.CITY, it) },
                        placeholder = "Sua cidade",
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }

        item {
            SectionCard(
                title = "Informacoes profissionais",
                icon = { Icon(Icons.Default.Work, null, tint = Color(0xFF2563EB)) },
            ) {
                ProfileField(
                    label = "Profissao / area",
                    value = profile.profession,
                    onValueChange = { viewModel.updateField(ProfileField.PROFESSION, it) },
                    placeholder = "Ex: Desenvolvedora de Software",
                )
                ProfileField(
                    label = "Sobre voce",
                    value = profile.bio,
                    onValueChange = { viewModel.updateField(ProfileField.BIO, it) },
                    placeholder = "Sua experiencia e objetivos...",
                    singleLine = false,
                )
            }
        }

        item {
            SectionCard(
                title = "Vagas afirmativas",
                icon = { Icon(Icons.Default.CheckCircle, null, tint = PinkPrimary) },
                subtitle = "Nos ajuda a encontrar oportunidades para voce",
            ) {
                SelectField(
                    label = "Identidade de genero",
                    value = profile.gender,
                    displayValue = if (profile.gender.isBlank()) "Selecione" else genderLabel(profile.gender),
                    options = genderOptions.map { it.value to it.label },
                    onSelect = { viewModel.updateField(ProfileField.GENDER, it) },
                )

                SelectField(
                    label = "Raca/Cor",
                    value = profile.race,
                    displayValue = if (profile.race.isBlank()) "Selecione" else raceLabel(profile.race),
                    options = raceOptions.map { it.value to it.label },
                    onSelect = { viewModel.updateField(ProfileField.RACE, it) },
                )

                if (profile.gender.isNotBlank() || profile.race.isNotBlank()) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (profile.gender.isNotBlank()) {
                            Text(
                                text = genderLabel(profile.gender),
                                color = PurplePrimary,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier
                                    .background(Color(0xFFF3E8FF), RoundedCornerShape(999.dp))
                                    .padding(horizontal = 10.dp, vertical = 5.dp),
                            )
                        }
                        if (profile.race.isNotBlank()) {
                            Text(
                                text = raceLabel(profile.race),
                                color = PinkPrimary,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier
                                    .background(Color(0xFFFCE7F3), RoundedCornerShape(999.dp))
                                    .padding(horizontal = 10.dp, vertical = 5.dp),
                            )
                        }
                    }
                }
            }
        }

        item {
            OutlinedButton(
                onClick = viewModel::save,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(18.dp),
            ) {
                Icon(
                    imageVector = if (uiState.saved) Icons.Default.CheckCircle else Icons.Default.Save,
                    contentDescription = null,
                    tint = if (uiState.saved) Color(0xFF22C55E) else PurplePrimary,
                )
                Text(
                    text = if (uiState.saved) "Perfil salvo com sucesso!" else "Salvar perfil",
                    color = if (uiState.saved) Color(0xFF22C55E) else PurplePrimary,
                    modifier = Modifier.padding(start = 8.dp),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                )
            }
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    icon: @Composable () -> Unit,
    subtitle: String? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(18.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(Color(0xFFF1F5F9), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center,
            ) {
                icon()
            }
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = Color(0xFF1E293B),
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF94A3B8),
                    )
                }
            }
        }

        content()
    }
}

@Composable
private fun ProfileField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    readOnly: Boolean = false,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        singleLine = singleLine,
        readOnly = readOnly,
        minLines = if (singleLine) 1 else 3,
        maxLines = if (singleLine) 1 else 5,
        shape = RoundedCornerShape(14.dp),
    )
}

@Composable
private fun SelectField(
    label: String,
    value: String,
    displayValue: String,
    options: List<Pair<String, String>>,
    onSelect: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(label, style = MaterialTheme.typography.labelMedium, color = Color(0xFF64748B))
        Box {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                shape = RoundedCornerShape(14.dp),
            ) {
                Text(displayValue, color = Color(0xFF334155), modifier = Modifier.weight(1f))
            }

            androidx.compose.material3.DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                options.forEach { option ->
                    androidx.compose.material3.DropdownMenuItem(
                        text = { Text(option.second) },
                        onClick = {
                            onSelect(option.first)
                            expanded = false
                        },
                    )
                }
            }
        }

        if (value.isNotBlank()) {
            TextButton(onClick = { onSelect("") }) {
                Text("Limpar", color = Color(0xFFEF4444))
            }
        }
    }
}

@Composable
private fun ProfileStatCard(
    value: String,
    label: String,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        icon()
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF0F172A),
            modifier = Modifier.padding(top = 6.dp),
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF94A3B8),
        )
    }
}
