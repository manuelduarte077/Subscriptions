package dev.donmanuel.monthlybill.app.features.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier
) {
    Column {
        Text("Calendar")
        Button(
            onClick = { /*TODO*/ },
        ) {
            Text("Click me!")
        }
    }
}


