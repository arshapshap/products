package com.arshapshap.products.feature.products.presentation.screen.productslist.productsrecyclerview.viewholder

import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.arshapshap.products.feature.products.databinding.ItemLoadMoreButtonBinding
import com.arshapshap.products.feature.products.presentation.screen.productslist.productsrecyclerview.RecyclerViewItem

internal class LoadMoreButtonViewHolder(
    private val binding: ItemLoadMoreButtonBinding,
    private val onLoadMore: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(loadMoreButtonItemInfo: RecyclerViewItem.LoadMoreButtonItem) {
        with (binding) {
            loadMoreButton.isGone = loadMoreButtonItemInfo.isLoading
            loadingProgressBar.isGone = !loadMoreButtonItemInfo.isLoading

            loadMoreButton.setOnClickListener {
                onLoadMore.invoke()
            }
        }
    }
}