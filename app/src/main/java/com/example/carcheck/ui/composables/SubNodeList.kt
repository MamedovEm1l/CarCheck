package com.example.carcheck.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.carcheck.data.model.NodeParameters
import com.example.carcheck.ui.screens.getCategoryStatusText

@Composable
fun SubNodeList(nodeParameters: List<NodeParameters>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column {
            // Заголовки колонок
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    "Название",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .width(100.dp)
                        .wrapContentWidth(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center
                )
                Text(
                    "Значение",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .width(70.dp)
                        .wrapContentWidth(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center
                )
                Text(
                    "Статус",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .width(110.dp)
                        .wrapContentWidth(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center
                )
            }

            // Строки параметров
            nodeParameters.forEachIndexed { index, it ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        it.name,
                        color = Color.DarkGray,
                        modifier = Modifier
                            .width(100.dp)
                            .wrapContentWidth(Alignment.CenterHorizontally),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        it.value,
                        color = Color.DarkGray,
                        modifier = Modifier
                            .width(70.dp)
                            .wrapContentWidth(Alignment.CenterHorizontally),
                        textAlign = TextAlign.Center
                    )
                    Box(
                        modifier = Modifier
                            .width(110.dp)
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    ) {
                        StatusBadge(value = getCategoryStatusText(it.status))
                    }
                }
            }
        }
    }
}