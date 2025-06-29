package com.example.carcheck.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun StatusBadge(value: String) {
    val (bgColor, textColor) = when (value.lowercase()) {
        "нормально" -> Color(0xFFD2F0D2) to Color(0xFF2E7D32)
        "требуется проверка" -> Color(0xFFFFE0B2) to Color(0xFFE65100)
        "неисправен" -> Color(0xFFFFCDD2) to Color(0xFFC62828)
        else -> Color.LightGray to Color.DarkGray
    }

    Box(
        modifier = Modifier
            .background(color = bgColor, shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = value,
            color = textColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
