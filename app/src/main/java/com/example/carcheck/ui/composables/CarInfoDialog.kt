package com.example.carcheck.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.carcheck.data.model.Cars

@Composable
fun CarInfoDialog(
    car: Cars,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFFE5CBA5),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.28f)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .background(Color(0xFFE5CBA5))
            ) {
                // Заголовок
                Text(
                    text = "Информация о машине",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally),
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Информация
                Text(
                    "Бренд: ${car.brand}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 40.dp, bottom = 5.dp)
                        .wrapContentWidth(Alignment.Start),
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Text(
                    "Модель: ${car.model}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 40.dp, bottom = 5.dp)
                        .wrapContentWidth(Alignment.Start),
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Text(
                    "Год выпуска: ${car.year}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 40.dp, bottom = 5.dp)
                        .wrapContentWidth(Alignment.Start),
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Text(
                    "Статус: ${car.status.name}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 40.dp)
                        .wrapContentWidth(Alignment.Start),
                    fontSize = 18.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Кнопка "Закрыть"
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
