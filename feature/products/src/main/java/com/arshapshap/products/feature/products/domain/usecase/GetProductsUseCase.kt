package com.arshapshap.products.feature.products.domain.usecase

import com.arshapshap.products.feature.products.domain.model.ProductsList
import com.arshapshap.products.feature.products.domain.repository.ProductRepository

private const val pageSize = 20

class GetProductsUseCase(
    private val repository: ProductRepository
) {

    suspend operator fun invoke(page: Int = 0): ProductsList {
        return repository.getProducts(page * pageSize, pageSize)
    }
}