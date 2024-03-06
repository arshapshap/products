package com.arshapshap.products.feature.products.domain.model

data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val price: Int,
    val discountPercentage: Double,
    val priceWithoutDiscount: Int,
    val rating: Double,
    val stock: Int,
    val brand: String,
    val category: String,
    val thumbnailUrl: String,
    val imagesUrl: List<String>
)