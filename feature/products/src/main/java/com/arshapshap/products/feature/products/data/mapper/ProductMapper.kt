package com.arshapshap.products.feature.products.data.mapper

import com.arshapshap.products.feature.products.data.network.response.CategoryRemote
import com.arshapshap.products.feature.products.data.network.response.ProductRemote
import com.arshapshap.products.feature.products.data.network.response.ProductsListRemote
import com.arshapshap.products.feature.products.domain.model.Category
import com.arshapshap.products.feature.products.domain.model.Product
import com.arshapshap.products.feature.products.domain.model.ProductsList

internal class ProductMapper {

    fun mapFromRemote(remote: ProductsListRemote) =
        ProductsList(
            list = remote.products.map { mapFromRemote(it) },
            canLoadMore = canLoadMore(remote.total, remote.skip, remote.limit)
        )

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
        category = Category(name = remote.category),
        thumbnailUrl = remote.thumbnail,
        imagesUrl = remote.images
    )

    fun mapFromRemote(remote: CategoryRemote) = Category(name = remote.name)

    private fun getPriceWithoutDiscount(price: Int, discountPercentage: Double): Int {
        if (discountPercentage == 0.0) return price
        return (100 * price / (100 - discountPercentage)).toInt()
    }

    private fun canLoadMore(total: Int, skip: Int, limit: Int) = skip + limit < total
}