package com.arshapshap.products.feature.products.presentation.screen.productslist.productsrecyclerview.viewholder

import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.arshapshap.products.feature.products.databinding.ItemFilterInfoBinding
import com.arshapshap.products.feature.products.presentation.screen.productslist.productsrecyclerview.RecyclerViewItem

internal class CategoryFilterViewHolder(
    private val binding: ItemFilterInfoBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(category: RecyclerViewItem.CategoryFilterItem) {
        binding.root.isGone = !category.visible
        binding.filterCategoryTagView.text = category.category?.name ?: ""
    }
}