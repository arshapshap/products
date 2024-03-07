package com.arshapshap.products.feature.products.presentation.screen.productslist.productsrecyclerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.arshapshap.products.feature.products.databinding.ItemLoadMoreButtonBinding
import com.arshapshap.products.feature.products.databinding.ItemProductBinding
import com.arshapshap.products.feature.products.domain.model.Product
import java.lang.IllegalArgumentException

private const val VIEW_TYPE_PRODUCT = 0
private const val VIEW_TYPE_LOAD_MORE_BUTTON = 1

internal class ProductsAdapter(
    private var list: MutableList<Product> = mutableListOf(),
    private var isLoadingMoreItems: Boolean = false,
    private var showLoadMoreButton: Boolean = false,
    private val onOpenDetails: (Int) -> Unit,
    private val onLoadMore: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun setList(newList: List<Product>) {
        if (newList.isEmpty()) {
            clearList()
            return
        }

        val diffCallback = DiffCallback(list, newList)
        val diffCourses = DiffUtil.calculateDiff(diffCallback)
        list.clear()
        list.addAll(newList)
        diffCourses.dispatchUpdatesTo(this)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clearList() {
        list.clear()
        notifyDataSetChanged()
    }

    fun setLoading(isLoading: Boolean) {
        isLoadingMoreItems = isLoading
        if (showLoadMoreButton)
            notifyItemChanged(itemCount - 1)
    }

    fun showLoadMoreButton(show: Boolean) {
        if (showLoadMoreButton && !show)
            notifyItemRemoved(itemCount - 1)
        if (!showLoadMoreButton && show)
            notifyItemInserted(itemCount - 1)
        showLoadMoreButton = show
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        VIEW_TYPE_PRODUCT -> ProductsViewHolder(getItemProductBinding(parent), onOpenDetails)
        VIEW_TYPE_LOAD_MORE_BUTTON -> LoadMoreButtonViewHolder(getLoadMoreButtonBinding(parent), onLoadMore)
        else -> throw IllegalArgumentException("Unsupported view type")
    }

    override fun getItemCount(): Int = list.size + if (showLoadMoreButton) 1 else 0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (holder) {
        is ProductsViewHolder -> holder.onBind(list[position])
        is LoadMoreButtonViewHolder -> holder.onBind(isLoadingMoreItems)
        else -> throw IllegalArgumentException("Unsupported ViewHolder class")
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1 && showLoadMoreButton) {
            VIEW_TYPE_LOAD_MORE_BUTTON
        } else {
            VIEW_TYPE_PRODUCT
        }
    }

    private fun getItemProductBinding(parent: ViewGroup): ItemProductBinding =
        ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    private fun getLoadMoreButtonBinding(parent: ViewGroup): ItemLoadMoreButtonBinding =
        ItemLoadMoreButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    private class DiffCallback(
        private val oldList: List<Product>,
        private val newList: List<Product>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
