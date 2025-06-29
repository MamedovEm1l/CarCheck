package com.example.carcheck.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.carcheck.data.model.NodeChange
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun NodeChangesTimeline(changes: List<NodeChange>) {
    var selectedFilter by remember { mutableStateOf("Все") }

    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val now = LocalDate.now()

    val filteredChanges = when (selectedFilter) {
        "За месяц" -> changes.filter {
            val date = LocalDate.parse(it.changeDate.substring(0, 10))
            date >= now.minusMonths(1)
        }

        "За 3 месяца" -> changes.filter {
            val date = LocalDate.parse(it.changeDate.substring(0, 10))
            date >= now.minusMonths(3)
        }

        else -> changes
    }


    Column {
        // Фильтр
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Фильтр:", modifier = Modifier.padding(end = 8.dp), color = Color(0xFF4D3B25))

            DropdownMenuFilter(
                selected = selectedFilter,
                options = listOf("Все", "За месяц", "За 3 месяца"),
                onSelect = { selectedFilter = it }
            )
        }

        // Timeline список
        LazyColumn {
            items(filteredChanges) { change ->
                AnimatedVisibility(visible = true, enter = fadeIn(animationSpec = tween(400))) {
                    Row(modifier = Modifier.padding(vertical = 8.dp)) {
                        // Точка и линия
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(Color(0xFF4D3B25), shape = CircleShape)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(60.dp)
                                    .background(Color(0xFF4D3B25))
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFD9C1A0)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    change.changeDate,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF4D3B25)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(change.description, color = Color(0xFF3A2D1B))
                            }
                        }
                    }
                }
            }
        }
    }
}
