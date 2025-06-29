package com.example.carcheck.ui.navigation


import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.carcheck.ui.screens.ArchiveMaintenanceScreen
import com.example.carcheck.ui.screens.CarDetailScreen
import com.example.carcheck.ui.screens.MaintenanceScreen
import com.example.carcheck.ui.screens.SelectCarScreen
import com.example.carcheck.ui.screens.SplashScreen
import com.example.carcheck.viewmodel.CarViewModel


sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object SelectCar : Screen("select_car")
    object CarDetail : Screen("car_detail")
    object Maintenance : Screen("maintenance")
    object ArchiveMaintenanceScreen : Screen("archive_maintenance")
}

@Composable
fun CarCheckNavHost(navController: NavHostController = rememberNavController()) {
    val carViewModel: CarViewModel = hiltViewModel()
    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen(onNavigate = { navController.navigate(Screen.SelectCar.route) })
        }
        composable(Screen.SelectCar.route) {
            SelectCarScreen(navController, carViewModel)
        }
        composable(Screen.ArchiveMaintenanceScreen.route) {
            ArchiveMaintenanceScreen(navController, carViewModel)
        }

        composable(Screen.CarDetail.route) {

            CarDetailScreen(
                navController = navController,
                viewModel = carViewModel
            )
        }

        composable(Screen.Maintenance.route) { backStackEntry ->
            MaintenanceScreen(
                navController,
                viewModel = carViewModel
            )
        }

    }
}