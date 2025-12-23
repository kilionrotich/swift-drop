package com.kilion.swiftdrop.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier.fillMaxWidth(), // Modifier is now the second parameter
    onClick: () -> Unit // onClick is now the last parameter
) {
    Button(
        onClick = onClick,
        modifier = modifier, // Use the passed-in modifier
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        Text(text)
    }
}
