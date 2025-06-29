package com.example.carcheck.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carcheck.data.model.CarNode
import com.example.carcheck.ui.screens.getCategoryStatusText
import com.example.carcheck.ui.screens.getStatusText

@Composable
fun NodeStatusSection(
    node: CarNode,
    onViewSubNodesClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.6f),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4D3B25))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(
                TranslateNodeCategoryToRussian(node.category.name),
                fontSize = 25.sp,
                color = Color(0xFFD9C1A0),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "Состояние узла: ${getCategoryStatusText(node.status)}",
                color = Color(0xFFD9C1A0),
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = onViewSubNodesClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD9C1A0),
                        contentColor = Color(0xFF4D3B25)
                    ),
                    shape = RoundedCornerShape(100.dp)
                ) {
                    Text(text = "Просмотреть подузлы", fontSize = 17.sp)
                }
            }
        }
    }
}
