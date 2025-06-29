package com.example.carcheck.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.carcheck.R
import com.example.carcheck.data.model.MaintenanceNodeTask
import com.example.carcheck.data.model.MaintenanceRecord
import com.example.carcheck.ui.composables.CompactDateRangeFilterDialog
import com.example.carcheck.ui.composables.MaintenanceCard
import com.example.carcheck.ui.composables.MaintenanceDetailDialog
import com.example.carcheck.ui.composables.TranslateNodeCategoryToRussian
import com.example.carcheck.ui.screens.drawer.DrawerBody
import com.example.carcheck.ui.screens.drawer.DrawerHeader
import com.example.carcheck.viewmodel.CarViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate

@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchiveMaintenanceScreen(
    navController: NavController = rememberNavController(),
    viewModel: CarViewModel = hiltViewModel()
) {
    val maintenanceRecords by viewModel.maintenanceRecords.collectAsState()
    val tasks by viewModel.nodeTasks.collectAsState()
    val car by viewModel.selectedCar.collectAsState()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    var currentIndex by remember { mutableStateOf<Int?>(null) }

    // Состояния для выбора диапазона дат
    var showCompactDialog by remember { mutableStateOf(false) }
    var selectedStartDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedEndDate by remember { mutableStateOf<LocalDate?>(null) }

    LaunchedEffect(car?.id) {
        car?.id?.let { carId ->
            if (viewModel.maintenanceRecords.value == null) {
                viewModel.fetchNodes(carId)
                viewModel.fetchMaintenanceRecords(carId)
                viewModel.fetchAllNodeTasksForCar(carId)
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(modifier = Modifier.fillMaxWidth(0.7f)) {
                DrawerHeader(car?.model ?: "Не выбран")
                DrawerBody(navController)
            }
        }
    ) {
        Scaffold(
            modifier = Modifier
                .background(Color(0xFFD9C1A0))
                .fillMaxSize()
                .padding(10.dp),
            topBar = {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp, start = 8.dp, end = 8.dp)
                    ) {
                        IconButton(
                            modifier = Modifier.size(50.dp),
                            onClick = { scope.launch { drawerState.open() } }
                        ) {
                            Icon(Icons.Default.Menu, contentDescription = "Меню")
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        IconButton(
                            onClick = { showCompactDialog = true },
                            modifier = Modifier.size(35.dp)
                                .padding(top = 3.dp, end = 10.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.filter_icon),
                                contentDescription = "Фильтр"
                            )
                        }
                        Text(
                            "Фильтр",
                            fontSize = 20.sp,
                            color = Color(0xFF3A2D1B),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "Архив",
                                fontSize = 50.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF3A2D1B),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                "Технического обслуживания",
                                fontSize = 20.sp,
                                color = Color(0xFF3A2D1B),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        ) { paddingValues ->

            if (showCompactDialog) {
                CompactDateRangeFilterDialog(
                    initialStartDate = selectedStartDate,
                    initialEndDate = selectedEndDate,
                    onDismiss = { showCompactDialog = false },
                    onDateRangeSelected = { start, end ->
                        selectedStartDate = start
                        selectedEndDate = end
                        viewModel.filterMaintenanceRecordsByDateRange(start, end)
                    }
                )
            }


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFD9C1A0))
                    .padding(paddingValues)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(10.dp)
                        .background(Color(0xFFE7D2B1), shape = RoundedCornerShape(16.dp))
                        .border(2.dp, Color(0xFF3A2D1B), shape = RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    LazyColumn(modifier = Modifier.padding(10.dp)) {
                        itemsIndexed(maintenanceRecords.orEmpty()) { index, record ->
                            MaintenanceCard(record = record) {
                                currentIndex = index
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }

            currentIndex?.let { index ->
                LaunchedEffect(index) {
                    viewModel.fetchNodeTasksForRecord(maintenanceRecords?.get(index)?.id)
                }

                MaintenanceDetailDialog(
                    maintenanceRecords = maintenanceRecords ?: emptyList(),
                    currentIndex = index,
                    tasks = tasks ?: emptyList(),
                    onClose = { currentIndex = null },
                    onNavigate = { newIndex -> currentIndex = newIndex }
                )
            }
        }
    }
}