package com.esurienthub.shelfcraft.features

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.esurienthub.shelfcraft.data.ProductStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailInventoryScreen(
    id: String,
    navigateToInventory: () -> Unit,
) {
    val product = ProductStore.getProductById(id)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Product Detail") },
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
        if (product == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Text("Product not found", style = MaterialTheme.typography.titleMedium)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top
            ) {
                if (product.imageUrl != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalPlatformContext.current)
                            .data(product.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        imageLoader = ImageLoader.Builder(LocalPlatformContext.current)
                            .crossfade(true)
                            .build()
                    )

                    Spacer(Modifier.height(16.dp))
                }

                Text(
                    product.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Text("Quantity: ${product.quantity}", style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(8.dp))
                Text("Created: ${product.createdOn}", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(8.dp))
                Text(
                    "Expiry: ${product.expiryDate ?: "-"}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
