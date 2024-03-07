package com.arshapshap.products.feature.products.presentation.screen.productslist.productsrecyclerview

import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.arshapshap.products.feature.products.databinding.ItemLoadMoreButtonBinding

internal class LoadMoreButtonViewHolder(
    private val binding: ItemLoadMoreButtonBinding,
    private val onLoadMore: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(loading: Boolean) {
        with (binding) {
            loadMoreButton.isGone = loading
            loadingProgressBar.isGone = !loading

            loadMoreButton.setOnClickListener {
                onLoadMore.invoke()
            }
        }
    }
}