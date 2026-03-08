package br.com.fiap.afirmamais.presentation.screen.about

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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.fiap.afirmamais.core.theme.PinkPrimary
import br.com.fiap.afirmamais.core.theme.PurplePrimary

@Composable
fun AboutScreen() {
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
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Sobre o projeto", color = Color(0xFFE9D5FF), style = MaterialTheme.typography.labelMedium)
                    Text(
                        text = "Afirma+",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    )
                    Text(
                        text = "Conectando talento e oportunidade com diversidade e inclusao no mercado de trabalho.",
                        color = Color(0xFFF5D0FE),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }

        item {
            InfoCard(
                icon = {
                    Icon(Icons.Default.TrackChanges, contentDescription = null, tint = PurplePrimary)
                },
                title = "Nossa missao",
                description = "Criar pontes entre empresas e profissionais que historicamente enfrentam barreiras no mercado.",
            )
        }

        item {
            InfoCard(
                icon = { Icon(Icons.Default.Favorite, contentDescription = null, tint = Color(0xFFEC4899)) },
                title = "Inclusao",
                description = "Promovemos ambientes onde todas as pessoas se sintam valorizadas.",
            )
        }

        item {
            InfoCard(
                icon = { Icon(Icons.Default.People, contentDescription = null, tint = Color(0xFF7C3AED)) },
                title = "Equidade",
                description = "Garantimos oportunidades mais justas para grupos historicamente marginalizados.",
            )
        }

        item {
            InfoCard(
                icon = { Icon(Icons.Default.ShowChart, contentDescription = null, tint = Color(0xFF4F46E5)) },
                title = "Crescimento",
                description = "Conectamos pessoas a oportunidades de desenvolvimento de carreira.",
            )
        }

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
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Nosso impacto",
                        color = Color.White,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 14.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        Stat(value = "500+", label = "Vagas")
                        Stat(value = "150+", label = "Empresas")
                        Stat(value = "2k+", label = "Cadastradas")
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoCard(
    icon: @Composable () -> Unit,
    title: String,
    description: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(18.dp))
            .padding(14.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFFF1F5F9), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center,
        ) {
            icon()
        }

        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = Color(0xFF0F172A),
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF64748B),
            )
        }
    }
}

@Composable
private fun Stat(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = Color.White,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFFE9D5FF),
        )
    }
}