package com.esurienthub.shelfcraft.features

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.esurienthub.shelfcraft.model.Product
import com.esurienthub.shelfcraft.data.ProductStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    navigateToAddInventory: () -> Unit,
    navigateToDetailInventory: (id: String) -> Unit
) {
    val searchQueryState = rememberSaveable { mutableStateOf("") }
    val products = ProductStore.products
    val filteredProducts by remember(searchQueryState.value, products) {
        derivedStateOf {
            val q = searchQueryState.value.trim()
            if (q.isEmpty()) products
            else products.filter { it.name.contains(q, ignoreCase = true) }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Inventory") },
                actions = {
                    IconButton(onClick = navigateToAddInventory) {
                        Icon(Icons.Filled.Add, contentDescription = "Add")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = searchQueryState.value,
                    onValueChange = { searchQueryState.value = it },
                    placeholder = { Text("Search products") },
                    leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer
                    )
                )
            }

            items(filteredProducts, key = { it.id }) { product ->
                ProductRow(
                    product = product,
                    modifier = Modifier.clickable { navigateToDetailInventory(product.id) }
                )
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }
        }
    }
}

@Composable
private fun ProductRow(product: Product, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(12.dp)),
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(product.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            imageLoader = ImageLoader.Builder(LocalPlatformContext.current)
                .crossfade(true)
                .build(),
        )

        Spacer(modifier = Modifier.size(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = "Qty: ${product.quantity} | Exp: ${product.expiryDate}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}