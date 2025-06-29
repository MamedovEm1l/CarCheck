package com.example.carcheck.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.carcheck.data.model.NodeChange
import com.example.carcheck.data.model.NodeParameters

@Composable
fun SubNodesDialog(
    nodeTitle: String,
    nodeParameters: List<NodeParameters>,
    nodeChanges: List<NodeChange>,
    onDismiss: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFFE5CBA5),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f) // Фиксированная высота
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .background(Color(0xFFE5CBA5))
            ) {
                // Заголовок
                Text(
                    nodeTitle,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Black
                )

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    color = Color.Black,
                    thickness = 1.dp
                )

                // Вкладки
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color(0xFFE5CBA5),
                    contentColor = Color(0xFF4D3B25)
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Параметры") }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("История") }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Прокручиваемое содержимое
                Box(modifier = Modifier
                    .weight(1f) // Занимает всё оставшееся место
                ) {
                    when (selectedTab) {
                        0 -> SubNodeList(nodeParameters)
                        1 -> NodeChangesTimeline(nodeChanges)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Кнопка "Закрыть" зафиксирована внизу
                Text(
                    text = "Закрыть",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable { onDismiss() }
                        .padding(8.dp),
                    color = Color.Black
                )
            }
        }
    }
}
