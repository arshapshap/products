package com.arshapshap.products.feature.products.domain.repository

import com.arshapshap.products.feature.products.domain.model.Category
import com.arshapshap.products.feature.products.domain.model.Product
import com.arshapshap.products.feature.products.domain.model.ProductsList

interface ProductRepository {

    suspend fun getProducts(skip: Int, limit: Int): ProductsList

    suspend fun getProductById(id: Int): Product?

    suspend fun getProductsBySearchQuery(query: String, skip: Int, limit: Int): ProductsList

    suspend fun getCategories(): List<Category>

    suspend fun getProductsByCategory(category: Category, skip: Int, limit: Int): ProductsList
}