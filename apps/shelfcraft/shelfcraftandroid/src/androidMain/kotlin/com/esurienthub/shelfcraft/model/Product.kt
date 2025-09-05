package com.esurienthub.shelfcraft.model

import kotlinx.datetime.LocalDate

data class Product(
    val id: String,
    val name: String,
    val quantity: Long,
    val createdOn: LocalDate,
    val expiryDate: LocalDate?,
    val imageUrl: String?
)
