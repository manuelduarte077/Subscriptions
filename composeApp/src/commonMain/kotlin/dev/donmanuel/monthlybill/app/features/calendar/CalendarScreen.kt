package dev.donmanuel.monthlybill.app.features.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun CalendarScreen(
    navController: NavController,
) {

    Column(
        verticalArrangement = Arrangement.Center,
    ) {
        Text("Calendar")
        Button(
            onClick = {
                navController.navigate("billDetails")
            },
        ) {
            Text("Click me!")
        }
    }
}


