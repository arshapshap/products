package com.arshapshap.products.feature.products.domain.usecase

import com.arshapshap.products.feature.products.domain.model.Product
import com.arshapshap.products.feature.products.domain.repository.ProductRepository

private const val pageSize = 20

class GetProductsUseCase(
    private val repository: ProductRepository
) {

    suspend operator fun invoke(page: Int = 0): List<Product> {
        return repository.getProducts(page * pageSize, pageSize)
    }
}