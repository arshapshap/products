package com.arshapshap.products.feature.products.presentation.screen.productslist.model

internal sealed interface ProductsListEvent {

    data class ShowNoConnectionError(val showDialog: Boolean) : ProductsListEvent

    data class ShowUnknownError(val showDialog: Boolean) : ProductsListEvent

    data object ShowEmptyListError : ProductsListEvent
}