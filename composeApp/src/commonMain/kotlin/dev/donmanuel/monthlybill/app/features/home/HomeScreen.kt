package dev.donmanuel.monthlybill.app.features.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

/**
 * Composable function for the Home Screen.
 *
 * Displays a grid of subscription cards.
 *
 * @param navController The NavController for handling navigation within the app.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(),
                title = {
                    Text(
                        text = "Monthly Bill",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        SubscriptionGrid(innerPadding, navController)
    }
}

/**
 * Composable function to display the subscription grid.
 *
 * @param innerPadding The padding values to apply to the grid content.
 * @param navController The NavController for handling navigation within the app.
 */
@Composable
fun SubscriptionGrid(
    innerPadding: androidx.compose.foundation.layout.PaddingValues,
    navController: NavController
) {
    val list = (1..20).map { it.toString() }
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        modifier = Modifier.padding(horizontal = 8.dp),
        contentPadding = innerPadding,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        content = {
            items(list.size) { index ->
                SubscriptionCard(
                    billNumber = list[index],
                    amount = list[index],
                    onCardClick = {
                        navController.navigate("billDetails")
                    },
                )
            }
        },
    )
}

/**
 * Composable function to display a single subscription card.
 *
 * @param billNumber The bill number.
 * @param amount The amount of the subscription.
 * @param onCardClick Callback to invoke when the card is clicked.
 */
@Composable
fun SubscriptionCard(
    billNumber: String,
    amount: String,
    onCardClick: () -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onCardClick),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Bill #$billNumber",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "$$amount",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            Text(
                text = "Details",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.End),
            )
        }
    }
}