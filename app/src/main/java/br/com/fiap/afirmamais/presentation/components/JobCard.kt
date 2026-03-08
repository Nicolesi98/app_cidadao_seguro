package br.com.fiap.afirmamais.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.People
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.fiap.afirmamais.core.theme.PinkPrimary
import br.com.fiap.afirmamais.core.theme.PurplePrimary
import br.com.fiap.afirmamais.core.util.formatSalary
import br.com.fiap.afirmamais.domain.model.Job
import br.com.fiap.afirmamais.presentation.state.scheduleLabelOf

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun JobCard(
    job: Job,
    isFavorited: Boolean,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f),
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(Color(0xFFF3E8FF), Color(0xFFFCE7F3)),
                                ),
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = job.company.firstOrNull()?.uppercase() ?: "?",
                            color = PurplePrimary,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        )
                    }

                    Spacer(modifier = Modifier.size(12.dp))

                    Column {
                        Text(
                            text = job.company,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF94A3B8),
                        )
                        Text(
                            text = job.title,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = Color(0xFF0F172A),
                        )
                    }
                }

                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isFavorited) Color(0xFFFCE7F3) else Color(0xFFF1F5F9)),
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = if (isFavorited) PinkPrimary else Color(0xFFCBD5E1),
                    )
                }
            }

            Spacer(modifier = Modifier.size(10.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                TagChip(
                    icon = {
                        Icon(Icons.Outlined.LocationOn, null, modifier = Modifier.size(14.dp))
                    },
                    text = job.city,
                    background = Color(0xFFF1F5F9),
                    color = Color(0xFF475569),
                )
                TagChip(
                    icon = {
                        Icon(Icons.Outlined.AccessTime, null, modifier = Modifier.size(14.dp))
                    },
                    text = scheduleLabelOf(job.schedule),
                    background = Color(0xFFF3E8FF),
                    color = PurplePrimary,
                )
                TagChip(
                    icon = {
                        Icon(Icons.Outlined.People, null, modifier = Modifier.size(14.dp))
                    },
                    text = "${job.numberOfPositions} vaga${if (job.numberOfPositions > 1) "s" else ""}",
                    background = Color(0xFFDCFCE7),
                    color = Color(0xFF15803D),
                )
            }

            Spacer(modifier = Modifier.size(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AttachMoney,
                        contentDescription = null,
                        tint = PurplePrimary,
                        modifier = Modifier.size(18.dp),
                    )
                    Text(
                        text = "R$ ${formatSalary(job.salary)}",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF334155),
                    )
                    Text(
                        text = "/mes",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF94A3B8),
                    )
                }
                Text(
                    text = "Ver detalhes ->",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                    color = PurplePrimary,
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(Color(0xFFF3E8FF))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                )
            }
        }
    }
}

@Composable
private fun TagChip(
    icon: @Composable () -> Unit,
    text: String,
    background: Color,
    color: Color,
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(background)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(contentAlignment = Alignment.Center) { icon() }
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = color,
        )
    }
}
