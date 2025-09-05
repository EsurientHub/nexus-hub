package com.esurienthub.shelfcraft.features

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.esurienthub.shelfcraft.data.ProductStore
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun AddInventoryScreen(
    navigateToInventory: () -> Unit,
    onCreated: (id: String) -> Unit
) {
    val nameState = remember { mutableStateOf("") }
    val quantityState = remember { mutableStateOf("") }
    val expiryState = remember { mutableStateOf("") }
    val imageUrlState = remember { mutableStateOf("") }
    val showDatePicker = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Add Product") },
                navigationIcon = {
                    Icon(
                        Icons.Filled.Inventory,
                        contentDescription = "Inventory",
                        modifier = Modifier.clickable { navigateToInventory() }
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = nameState.value,
                onValueChange = { nameState.value = it },
                label = { Text("Name") }
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = quantityState.value,
                onValueChange = { quantityState.value = it.filter { ch -> ch.isDigit() } },
                label = { Text("Quantity") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(Modifier.height(12.dp))

            Text("Expiry Date")
            Spacer(Modifier.height(6.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp))
                    .clickable { showDatePicker.value = true }
                    .padding(12.dp),
                text = expiryState.value.ifBlank { "Tap to select date" }
            )

            if (showDatePicker.value) {
                val datePickerState = rememberDatePickerState()
                DatePickerDialog(
                    onDismissRequest = { showDatePicker.value = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                val selected = datePickerState.selectedDateMillis
                                if (selected != null) {
                                    val iso = Instant.fromEpochMilliseconds(selected)
                                        .toLocalDateTime(TimeZone.currentSystemDefault())
                                        .date
                                        .toString()
                                    expiryState.value = iso
                                }
                                showDatePicker.value = false
                            }
                        ) { Text("OK") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker.value = false }) { Text("Cancel") }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = imageUrlState.value,
                onValueChange = { imageUrlState.value = it },
                label = { Text("Image URL (optional)") }
            )

            Spacer(Modifier.height(24.dp))

            Button(
                enabled = nameState.value.isNotBlank() && quantityState.value.isNotBlank(),
                onClick = {
                    val product = ProductStore.addProduct(
                        name = nameState.value.trim(),
                        quantity = quantityState.value.toInt(),
                        expiryDateIso = expiryState.value.trim().ifBlank { null },
                        imageUrl = imageUrlState.value.trim().ifBlank { null }
                    )
                    onCreated(product.id)
                }
            ) {
                Text("Save")
            }
        }
    }
}
