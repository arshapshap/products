package com.arshapshap.products.feature.products.data.network

import com.arshapshap.products.feature.products.data.network.response.CategoryRemote
import com.arshapshap.products.feature.products.data.network.response.ProductRemote
import com.arshapshap.products.feature.products.data.network.response.ProductsListRemote
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface ProductsApi {

    @GET("products")
    suspend fun getProducts(@Query("skip") skip: Int, @Query("limit") limit: Int): ProductsListRemote

    @GET("products/{id}")
    suspend fun getProductById(@Path("path_variable") id: Int): ProductRemote?

    @GET("products/search")
    suspend fun getProductsBySearchQuery(@Query("q") query: String): ProductsListRemote

    @GET("products/categories")
    suspend fun getCategories(): List<CategoryRemote>

    @GET("products/category/{category}")
    suspend fun getProductsByCategory(@Path("category") category: String): ProductsListRemote
}