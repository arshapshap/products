package com.arshapshap.products.feature.products.presentation.screen.productdetails.contract

internal sealed interface ProductDetailsEvent {

    data object NoConnectionError : ProductDetailsEvent

    data object UnknownError : ProductDetailsEvent

    data object ProductNotFoundError : ProductDetailsEvent

    data object NoSuchFunctionality : ProductDetailsEvent
}