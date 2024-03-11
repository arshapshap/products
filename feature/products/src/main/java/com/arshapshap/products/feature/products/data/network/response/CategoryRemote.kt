package com.arshapshap.products.feature.products.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
internal value class CategoryRemote(
    @SerialName("name")
    val name: String
)