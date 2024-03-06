package com.arshapshap.products.feature.products.data.mapper

import com.arshapshap.products.feature.products.data.network.response.ProductRemote
import com.arshapshap.products.feature.products.data.network.response.ProductsListRemote
import com.arshapshap.products.feature.products.domain.model.Product

internal class ProductMapper {

    fun mapFromRemote(remote: ProductsListRemote) =
        remote.products.map { mapFromRemote(it) }

    fun mapFromRemote(remote: ProductRemote) = Product(
        id = remote.id,
        title = remote.title,
        description = remote.description,
        price = remote.price,
        discountPercentage = remote.discountPercentage,
        priceWithoutDiscount = getPriceWithoutDiscount(remote.price, remote.discountPercentage),
        rating = remote.rating,
        stock = remote.stock,
        brand = remote.brand,
        category = remote.category,
        thumbnailUrl = remote.thumbnail,
        imagesUrl = remote.images
    )

    private fun getPriceWithoutDiscount(price: Int, discountPercentage: Double): Int {
        if (discountPercentage == 0.0) return price
        return (100 * price / (100 - discountPercentage)).toInt()
    }
}