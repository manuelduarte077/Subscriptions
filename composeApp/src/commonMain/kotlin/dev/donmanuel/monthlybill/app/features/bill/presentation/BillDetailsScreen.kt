package dev.donmanuel.monthlybill.app.features.bill.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import monthlybill.composeapp.generated.resources.Res
import monthlybill.composeapp.generated.resources.arrow_back
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillDetailsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.Companion.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Bill Details",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Companion.Center,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painterResource(Res.drawable.arrow_back),
                            contentDescription = "Back",
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
                scrollBehavior = scrollBehavior,
            )
        }

    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            val list = (1..25).map { it.toString() }
            items(count = list.size) {
                Card(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    onClick = {
                    },
                ) {
                    Text(
                        text = "Item ${list[it]}",
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Companion.Center,
                    )
                }
            }
        }
    }
}