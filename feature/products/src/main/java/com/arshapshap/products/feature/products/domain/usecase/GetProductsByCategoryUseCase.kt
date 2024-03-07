package com.arshapshap.products.feature.products.domain.usecase

import com.arshapshap.products.feature.products.domain.model.Category
import com.arshapshap.products.feature.products.domain.model.ProductsList
import com.arshapshap.products.feature.products.domain.repository.ProductRepository

class GetProductsByCategoryUseCase(
    private val repository: ProductRepository
) {

    suspend operator fun invoke(category: Category, page: Int = 0): ProductsList {
        return repository.getProductsByCategory(category, page * pageSize, pageSize)
    }
}