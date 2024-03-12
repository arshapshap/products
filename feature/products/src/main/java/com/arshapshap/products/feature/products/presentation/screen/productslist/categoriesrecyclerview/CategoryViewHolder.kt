package com.arshapshap.products.feature.products.presentation.screen.productslist.categoriesrecyclerview

import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.arshapshap.products.feature.products.databinding.ItemCategoryBinding
import com.arshapshap.products.feature.products.domain.model.Category


internal class CategoryViewHolder(
    private val binding: ItemCategoryBinding,
    private val onClick: (Category) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(category: Category, isSelected: Boolean) {
        with (binding) {
            categoryNameTextView.text = category.name
            root.setOnClickListener {
                onClick.invoke(category)
            }

            if (isSelected) {
                setDrawableStart(com.arshapshap.products.core.designsystem.R.drawable.ic_radio_button_checked)
            }
            else {
                setDrawableStart(com.arshapshap.products.core.designsystem.R.drawable.ic_radio_button_unchecked)
            }
        }
    }

    private fun ItemCategoryBinding.setDrawableStart(@DrawableRes drawableId: Int) {
        binding.checkBoxImageView.setImageDrawable(ResourcesCompat.getDrawable(root.resources, drawableId, root.context.theme))

    }
}