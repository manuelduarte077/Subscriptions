package dev.donmanuel.monthlybill.app.features.home.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import monthlybill.composeapp.generated.resources.Res
import monthlybill.composeapp.generated.resources.arrow_back
import monthlybill.composeapp.generated.resources.ic_clear_all
import monthlybill.composeapp.generated.resources.ic_search
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class SearchBarValue {
    Collapsed,
    Expanded
}

@Stable
class SearchBarState(initialActive: Boolean = false) {
    var active by mutableStateOf(initialActive)
        private set

    val currentValue: SearchBarValue
        get() = if (active) SearchBarValue.Expanded else SearchBarValue.Collapsed

    fun animateToCollapsed() {
        active = false
    }

    fun animateToExpanded() {
        active = true
    }
}

@Composable
fun rememberSearchBarState(initialActive: Boolean = false): SearchBarState {
    return remember { SearchBarState(initialActive) }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SimpleSearchBar(
    modifier: Modifier = Modifier
) {
    val searchBarState = rememberSearchBarState()
    var query by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    SearchBar(
        modifier = modifier,
        query = query,
        onQueryChange = { query = it },
        onSearch = { _ ->
            scope.launch { searchBarState.animateToCollapsed() }
        },
        active = searchBarState.currentValue == SearchBarValue.Expanded,
        onActiveChange = { isActive ->
            if (isActive) {
                searchBarState.animateToExpanded()
            } else {
                searchBarState.animateToCollapsed()
            }
        },
        placeholder = { Text("Buscar...") },
        leadingIcon = {
            if (searchBarState.currentValue == SearchBarValue.Expanded) {
                IconButton(onClick = { scope.launch { searchBarState.animateToCollapsed() } }) {
                    Icon(
                        painter = painterResource(Res.drawable.arrow_back),
                        modifier = Modifier.size(24.dp),
                        contentDescription = "Atrás"
                    )
                }
            } else {
                Icon(
                    painter = painterResource(Res.drawable.ic_search),
                    modifier = Modifier.size(24.dp),
                    contentDescription = "Ícono de búsqueda"
                )
            }
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { query = "" }) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_clear_all),
                        modifier = Modifier.size(24.dp),
                        contentDescription = "Limpiar búsqueda"
                    )
                }
            }
        },
        content = {}
    )
}