package com.arshapshap.products.feature.products.presentation.screen.productslist.productsrecyclerview.viewholder

import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import androidx.recyclerview.widget.RecyclerView
import com.arshapshap.products.feature.products.R
import com.arshapshap.products.feature.products.databinding.ItemProductBinding
import com.arshapshap.products.feature.products.domain.model.Product
import com.bumptech.glide.Glide
import com.google.android.material.color.MaterialColors
import java.text.DecimalFormat
import java.util.Locale


internal class ProductsViewHolder(
    private val binding: ItemProductBinding,
    private val onOpenDetails: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private val resources
        get() = binding.root.context.resources

    fun onBind(product: Product) {
        with (binding) {
            Glide.with(root.context)
                .load(product.thumbnailUrl)
                .placeholder(ColorDrawable(MaterialColors.getColor(root, com.google.android.material.R.attr.backgroundColor)))
                .into(thumbnailImageView)

            ratingTag.text = resources.getString(R.string.rating, product.rating.toString())
            categoryTag.text = product.category.name.capitalize()

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
        }
    }

    private fun Int.toStringWithSpaces(): String {
        return DecimalFormat("###,###").format(this).replace(',', ' ')
    }

    private fun String.capitalize() = this.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(
            Locale.getDefault()
        ) else it.toString()
    }
}