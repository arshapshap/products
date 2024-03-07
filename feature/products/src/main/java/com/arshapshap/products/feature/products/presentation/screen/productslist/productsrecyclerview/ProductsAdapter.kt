package com.arshapshap.products.feature.products.presentation.screen.productslist.productsrecyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.arshapshap.products.feature.products.databinding.ItemFilterInfoBinding
import com.arshapshap.products.feature.products.databinding.ItemLoadMoreButtonBinding
import com.arshapshap.products.feature.products.databinding.ItemProductBinding
import com.arshapshap.products.feature.products.domain.model.Category
import com.arshapshap.products.feature.products.domain.model.Product
import com.arshapshap.products.feature.products.presentation.screen.productslist.productsrecyclerview.viewholder.CategoryFilterViewHolder
import com.arshapshap.products.feature.products.presentation.screen.productslist.productsrecyclerview.viewholder.LoadMoreButtonViewHolder
import com.arshapshap.products.feature.products.presentation.screen.productslist.productsrecyclerview.viewholder.ProductViewHolder
import java.lang.IllegalArgumentException

private const val VIEW_TYPE_PRODUCT = 0
private const val VIEW_TYPE_LOAD_MORE_BUTTON = 1
private const val VIEW_TYPE_FILTER_INFO = 2

internal class ProductsAdapter(
    private var list: MutableList<RecyclerViewItem> = mutableListOf(),
    private val onOpenDetails: (Int) -> Unit,
    private val onCategoryClick: (Category) -> Unit,
    private val onCategoryFilterClick: () -> Unit,
    private val onLoadMore: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun setProductsList(newList: List<Product>) {
        val recyclerViewItemsList = mutableListOf<RecyclerViewItem>()
        if (list.isNotEmpty() && list.first() is RecyclerViewItem.CategoryFilterItem)
            recyclerViewItemsList.add(list.first())

        recyclerViewItemsList.addAll(newList.map { RecyclerViewItem.ProductItem(it) })

        if (list.isNotEmpty() && list.last() is RecyclerViewItem.LoadMoreButtonItem)
            recyclerViewItemsList.add(list.last())

        updateList(recyclerViewItemsList)
    }

    fun setLoadMoreButton(visible: Boolean, isLoading: Boolean? = null) {
        val oldLoadMoreButtonItem = getLoadMoreButtonItem()
        val newList = list.takeWhile { it !is RecyclerViewItem.LoadMoreButtonItem }.toMutableList()
        if (visible)
            newList.add(RecyclerViewItem.LoadMoreButtonItem(
                isLoading = isLoading ?: oldLoadMoreButtonItem.isLoading
            ))
        updateList(newList)
    }

    private fun getLoadMoreButtonItem(): RecyclerViewItem.LoadMoreButtonItem {
        return if (list.isNotEmpty() && list.last() is RecyclerViewItem.LoadMoreButtonItem)
            (list.last() as RecyclerViewItem.LoadMoreButtonItem)
        else
            RecyclerViewItem.LoadMoreButtonItem(isLoading = false)
    }

    fun setCategoryFilter(category: Category?) {
        val newList = mutableListOf<RecyclerViewItem>()
        if (category != null)
            newList.add(RecyclerViewItem.CategoryFilterItem(
                category = category
            ))
        newList.addAll(list.dropWhile { it is RecyclerViewItem.CategoryFilterItem })
        updateList(newList)
    }

    private fun updateList(newList: List<RecyclerViewItem>) {
        val diffCallback = DiffCallback(list, newList)
        val diffCourses = DiffUtil.calculateDiff(diffCallback)
        list.clear()
        list.addAll(newList)
        diffCourses.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        VIEW_TYPE_PRODUCT -> ProductViewHolder(getItemProductBinding(parent), onOpenDetails, onCategoryClick)
        VIEW_TYPE_LOAD_MORE_BUTTON -> LoadMoreButtonViewHolder(getLoadMoreButtonBinding(parent), onLoadMore)
        VIEW_TYPE_FILTER_INFO -> CategoryFilterViewHolder(getFilterInfoBinding(parent), onCategoryFilterClick)
        else -> throw IllegalArgumentException("Unsupported view type")
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (holder) {
        is ProductViewHolder -> holder.onBind((list[position] as RecyclerViewItem.ProductItem).product)
        is LoadMoreButtonViewHolder -> holder.onBind(list[position] as RecyclerViewItem.LoadMoreButtonItem)
        is CategoryFilterViewHolder -> holder.onBind(list[position] as RecyclerViewItem.CategoryFilterItem)
        else -> throw IllegalArgumentException("Unsupported ViewHolder class")
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position] is RecyclerViewItem.LoadMoreButtonItem) {
            VIEW_TYPE_LOAD_MORE_BUTTON
        } else if (list[position] is RecyclerViewItem.CategoryFilterItem) {
            VIEW_TYPE_FILTER_INFO
        } else {
            VIEW_TYPE_PRODUCT
        }
    }

    private fun getItemProductBinding(parent: ViewGroup): ItemProductBinding =
        ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    private fun getLoadMoreButtonBinding(parent: ViewGroup): ItemLoadMoreButtonBinding =
        ItemLoadMoreButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    private fun getFilterInfoBinding(parent: ViewGroup): ItemFilterInfoBinding =
        ItemFilterInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    private class DiffCallback(
        private val oldList: List<RecyclerViewItem>,
        private val newList: List<RecyclerViewItem>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return areBothCategories(oldItemPosition, newItemPosition)
                    || areProductsIdsTheSame(oldItemPosition, newItemPosition)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return areCategoriesTheSame(oldItemPosition, newItemPosition)
                    || areProductsTheSame(oldItemPosition, newItemPosition)
        }

        private fun areBothCategories(oldItemPosition: Int, newItemPosition: Int) : Boolean {
            if (oldList[oldItemPosition] !is RecyclerViewItem.CategoryFilterItem) return false
            if (newList[newItemPosition] !is RecyclerViewItem.CategoryFilterItem) return false
            return true
        }

        private fun areProductsIdsTheSame(oldItemPosition: Int, newItemPosition: Int) : Boolean {
            val old = oldList[oldItemPosition] as? RecyclerViewItem.ProductItem ?: return false
            val new = newList[newItemPosition] as? RecyclerViewItem.ProductItem ?: return false
            return old.product.id == new.product.id
        }

        private fun areCategoriesTheSame(oldItemPosition: Int, newItemPosition: Int) : Boolean {
            val old = oldList[oldItemPosition] as? RecyclerViewItem.CategoryFilterItem ?: return false
            val new = newList[newItemPosition] as? RecyclerViewItem.CategoryFilterItem ?: return false
            return old.category == new.category
        }

        private fun areProductsTheSame(oldItemPosition: Int, newItemPosition: Int) : Boolean {
            val old = oldList[oldItemPosition] as? RecyclerViewItem.ProductItem ?: return false
            val new = newList[newItemPosition] as? RecyclerViewItem.ProductItem ?: return false
            return old.product == new.product
        }
    }
}
