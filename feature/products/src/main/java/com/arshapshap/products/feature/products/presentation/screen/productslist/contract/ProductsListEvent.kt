package com.arshapshap.products.feature.products.presentation.screen.productslist.contract

internal sealed interface ProductsListEvent {

    data class NoConnectionError(val showDialog: Boolean) : ProductsListEvent

    data class UnknownError(val showDialog: Boolean) : ProductsListEvent

    data object EmptyListError : ProductsListEvent
}