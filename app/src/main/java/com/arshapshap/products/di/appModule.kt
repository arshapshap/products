package com.arshapshap.products.di

import com.arshapshap.products.feature.products.presentation.FeatureProductsRouter
import com.arshapshap.products.navigation.Navigator
import org.koin.dsl.module

val appModule = module {

    // navigation
    single<Navigator> { Navigator() }
    single<FeatureProductsRouter> { get<Navigator>() }
}