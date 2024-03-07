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
        mapper.mapFromRemote(remoteSource.getProducts(skip, limit))

    override suspend fun getProductById(id: Int): Product? =
        remoteSource.getProductById(id)?.let { mapper.mapFromRemote(it) }

    override suspend fun getProductsBySearchQuery(query: String, skip: Int, limit: Int): ProductsList =
        mapper.mapFromRemote(remoteSource.getProductsBySearchQuery(query))

    override suspend fun getCategories(): List<Category> =
        remoteSource.getCategories().map { mapper.mapFromRemote(it) }

    override suspend fun getProductsByCategory(category: Category, skip: Int, limit: Int): ProductsList =
        mapper.mapFromRemote(remoteSource.getProductsByCategory(category.name.lowercase()))
}