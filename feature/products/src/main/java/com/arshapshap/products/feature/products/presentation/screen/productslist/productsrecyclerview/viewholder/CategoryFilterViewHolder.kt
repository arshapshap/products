package com.arshapshap.products.feature.products.presentation.screen.productslist.productsrecyclerview.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.arshapshap.products.feature.products.databinding.ItemFilterInfoBinding
import com.arshapshap.products.feature.products.presentation.screen.productslist.productsrecyclerview.RecyclerViewItem

internal class CategoryFilterViewHolder(
    private val binding: ItemFilterInfoBinding,
    private val onCategoryClick: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(category: RecyclerViewItem.CategoryFilterItem) {
        binding.filterCategoryTagView.text = category.category?.name ?: ""

        binding.filterCategoryTagView.setOnClickListener {
            onCategoryClick.invoke()
        }
    }
}