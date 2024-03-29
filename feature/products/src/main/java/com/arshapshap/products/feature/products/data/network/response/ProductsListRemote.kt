package com.arshapshap.products.feature.products.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ProductsListRemote(
    @SerialName("products")
    val products: List<ProductRemote>,
    @SerialName("total")
    val total: Int,
    @SerialName("skip")
    val skip: Int,
    @SerialName("limit")
    val limit: Int,
)
