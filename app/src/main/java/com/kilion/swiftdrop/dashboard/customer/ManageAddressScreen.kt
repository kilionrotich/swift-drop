package com.kilion.swiftdrop.dashboard.customer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kilion.swiftdrop.data.model.Address

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageAddressScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val addresses by viewModel.addresses.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var editingAddress by remember { mutableStateOf<Address?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { 
                editingAddress = null
                showDialog = true 
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Address")
            }
        }
    ) {
        LazyColumn(modifier = Modifier.padding(it).fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
            items(addresses) { address ->
                AddressItem(
                    address = address,
                    onEdit = { 
                        editingAddress = address
                        showDialog = true 
                    },
                    onDelete = { viewModel.deleteAddress(address) },
                    onSetDefault = { viewModel.setDefaultAddress(address) }
                )
            }
        }
    }

    if (showDialog) {
        AddressDialog(
            address = editingAddress,
            onDismiss = { showDialog = false },
            onSave = {
                if (editingAddress == null) {
                    viewModel.addAddress(it.street, it.city, it.state, it.zipCode)
                } else {
                    viewModel.updateAddress(it)
                }
                showDialog = false
            }
        )
    }
}

@Composable
fun AddressItem(
    address: Address, 
    onEdit: () -> Unit, 
    onDelete: () -> Unit, 
    onSetDefault: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(address.street, style = MaterialTheme.typography.titleMedium)
            Text("${address.city}, ${address.state} ${address.zipCode}", style = MaterialTheme.typography.bodyMedium)
            if (address.isDefault) {
                Text("Default", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Address")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Address")
                }
                Button(onClick = onSetDefault) {
                    Text("Set as Default")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressDialog(
    address: Address?,
    onDismiss: () -> Unit,
    onSave: (Address) -> Unit
) {
    var street by remember { mutableStateOf(address?.street ?: "") }
    var city by remember { mutableStateOf(address?.city ?: "") }
    var state by remember { mutableStateOf(address?.state ?: "") }
    var zipCode by remember { mutableStateOf(address?.zipCode ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (address == null) "Add Address" else "Edit Address") },
        text = {
            Column {
                TextField(value = street, onValueChange = { street = it }, label = { Text("Street") })
                TextField(value = city, onValueChange = { city = it }, label = { Text("City") })
                TextField(value = state, onValueChange = { state = it }, label = { Text("State") })
                TextField(value = zipCode, onValueChange = { zipCode = it }, label = { Text("Zip Code") })
            }
        },
        confirmButton = {
            Button(onClick = {
                val newAddress = address?.copy(street = street, city = city, state = state, zipCode = zipCode) 
                    ?: Address("", street, city, state, zipCode)
                onSave(newAddress)
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
