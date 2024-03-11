package com.arshapshap.products.feature.products.domain.model

data class ProductsList(
    val list: List<Product>,
    val canLoadMore: Boolean
)