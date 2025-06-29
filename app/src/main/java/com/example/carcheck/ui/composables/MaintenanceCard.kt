package com.example.carcheck.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carcheck.data.model.MaintenanceRecord
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MaintenanceCard(record: MaintenanceRecord, onClick: () -> Unit) {
    val isoDate = record.date
    val dueDate = try {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        isoDate.let { formatter.format(parser.parse(it)!!) }
    } catch (e: Exception) {
        SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date()) // fallback
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFFA726), shape = RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Text(
            text = "Отчёт ТО от $dueDate",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF3A2D1B)
        )
    }
}