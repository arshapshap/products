package com.arshapshap.products.feature.products.di

import com.arshapshap.products.feature.products.data.mapper.ProductMapper
import com.arshapshap.products.feature.products.data.network.ProductsApi
import com.arshapshap.products.feature.products.data.repository.ProductRepositoryImpl
import com.arshapshap.products.feature.products.domain.repository.ProductRepository
import com.arshapshap.products.feature.products.domain.usecase.GetProductsUseCase
import com.arshapshap.products.feature.products.presentation.screen.productslist.ProductsListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val featureProductsModule = module {
    // data
    factory<ProductsApi> { get<Retrofit>().create(ProductsApi::class.java) }
    factory<ProductMapper> { ProductMapper() }
    factory<ProductRepository> { ProductRepositoryImpl(get(), get()) }

    // domain
    factory<GetProductsUseCase> { GetProductsUseCase(get()) }

    // presentation
    viewModel<ProductsListViewModel> { ProductsListViewModel(get()) }
}