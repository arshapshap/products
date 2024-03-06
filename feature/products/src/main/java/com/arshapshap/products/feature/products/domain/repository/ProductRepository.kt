package com.arshapshap.products.feature.products.domain.repository

import com.arshapshap.products.feature.products.domain.model.Product

interface ProductRepository {

    suspend fun getProducts(skip: Int, limit: Int): List<Product>
}