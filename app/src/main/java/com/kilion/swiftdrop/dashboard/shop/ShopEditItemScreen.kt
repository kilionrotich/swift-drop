package com.kilion.swiftdrop.dashboard.shop

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kilion.swiftdrop.data.model.Item
import com.kilion.swiftdrop.ui.components.SwiftDropTextField

@Composable
fun ShopEditItemScreen(navController: NavController, viewModel: ShopViewModel = hiltViewModel(), itemId: String?) {
    val uiState by viewModel.uiState.collectAsState()
    val itemToEdit = (uiState as? ShopUiState.SuccessItems)?.items?.firstOrNull { it.id == itemId }

    var name by remember { mutableStateOf(itemToEdit?.name ?: "") }
    var description by remember { mutableStateOf(itemToEdit?.description ?: "") }
    var price by remember { mutableStateOf(itemToEdit?.price?.toString() ?: "") }

    Column(modifier = Modifier.padding(16.dp)) {
        SwiftDropTextField(value = name, onValueChange = { name = it }, label = "Item Name")
        Spacer(modifier = Modifier.height(8.dp))
        SwiftDropTextField(value = description, onValueChange = { description = it }, label = "Item Description")
        Spacer(modifier = Modifier.height(8.dp))
        SwiftDropTextField(value = price, onValueChange = { price = it }, label = "Price")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val updatedItem = itemToEdit?.copy(
                name = name,
                description = description,
                price = price.toDoubleOrNull() ?: 0.0
            )
            if (updatedItem != null) {
                viewModel.editItem(updatedItem)
                navController.popBackStack()
            }
        }) {
            Text("Save Changes")
        }
    }
}
