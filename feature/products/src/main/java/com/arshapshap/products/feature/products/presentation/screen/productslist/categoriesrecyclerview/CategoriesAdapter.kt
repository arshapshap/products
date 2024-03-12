package com.arshapshap.products.feature.products.presentation.screen.productslist.categoriesrecyclerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arshapshap.products.feature.products.databinding.ItemCategoryBinding
import com.arshapshap.products.feature.products.domain.model.Category

internal class CategoriesAdapter(
    private var list: MutableList<Category> = mutableListOf(),
    private var selectedCategory: Category? = null,
    private val onSelectCategory: (Category) -> Unit,
    private val onUnselectCategory: () -> Unit,
) : RecyclerView.Adapter<CategoryViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun setList(newList: List<Category>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    fun setSelectedCategory(category: Category?) {
        val oldSelectedCategory = selectedCategory
        selectedCategory = category
        list.forEachIndexed { index, it ->
            if (it == oldSelectedCategory || it == category)
                notifyItemChanged(index)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CategoryViewHolder(getItemCategoryBinding(parent)) {
            if (it != selectedCategory)
                onSelectCategory.invoke(it)
            else
                onUnselectCategory.invoke()
        }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) =
        holder.onBind(list[position], list[position] == selectedCategory)

    private fun getItemCategoryBinding(parent: ViewGroup): ItemCategoryBinding =
        ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
}