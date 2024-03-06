package com.arshapshap.products.feature.products.presentation.screen.productslist.productsrecyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.arshapshap.products.feature.products.databinding.ItemProductBinding
import com.arshapshap.products.feature.products.domain.model.Product


internal class ProductsAdapter(
    private var list: MutableList<Product> = mutableListOf(),
    private val onOpenDetails: (Int) -> Unit
) : RecyclerView.Adapter<ProductsViewHolder>() {

    fun setList(newList: List<Product>) {
        val diffCallback = DiffCallback(list, newList)
        val diffCourses = DiffUtil.calculateDiff(diffCallback)
        list.clear()
        list.addAll(newList)
        diffCourses.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder =
        ProductsViewHolder(getBinding(parent), onOpenDetails)

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) =
        holder.onBind(list[position])

    private fun getBinding(parent: ViewGroup): ItemProductBinding =
        ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)

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
