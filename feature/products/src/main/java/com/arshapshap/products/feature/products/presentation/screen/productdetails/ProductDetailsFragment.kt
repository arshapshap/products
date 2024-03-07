package com.arshapshap.products.feature.products.presentation.screen.productdetails

import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import com.arshapshap.products.core.presentation.BaseFragment
import com.arshapshap.products.feature.products.R
import com.arshapshap.products.feature.products.databinding.FragmentProductDetailsBinding
import com.arshapshap.products.feature.products.domain.model.Product
import com.arshapshap.products.feature.products.presentation.screen.productdetails.imagecarousel.ImageCarouselAdapter
import com.arshapshap.products.feature.products.presentation.screen.productdetails.imagecarousel.ImageCarouselLoader
import com.arshapshap.products.feature.products.presentation.screen.productdetails.model.ProductDetailsError
import com.arshapshap.utils.toStringWithSpaces
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class ProductDetailsFragment : BaseFragment<FragmentProductDetailsBinding, ProductDetailsViewModel>(
    FragmentProductDetailsBinding::inflate
) {

    companion object {

        fun createBundle(productId: Int): Bundle {
            return bundleOf(PRODUCT_ID_KEY to productId)
        }

        private const val PRODUCT_ID_KEY = "PRODUCT_ID_KEY"
    }

    override val viewModel: ProductDetailsViewModel by viewModel(
        parameters = { parametersOf(arguments?.getInt(PRODUCT_ID_KEY)) }
    )

    override fun initViews() {
        binding.imagesViewPager2.adapter = ImageCarouselAdapter()

        val tabLayoutMediator = TabLayoutMediator(
            binding.tabLayout, binding.imagesViewPager2, true
        ) { tab, position -> }
        tabLayoutMediator.attach()
    }

    override fun subscribe() {
        viewModel.loadData()
        viewModel.loading.observe(viewLifecycleOwner) {
            binding.loadingProgressBar.isGone = !it
        }
        viewModel.product.observe(viewLifecycleOwner) {
            if (it != null)
                showContent(it)
            binding.contentLayout.isGone = it == null
        }
        viewModel.error.observe(viewLifecycleOwner) {
            binding.errorLinearLayout.root.isGone = it == null
            binding.contentLayout.isGone = it != null

            if (it != null)
                showError(it)
        }
    }

    private fun showContent(product: Product) {
        with (binding) {
            ImageCarouselLoader(
                context = requireContext(),
                adapter = getImageCarouselAdapter()
            ).loadImages(product.imagesUrl)

            ratingTag.text = resources.getString(R.string.rating, product.rating.toString())
            categoryTag.text = product.category.name
            stockTag.text = getString(R.string.stock, product.stock.toString())
            titleTextView.text = product.title
            descriptionTextView.text = product.description
            brandTextView.text = product.brand
            priceTextView.text = resources.getString(R.string.price_in_dollars, product.price.toStringWithSpaces())

            if (product.discountPercentage > 0) {
                priceWithoutDiscountTextView.text = resources.getString(
                    R.string.price_in_dollars,
                    product.priceWithoutDiscount.toStringWithSpaces()
                )
                priceWithoutDiscountTextView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }
        }
    }

    private fun showError(error: ProductDetailsError) {
        val (drawable, headline, hint) = getErrorData(error)

        with (binding.errorLinearLayout) {
            root.isGone = false
            errorImageView.setImageDrawable(drawable)
            errorHeadlineTextView.text = headline
            errorHintTextView.text = hint
        }
    }

    private fun getErrorData(error: ProductDetailsError): Triple<Drawable?, String, String> {
        val drawableId = when (error) {
            ProductDetailsError.NoConnectionError -> R.drawable.ic_satellite
            ProductDetailsError.UnknownError -> R.drawable.ic_bug
            ProductDetailsError.ProductNotFoundError -> R.drawable.ic_mistery
        }
        val headlineStringId = when (error) {
            ProductDetailsError.NoConnectionError -> R.string.no_contact
            ProductDetailsError.UnknownError -> R.string.something_broke_on_the_server
            ProductDetailsError.ProductNotFoundError -> R.string.something_weird
        }
        val hintStringId = when (error) {
            ProductDetailsError.NoConnectionError -> R.string.check_your_connection_and_try_again
            ProductDetailsError.UnknownError -> R.string.don_t_worry_the_problem_will_be_solved_soon
            ProductDetailsError.ProductNotFoundError -> R.string.the_product_was_not_found_an_error_may_have_occurred
        }

        val drawable = ResourcesCompat.getDrawable(resources, drawableId, requireContext().theme)
        val headline = getString(headlineStringId)
        val hint = getString(hintStringId)

        return Triple(drawable, headline, hint)
    }

    private fun getImageCarouselAdapter() =
        binding.imagesViewPager2.adapter as ImageCarouselAdapter
}