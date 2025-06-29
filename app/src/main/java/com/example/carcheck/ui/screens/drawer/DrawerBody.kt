package com.example.carcheck.ui.screens.drawer


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.carcheck.ui.navigation.Screen


@Preview(showBackground = true)
@Composable
fun DrawerBody(
    navController: NavController = rememberNavController()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(bottomEnd = 24.dp))
            .background(Color(0xFF8A5F42))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DrawerMenuItem("Состояние машины", Icons.Default.Home) {
                navController.navigate(Screen.CarDetail.route)
            }
            Divider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = Color(0xFFD9C1A0))
            DrawerMenuItem("Техническое обслуживание", Icons.Default.Build) {
                navController.navigate(Screen.Maintenance.route)
            }
            Divider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = Color(0xFFD9C1A0))
            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { navController.navigate(Screen.SelectCar.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD9C1A0),
                    contentColor = Color(0xFF4D3B25)
                ),
                shape = RoundedCornerShape(100.dp)
            ) {
                Text(text = "Выбрать другую машину", fontSize = 15.sp)
            }
        }
    }
}

@Composable
fun DrawerMenuItem(
    text: String = "fds",
    icon: ImageVector = Icons.Default.Build,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = BrownBackground // или любой другой цвет
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            fontSize = 16.sp,
            color = BrownBackground,
            fontWeight = FontWeight.Medium
        )
    }
}
