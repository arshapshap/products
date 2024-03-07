package com.arshapshap.products.feature.products.presentation.screen.productslist.productsrecyclerview

import com.arshapshap.products.feature.products.domain.model.Category
import com.arshapshap.products.feature.products.domain.model.Product

internal sealed interface RecyclerViewItem {

    data class ProductItem(val product: Product) : RecyclerViewItem

    data class LoadMoreButtonItem(val isLoading: Boolean) : RecyclerViewItem

    data class CategoryFilterItem(val category: Category?) : RecyclerViewItem
}