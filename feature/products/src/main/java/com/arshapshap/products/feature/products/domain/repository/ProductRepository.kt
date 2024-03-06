package com.arshapshap.products.feature.products.domain.repository

import com.arshapshap.products.feature.products.domain.model.Product

interface ProductRepository {

    suspend fun getProducts(skip: Int, limit: Int): List<Product>

    suspend fun getProductById(id: Int): Product?

    suspend fun getProductsBySearchQuery(query: String): List<Product>

    suspend fun getCategories(): List<String>

    suspend fun getProductsByCategory(category: String): List<Product>
}