package com.arshapshap.products.feature.products.domain.usecase

import com.arshapshap.products.feature.products.domain.model.Category
import com.arshapshap.products.feature.products.domain.repository.ProductRepository

class GetCategoriesUseCase(
    private val repository: ProductRepository
) {

    suspend operator fun invoke(): List<Category> {
        return repository.getCategories()
    }
}