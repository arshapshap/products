package com.arshapshap.products.feature.products.presentation.screen.productslist.productsrecyclerview.viewholder

import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.arshapshap.products.feature.products.R
import com.arshapshap.products.feature.products.databinding.ItemProductBinding
import com.arshapshap.products.feature.products.domain.model.Category
import com.arshapshap.products.feature.products.domain.model.Product
import com.arshapshap.utils.toStringWithSpaces
import com.google.android.material.color.MaterialColors


internal class ProductViewHolder(
    private val binding: ItemProductBinding,
    private val onOpenDetails: (Int) -> Unit,
    private val onCategoryClick: (Category) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private val resources
        get() = binding.root.context.resources

    fun onBind(product: Product) {
        with (binding) {
            thumbnailImageView.load(product.thumbnailUrl) {
                placeholder(ColorDrawable(MaterialColors.getColor(root, com.google.android.material.R.attr.backgroundColor)))
            }

            ratingTag.text = resources.getString(R.string.rating, product.rating.toString())
            categoryTag.text = product.category.name

            titleTextView.text = product.title
            descriptionTextView.text = product.description
            priceTextView.text = resources.getString(R.string.price_in_dollars, product.price.toStringWithSpaces())

            if (product.discountPercentage > 0) {
                priceWithoutDiscountTextView.text = resources.getString(
                    R.string.price_in_dollars,
                    product.priceWithoutDiscount.toStringWithSpaces()
                )
                priceWithoutDiscountTextView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }

            openDetailsButton.setOnClickListener {
                onOpenDetails.invoke(product.id)
            }

            categoryTag.setOnClickListener {
                onCategoryClick.invoke(product.category)
            }
        }
    }
}