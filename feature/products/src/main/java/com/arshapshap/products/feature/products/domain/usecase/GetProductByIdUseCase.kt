package com.arshapshap.products.feature.products.domain.usecase

import com.arshapshap.products.feature.products.domain.model.Product
import com.arshapshap.products.feature.products.domain.repository.ProductRepository

class GetProductByIdUseCase(
    private val repository: ProductRepository
) {

    suspend operator fun invoke(id: Int): Product? {
        return repository.getProductById(id)
    }
}