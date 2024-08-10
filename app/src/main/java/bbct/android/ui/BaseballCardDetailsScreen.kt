package bbct.android.ui

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import bbct.android.R

data class BaseballCardState(
    var autographed: Boolean = false,
    var condition: String = "",
    var brand: String = "",
    var year: String = "",
    var number: String = "",
    var value: String = "",
    var count: String = "",
    var playerName: String = "",
    var team: String = "",
    var position: String = ""
)

@Composable
fun BaseballCardDetailsScreen(navController: NavHostController) {
    Scaffold(
        topBar = { TopBar() },
        floatingActionButton = { SaveCardButton(navController) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        BaseballCardDetails(modifier = Modifier.padding(innerPadding))
    }
}


@Composable
fun BaseballCardDetails(modifier: Modifier = Modifier, context: Context = LocalContext.current) {
    val resources = context.resources
    val conditions = resources.getStringArray(R.array.condition)
    val positions = resources.getStringArray(R.array.positions)
    val state = remember { mutableStateOf(BaseballCardState()) }

    Column(modifier = modifier) {
        Row {
            Checkbox(
                checked = state.value.autographed,
                onCheckedChange = { state.value = state.value.copy(autographed = it) },
            )
            Text(text = stringResource(id = R.string.autograph_label))
        }
        Row {
            Select(
                labelText = stringResource(id = R.string.condition_label),
                options = conditions,
                selected = state.value.condition,
                onSelectedChange = { state.value = state.value.copy(condition = it) }
            )
        }
        Row {
            TextField(
                label = { Text(text = stringResource(id = R.string.brand_label)) },
                value = state.value.brand,
                onValueChange = { state.value = state.value.copy(brand = it) }
            )
        }
        Row {
            TextField(
                label = { Text(text = stringResource(id = R.string.year_label)) },
                value = state.value.year,
                onValueChange = { state.value = state.value.copy(year = it) }
            )
        }
        Row {
            TextField(
                label = { Text(text = stringResource(id = R.string.number_label)) },
                value = state.value.number,
                onValueChange = { state.value = state.value.copy(number = it) }
            )
        }
        Row {
            TextField(
                label = { Text(text = stringResource(id = R.string.value_label)) },
                value = state.value.value,
                onValueChange = { state.value = state.value.copy(value = it) }
            )
        }
        Row {
            TextField(
                label = { Text(text = stringResource(id = R.string.count_label)) },
                value = state.value.count,
                onValueChange = { state.value = state.value.copy(count = it) }
            )
        }
        Row {
            TextField(
                label = { Text(text = stringResource(id = R.string.player_name_label)) },
                value = state.value.playerName,
                onValueChange = { state.value = state.value.copy(playerName = it) }
            )
        }
        Row {
            TextField(
                label = { Text(text = stringResource(id = R.string.team_label)) },
                value = state.value.team,
                onValueChange = { state.value = state.value.copy(team = it) }
            )
        }
        Row {
            Select(
                labelText = stringResource(id = R.string.player_position_label),
                options = positions,
                selected = state.value.position,
                onSelectedChange = { state.value = state.value.copy(position = it) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Select(
    labelText: String,
    options: Array<String>,
    selected: String,
    onSelectedChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        TextField(
            label = { Text(text = labelText) },
            readOnly = true,
            value = selected,
            onValueChange = { /* Do nothing */ },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option) },
                    onClick = {
                        onSelectedChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun SaveCardButton(navController: NavController) {
    FloatingActionButton(onClick = { /* TODO */ }) {
        Icon(Icons.Default.Check, contentDescription = stringResource(id = R.string.save_menu))
    }

}