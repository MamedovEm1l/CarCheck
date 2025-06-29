package com.example.carcheck.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.carcheck.data.model.CarNode
import com.example.carcheck.data.model.Cars
import com.example.carcheck.ui.composables.ErrorDialog
import com.example.carcheck.viewmodel.CarViewModel

val BrownCard = Color(0xFF3A2D1B)
val BrownText = Color(0xFFFBE9E7)
val BrownAccent = Color(0xFF8D6E63)

@Composable
fun SelectCarScreen(
    navController: NavController = rememberNavController(),
    viewModel: CarViewModel = hiltViewModel()
) {
    val cars by viewModel.cars.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchCars()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD9C1A0))
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        )
        {
            Text(
                text = "Выбор автомобиля",
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                color = BrownCard,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, bottom = 40.dp)
                    .wrapContentWidth(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center

            )
        }

        if (cars == null) {
            Spacer(modifier = Modifier.weight(1f))
            CircularProgressIndicator(color = BrownAccent)
            Spacer(modifier = Modifier.weight(1f))
        } else if (errorMessage != null) {
            Spacer(modifier = Modifier.weight(1f))
            ErrorDialog(
                message = errorMessage!!,
                onDismiss = {
                    viewModel.fetchCars()
                    viewModel.clearError()
                }
            )
            Spacer(modifier = Modifier.weight(1f))
        } else if (cars!!.isEmpty()) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Нет доступных автомобилей",
                style = MaterialTheme.typography.bodyLarge,
                color = BrownCard
            )
            Spacer(modifier = Modifier.weight(1f))
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LazyColumn(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(cars!!) { car ->
                        CarItem(car = car) {
                            viewModel.selectCar(car)
                            navController.navigate("car_detail")
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun CarItem(car: Cars, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = BrownCard
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${car.brand} ${car.model} (${car.year})",
                style = MaterialTheme.typography.titleMedium,
                color = BrownText
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Статус: ${getStatusText(car.status)}",
                style = MaterialTheme.typography.bodyMedium,
                color = getStatusColor(car.status)
            )
        }
    }
}


@Composable
fun getStatusColor(status: Cars.CarStatus): Color {
    return when (status) {
        Cars.CarStatus.OK -> Color(0xFF81C784)  // зелёный
        Cars.CarStatus.WARNING -> Color(0xFFFFD54F)  // янтарный
        Cars.CarStatus.ERROR -> Color(0xFFE57373)  // красный
    }
}

fun getStatusText(status: Cars.CarStatus): String {
    return when (status) {
        Cars.CarStatus.OK -> "Исправен"
        Cars.CarStatus.WARNING -> "Требует проверки"
        Cars.CarStatus.ERROR -> "Требует ремонта"
    }
}
fun getCategoryStatusText(status: CarNode.NodeStatus): String {
    return when (status) {
        CarNode.NodeStatus.NORMAL -> "нормально"
        CarNode.NodeStatus.NEEDS_CHECK-> "требуется проверка"
        CarNode.NodeStatus.BROKEN -> "неисправен"
    }
}