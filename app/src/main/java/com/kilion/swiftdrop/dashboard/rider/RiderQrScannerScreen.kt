package com.kilion.swiftdrop.dashboard.rider

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RiderQrScannerScreen(
    onQrScanned: (String) -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Scan Order QR") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("QR Scanner Placeholder")
            // TODO: Integrate ML Kit Barcode Scanner here
            // When QR is scanned:
            // val orderId = "decoded-order-id"
            // onQrScanned(orderId)
        }
    }
}