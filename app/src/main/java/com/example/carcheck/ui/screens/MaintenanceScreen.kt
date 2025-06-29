package com.example.carcheck.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.carcheck.R
import com.example.carcheck.data.model.CarNode
import com.example.carcheck.data.model.MaintenanceNodeTask
import com.example.carcheck.data.model.MaintenanceRecord
import com.example.carcheck.ui.composables.ConfirmSaveDialog
import com.example.carcheck.ui.composables.TranslateNodeCategoryToRussian
import com.example.carcheck.ui.navigation.Screen
import com.example.carcheck.ui.screens.drawer.DrawerBody
import com.example.carcheck.ui.screens.drawer.DrawerHeader
import com.example.carcheck.viewmodel.CarViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaintenanceScreen(
    navController: NavController,
    viewModel: CarViewModel = hiltViewModel()
) {
    val maintenanceRecord by viewModel.maintenanceRecords.collectAsState()
    val car by viewModel.selectedCar.collectAsState()
    val carNodes by viewModel.nodes.collectAsState()
    val categories = remember(carNodes) {
        carNodes?.map { it.category }
            ?.distinct()
            ?.mapNotNull { categoryName ->
                CarNode.NodeCategory.entries.find { it.name == categoryName.name }
            } ?: emptyList()
    }
    val checkboxStates = remember { mutableStateMapOf<CarNode.NodeCategory, Boolean>() }
    val commentStates = remember { mutableStateMapOf<CarNode.NodeCategory, String>() }
    val openConfirmDialog = remember { mutableStateOf(false) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val isoDate = maintenanceRecord?.lastOrNull()?.nextDueDate
    val nextDueDate = try {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        isoDate?.let { formatter.format(parser.parse(it)!!) }
    } catch (e: Exception) {
        SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date()) // fallback
    }


    LaunchedEffect(car) {
        car?.let {
            viewModel.fetchNodes(it.id)
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
                            modifier = Modifier
                                .size(35.dp)
                                .padding(top = 3.dp, end = 10.dp),
                            onClick = { navController.navigate(Screen.ArchiveMaintenanceScreen.route) }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.archiv_icon),
                                contentDescription = "Архив ТО",
                            )
                        }
                        Text(
                            "Архив ТО",
                            fontSize = 20.sp,
                            color = Color(0xFF3A2D1B),
                            fontWeight = Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Техническое обслуживание",
                            fontSize = 50.sp,
                            fontWeight = Bold,
                            color = Color(0xFF3A2D1B),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFD9C1A0))
                    .padding(paddingValues)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.20f)
                        .padding(10.dp)
                        .background(Color(0xFFE7D2B1), shape = RoundedCornerShape(16.dp))
                        .border(
                            2.dp,
                            Color(0xFF3A2D1B),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "Дата следующего ТО",
                            fontSize = 20.sp,
                            color = Color(0xFF59432A)
                        )
                        Spacer(Modifier.height(10.dp))
                        Text(
                            nextDueDate ?: SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date()),
                            fontSize = 20.sp,
                            fontWeight = Bold,
                            color = Color(0xFF59432A)
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(1f)
                        .padding(10.dp)
                        .background(Color(0xFFE7D2B1), shape = RoundedCornerShape(16.dp))
                        .border(
                            2.dp,
                            Color(0xFF3A2D1B),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Выполнено",
                            fontSize = 24.sp,
                            fontWeight = Bold,
                            color = Color(0xFF3A2D1B),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        ) {
                            items(categories) { category ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = checkboxStates[category] == true,
                                        onCheckedChange = { checked ->
                                            checkboxStates[category] = checked
                                        },
                                        colors = CheckboxDefaults.colors(
                                            checkmarkColor = Color(0xFFE7D2B1),
                                            checkedColor = Color(0xFF4F3B24)
                                        ),
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                    Text(
                                        text = TranslateNodeCategoryToRussian(category.name),
                                        fontSize = 16.sp,
                                        color = Color(0xFF3A2D1B),
                                        modifier = Modifier.weight(0.8f)
                                    )
                                    TextField(
                                        value = commentStates[category] ?: "",
                                        onValueChange = { commentStates[category] = it },
                                        placeholder = { Text("Комментарий") },
                                        modifier = Modifier
                                            .weight(1f)
                                            .border(
                                                1.dp,
                                                Color(0xFF3A2D1B),
                                                shape = RoundedCornerShape(16.dp)
                                            ),
                                        singleLine = true,
                                        colors = TextFieldDefaults.textFieldColors(
                                            containerColor = Color.Transparent,
                                            focusedIndicatorColor = Color.Transparent,
                                            unfocusedIndicatorColor = Color.Transparent,
                                            disabledIndicatorColor = Color.Transparent
                                        ),
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        val carId = car?.id ?: 1
                        val currentDate =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                        val description = ""
                        val calendar = Calendar.getInstance()
                        calendar.add(Calendar.MONTH, 6)
                        val nextDateIso = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)

                        val newRecord = MaintenanceRecord(
                            carId = carId,
                            date = currentDate,
                            description = description,
                            nextDueDate = nextDateIso,
                        )

                        val context = LocalContext.current

                        Button(
                            onClick = {
                                val selectedCategories = checkboxStates.filterValues { it }.keys

                                if (selectedCategories.isEmpty()) {
                                    Toast.makeText(context, "Выберите хотя бы одну категорию", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }

                                for (category in selectedCategories) {
                                    val comment = commentStates[category]?.trim() ?: ""
                                    if (comment.isEmpty()) {
                                        Toast.makeText(
                                            context,
                                            "Для категории '${TranslateNodeCategoryToRussian(category.name)}' введите комментарий",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        return@Button
                                    }
                                }
                                openConfirmDialog.value = true
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4D3B25),
                                contentColor = Color(0xFFD9C1A0)
                            ),
                            shape = RoundedCornerShape(100.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text("Сохранить")
                        }
                        if (openConfirmDialog.value) {
                            ConfirmSaveDialog(
                                onDismiss = { openConfirmDialog.value = false },
                                onConfirm = {
                                    openConfirmDialog.value = false

                                    viewModel.addMaintenanceRecord(newRecord) { record ->
                                        checkboxStates.filterValues { it }.keys.forEach { category ->
                                            val comment = commentStates[category] ?: ""
                                            val nodeTask = MaintenanceNodeTask(
                                                maintenanceId = record.id,
                                                taskDescription = comment,
                                                category = category.name
                                            )
                                            viewModel.addNodeTask(nodeTask)
                                        }

                                        Toast.makeText(context, "Данные успешно сохранены", Toast.LENGTH_SHORT).show()
                                        checkboxStates.clear()
                                        commentStates.clear()
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


