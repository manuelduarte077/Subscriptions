package dev.donmanuel.monthlybill.app.features.home

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {

    Text(
        text = "Home Screen",
    )
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}