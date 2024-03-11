package com.arshapshap.products.feature.products.presentation.screen.productdetails.model

internal sealed interface ProductDetailsError {

    data object NoConnectionError : ProductDetailsError

    data object UnknownError : ProductDetailsError

    data object ProductNotFoundError : ProductDetailsError
}