package com.example.carcheck.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import java.time.LocalDate

@Composable
fun CompactDateRangeFilterDialog(
    initialStartDate: LocalDate?,
    initialEndDate: LocalDate?,
    onDismiss: () -> Unit,
    onDateRangeSelected: (LocalDate?, LocalDate?) -> Unit
) {
    var startDate by remember { mutableStateOf(initialStartDate ?: LocalDate.now().minusMonths(1)) }
    var endDate by remember { mutableStateOf(initialEndDate ?: LocalDate.now()) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFFE5CBA5),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .background(Color(0xFFE5CBA5))
            ) {
                // Заголовок
                Text(
                    text = "Выбор диапазона дат",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )

                // Поля ввода дат
                Text("С:", fontSize = 18.sp, color = Color.Black, modifier = Modifier.padding(start = 8.dp, bottom = 4.dp))
                DateInputField(date = startDate) {
                    startDate = it
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("По:", fontSize = 18.sp, color = Color.Black, modifier = Modifier.padding(start = 8.dp, bottom = 4.dp))
                DateInputField(date = endDate) {
                    endDate = it
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Кнопки
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = "Отмена",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        modifier = Modifier
                            .clickable { onDismiss() }
                            .padding(8.dp)
                    )
                    Text(
                        text = "Применить",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        modifier = Modifier
                            .clickable {
                                onDateRangeSelected(startDate, endDate)
                                onDismiss()
                            }
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DateInputField(date: LocalDate, onDateSelected: (LocalDate) -> Unit) {
    val context = LocalContext.current
    val datePickerDialog = remember {
        android.app.DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                onDateSelected(LocalDate.of(year, month + 1, dayOfMonth))
            },
            date.year,
            date.monthValue - 1,
            date.dayOfMonth
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF5E3C4)) // Бежевый под цвет фона
            .clickable { datePickerDialog.show() }
            .padding(12.dp)
    ) {
        Text(text = date.toString(), color = Color.Black)
    }
}
