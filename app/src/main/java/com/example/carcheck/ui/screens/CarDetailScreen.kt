package com.example.carcheck.ui.screens

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.Coil
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.carcheck.data.model.CarNode
import com.example.carcheck.data.model.CarNode.NodeCategory
import com.example.carcheck.data.model.CarNode.NodeStatus
import com.example.carcheck.data.model.NodePositions
import com.example.carcheck.ui.composables.AnimatedWobbleButton
import com.example.carcheck.ui.composables.CarInfoDialog
import com.example.carcheck.ui.composables.ErrorDialog
import com.example.carcheck.ui.composables.NodeButton
import com.example.carcheck.ui.composables.NodeStatusSection
import com.example.carcheck.ui.composables.SubNodesDialog
import com.example.carcheck.ui.composables.TranslateNodeCategoryToRussian
import com.example.carcheck.ui.screens.drawer.DrawerBody
import com.example.carcheck.ui.screens.drawer.DrawerHeader
import com.example.carcheck.utils.NotificationUtils
import com.example.carcheck.viewmodel.CarViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedBoxWithConstraintsScope")
@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarDetailScreen(
    navController: NavController = rememberNavController(),
    viewModel: CarViewModel = hiltViewModel()
) {
    val car by viewModel.selectedCar.collectAsState()
    val nodes by viewModel.nodes.collectAsState()
    val positions by viewModel.positions.collectAsState()
    val selectedNode by viewModel.selectedNode.collectAsState()
    val parameters by viewModel.parameters.collectAsState()
    val changes by viewModel.changes.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var showInfoDialog by remember { mutableStateOf(false) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current


    // Single launched effect for car
    LaunchedEffect(car) {
        car?.let {
            viewModel.selectCar(it)
            viewModel.clearSelectedNode()
            listOfNotNull(it.imageLeft, it.imageRight).forEach { image ->
                Coil.imageLoader(context).enqueue(
                    ImageRequest.Builder(context).data(image).build()
                )
            }
        }
    }

//    LaunchedEffect(parameters) {
//        parameters?.forEach { param ->
//            if (param.status == NodeStatus.BROKEN) {
//                NotificationUtils.showCriticalParameterNotification(context, param)
//            }
//        }
//    }

//    LaunchedEffect(Unit) {
//        NotificationUtils.createNotificationChannel(context)
//    }

    val positionedNodes = remember(nodes, positions) {
        positions?.sortedBy { it.y }?.mapNotNull { position ->
            nodes?.find { it.category == position.category }?.let { node -> node to position }
        }.orEmpty()
    }

    errorMessage?.let {
        ErrorDialog(message = it, onDismiss = { viewModel.clearError() })
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(Modifier.fillMaxWidth(0.7f)) {
                DrawerHeader(car?.model ?: "Не выбран")
                DrawerBody(navController)
            }
        }
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackBarHostState) },
            modifier = Modifier
                .background(Color(0xFFD9C1A0))
                .fillMaxSize()
                .padding(10.dp),
            topBar = {
                Column {
                    IconButton(
                        modifier = Modifier.size(50.dp),
                        onClick = { scope.launch { drawerState.open() } }
                    ) {
                        Icon(Icons.Default.Menu, contentDescription = "Меню")
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                    ) {
                        Text(
                            text = "Состояние",
                            fontSize = 50.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF3A2D1B),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    Spacer(Modifier.height(20.dp))
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
                        .fillMaxHeight(0.6f)
                        .background(Color(0xFFE7D2B1), shape = RoundedCornerShape(16.dp))
                        .border(2.dp, Color(0xFF3A2D1B), shape = RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    IconButton(
                        modifier = Modifier
                            .size(60.dp)
                            .align(Alignment.TopStart),
                        onClick = { showInfoDialog = true }
                    ) {
                        Icon(Icons.Default.Info, contentDescription = "Информация")
                    }
                    if (showInfoDialog && car != null) {
                        CarInfoDialog(car = car!!, onDismiss = { showInfoDialog = false })
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth().padding(start = 5.dp, end = 5.dp)
                    ) {
                        listOf(car?.imageLeft, car?.imageRight).forEachIndexed { index, imageUrl ->
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = if (index == 0) "Вид слева" else "Вид справа",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1.8f)
                            )
                        }
                    }

                    BoxWithConstraints(modifier = Modifier.fillMaxWidth()
                        .aspectRatio(1f) // можно подогнать под реальные пропорции
                        .padding(vertical = 4.dp)) {
                        val scaleX = constraints.maxWidth.toFloat() / 1280f
                        val scaleY = constraints.maxHeight.toFloat() / (519f * 2)
                        val buttonSize = (constraints.maxWidth * 0.035f).coerceAtLeast(24f).dp

                        val rememberedButtons = remember(positionedNodes, scaleX, scaleY) {
                            positionedNodes.map { (node, position) ->
                                val x = position.x * scaleX
                                val y = position.y * scaleY
                                Triple(node, position, IntOffset(x.toInt(), y.toInt()))
                            }
                        }

                        rememberedButtons.forEachIndexed { index, (node, position, offset) ->
                                NodeButton(
                                    index = index,
                                    node = node,
                                    offset = offset,
                                    size = buttonSize,
                                    isSelected = selectedNode?.id == node.id,
                                    onClick = {
                                        viewModel.selectNode(node)
                                        viewModel.loadCategoryDataForNode(node)
                                    }
                                )
                        }
                    }

                }

                if (showDialog) {
                    SubNodesDialog(
                        nodeTitle = TranslateNodeCategoryToRussian(selectedNode?.category?.name ?: ""),
                        nodeParameters = parameters.orEmpty(),
                        nodeChanges = changes.orEmpty(),
                        onDismiss = { showDialog = false }
                    )
                }

                AnimatedVisibility(
                    visible = selectedNode != null,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    selectedNode?.let {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            NodeStatusSection(node = it) { showDialog = true }
                        }

                    }
                }

            }
        }
    }
}


