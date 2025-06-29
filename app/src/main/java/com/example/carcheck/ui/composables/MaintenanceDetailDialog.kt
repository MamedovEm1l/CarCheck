package com.example.carcheck.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.carcheck.data.model.MaintenanceNodeTask
import com.example.carcheck.data.model.MaintenanceRecord
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MaintenanceDetailDialog(
    maintenanceRecords: List<MaintenanceRecord>,
    currentIndex: Int,
    tasks: List<MaintenanceNodeTask>,
    onClose: () -> Unit,
    onNavigate: (Int) -> Unit
) {
    val record = maintenanceRecords[currentIndex]

    val isoDate = record.date
    val dueDate = try {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        isoDate.let { formatter.format(parser.parse(it)!!) }
    } catch (e: Exception) {
        SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date()) // fallback
    }

    val groupedTasks = remember(record.id, tasks) {
        tasks
            .filter { it.maintenanceId == record.id }
            .groupBy { it.category ?: "Без категории" }
            .toList()
    }

    Dialog(onDismissRequest = onClose) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFFE5CBA5),
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Заголовок
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "ОТЧЕТ ТО $dueDate",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4D3227)
                    )
                }

                Divider(color = Color(0xFF4D3227), thickness = 1.dp)

                // Прокручиваемая область
                Box(
                    modifier = Modifier
                        .weight(1f) // занимает всё оставшееся пространство
                ) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()

                    ) {
                        items(groupedTasks) { (category, taskList) ->
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color(0xFFD2B58D),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = TranslateNodeCategoryToRussian(category),
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF2B1B12)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    taskList.forEach { task ->
                                        Row(
                                            verticalAlignment = Alignment.Top,
                                            modifier = Modifier.padding(bottom = 4.dp)
                                        ) {
                                            Text("• ", color = Color(0xFF852E0C))
                                            Text(task.taskDescription, color = Color.Black)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Кнопки
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(
                        onClick = {
                            if (currentIndex > 0) onNavigate(currentIndex - 1)
                        },
                        enabled = currentIndex > 0
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Предыдущий",
                            tint = Color(0xFF4D3227)
                        )
                    }

                    Button(
                        onClick = onClose,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE6C39C)
                        ),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text("ЗАКРЫТЬ", color = Color(0xFF852E0C))
                    }

                    IconButton(
                        onClick = {
                            if (currentIndex < maintenanceRecords.lastIndex) onNavigate(currentIndex + 1)
                        },
                        enabled = currentIndex < maintenanceRecords.lastIndex
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Следующий",
                            tint = Color(0xFF4D3227)
                        )
                    }
                }
            }
        }
    }
}
