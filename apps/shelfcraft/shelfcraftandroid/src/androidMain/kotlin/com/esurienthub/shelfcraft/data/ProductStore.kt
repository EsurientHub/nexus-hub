package com.esurienthub.shelfcraft.data

import androidx.compose.runtime.mutableStateListOf
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import com.esurienthub.shelfcraft.model.Product
import java.util.UUID
import kotlin.time.ExperimentalTime

object ProductStore {
	val products = mutableStateListOf<Product>()

	@OptIn(ExperimentalTime::class)
    fun addProduct(
		name: String,
		quantity: Int,
		expiryDateIso: String?,
		imageUrl: String?
	): Product {
		val createdOn = kotlin.time.Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
		val expiry = expiryDateIso?.takeIf { it.isNotBlank() }?.let { kotlinx.datetime.LocalDate.parse(it) }
		val product = Product(
			id = UUID.randomUUID().toString(),
			name = name,
			quantity = quantity.toLong(),
			createdOn = createdOn,
			expiryDate = expiry,
			imageUrl = imageUrl?.takeIf { it.isNotBlank() }
		)
		products.add(0, product)
		return product
	}

	fun getProductById(id: String): Product? = products.firstOrNull { it.id == id }
}
