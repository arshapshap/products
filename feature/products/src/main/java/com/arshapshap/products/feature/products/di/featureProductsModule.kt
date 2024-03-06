package com.arshapshap.products.feature.products.di

import com.arshapshap.products.feature.products.domain.usecase.GetProductsUseCase
import com.arshapshap.products.feature.products.presentation.screen.productslist.ProductsListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featureProductsModule = module {
    // domain
//    factory<GetProductsUseCase> { GetProductsUseCase(get()) }

    // presentation
    viewModel<ProductsListViewModel> { ProductsListViewModel() }
}