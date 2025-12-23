package com.kilion.swiftdrop.dashboard.shop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kilion.swiftdrop.data.model.Item
import com.kilion.swiftdrop.ui.theme.MintLace
import kotlinx.coroutines.flow.collectLatest

/**
 * A screen composable that provides a form for shop owners to add new items.
 *
 * @param viewModel The shared ShopViewModel instance provided by Hilt.
 */
@Composable
fun ShopAddItemsScreen(navController: NavController, viewModel: ShopViewModel) {
    // Collect the UI state from the ViewModel. The UI will recompose when this changes.
    val uiState by viewModel.uiState.collectAsState()

    // Local state for the text fields. `remember` ensures the state survives recompositions.
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    // --- State Observation ---
    // LaunchedEffect is used to react to state changes from the ViewModel,
    // like showing a snackbar on success or error.
    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is ShopUiState.Loading -> {
                isLoading = true
            }
            is ShopUiState.SuccessItems -> {
                isLoading = false
                snackbarHostState.showSnackbar("Item added successfully!")
                // Clear the fields after successful submission
                name = ""
                price = ""
                description = ""
            }
            is ShopUiState.Error -> {
                isLoading = false
                snackbarHostState.showSnackbar("Error: ${state.message}")
            }
            // For other states, we are not loading.
            else -> {
                isLoading = false
            }
        }
    }

    // --- UI Layout ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MintLace)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Add a New Item", style = MaterialTheme.typography.headlineSmall)

        // Text field for the item name
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Item Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Text field for the price, restricted to number input
        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Price") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )

        // Text field for the description
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp) // Make description field larger
        )

        Spacer(modifier = Modifier.height(8.dp))

        // --- Action Button ---
        Button(
            onClick = {
                // Basic validation
                if (name.isNotBlank() && price.isNotBlank()) {
                    val item = Item(
                        id = "", // The backend should generate the ID
                        name = name,
                        price = price.toDoubleOrNull() ?: 0.0,
                        description = description
                    )
                    viewModel.addItem(item)
                } else {
                    // This logic can be expanded to show an error message
                }
            },
            modifier = Modifier.fillMaxWidth(),
            // Disable button while loading to prevent multiple clicks
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.height(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Add Item")
            }
        }
    }

    // This component is required to show Snackbars.
    // It's usually placed in the root Scaffold of your screen.
    SnackbarHost(hostState = snackbarHostState, modifier = Modifier.fillMaxWidth())
}
