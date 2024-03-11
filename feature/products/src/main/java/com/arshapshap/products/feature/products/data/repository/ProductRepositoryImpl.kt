package com.arshapshap.products.feature.products.data.repository

import com.arshapshap.products.feature.products.data.mapper.ProductMapper
import com.arshapshap.products.feature.products.data.network.ProductsApi
import com.arshapshap.products.feature.products.domain.model.Category
import com.arshapshap.products.feature.products.domain.model.Product
import com.arshapshap.products.feature.products.domain.model.ProductsList
import com.arshapshap.products.feature.products.domain.repository.ProductRepository

internal class ProductRepositoryImpl(
    private val remoteSource: ProductsApi,
    private val mapper: ProductMapper
) : ProductRepository {

    override suspend fun getProducts(skip: Int, limit: Int): ProductsList =
        mapper.toDomain(remoteSource.getProducts(skip, limit))

    override suspend fun getProductById(id: Int): Product? =
        remoteSource.getProductById(id)?.let { mapper.toDomain(it) }

    override suspend fun getProductsBySearchQuery(query: String, skip: Int, limit: Int): ProductsList =
        mapper.toDomain(remoteSource.getProductsBySearchQuery(query, skip, limit))

    override suspend fun getCategories(): List<Category> =
        remoteSource.getCategories().map { mapper.toDomain(it) }

    override suspend fun getProductsByCategory(category: Category, skip: Int, limit: Int): ProductsList {
        val categoryRemote = mapper.toRemote(category)
        return mapper.toDomain(remoteSource.getProductsByCategory(categoryRemote.name, skip, limit))
    }
}