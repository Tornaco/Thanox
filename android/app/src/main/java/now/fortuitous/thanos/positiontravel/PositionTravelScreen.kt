package now.fortuitous.thanos.positiontravel

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import github.tornaco.android.thanos.module.compose.common.widget.ThanoxMediumAppBarScaffold

@Composable
fun PositionTravelScreen(
    viewModel: PositionTravelViewModel,
    onBack: () -> Unit,
) {
    LaunchedEffect(viewModel) {
        viewModel.loadState()
    }

    val state = viewModel.uiState
    var latInput by remember(state.latitude) { mutableStateOf(state.latitude) }
    var lonInput by remember(state.longitude) { mutableStateOf(state.longitude) }

    ThanoxMediumAppBarScaffold(
        title = { Text("Position Travel") },
        onBackPressed = onBack,
    ) { contentPadding->
        if (!state.isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(contentPadding)
                    .padding(16.dp),
            ) {
                // Enable/disable switch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = if (state.isEnabled) "Enabled" else "Disabled",
                        modifier = Modifier.weight(1f),
                    )
                    Switch(
                        checked = state.isEnabled,
                        onCheckedChange = { viewModel.setEnabled(it) },
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Latitude input
                OutlinedTextField(
                    value = latInput,
                    onValueChange = { latInput = it },
                    label = { Text("Latitude") },
                    placeholder = { Text("-90 to 90") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Longitude input
                OutlinedTextField(
                    value = lonInput,
                    onValueChange = { lonInput = it },
                    label = { Text("Longitude") },
                    placeholder = { Text("-180 to 180") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Save button
                Button(
                    onClick = { viewModel.saveCoordinates(latInput, lonInput) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Save Coordinates")
                }
            }
        }
    }
}
