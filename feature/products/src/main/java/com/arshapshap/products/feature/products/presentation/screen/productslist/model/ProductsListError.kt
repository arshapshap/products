package com.arshapshap.products.feature.products.presentation.screen.productslist.model

internal sealed interface ProductsListError {

    data class NoConnectionError(val showDialog: Boolean) : ProductsListError

    data class UnknownError(val showDialog: Boolean) : ProductsListError

    data object EmptyListError : ProductsListError
}