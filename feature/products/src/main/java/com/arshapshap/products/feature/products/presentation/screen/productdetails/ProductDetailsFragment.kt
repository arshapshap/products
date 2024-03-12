package com.arshapshap.products.feature.products.presentation.screen.productdetails

import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import com.arshapshap.products.core.designsystem.extensions.showDialogWithDrawable
import com.arshapshap.products.core.presentation.BaseFragment
import com.arshapshap.products.feature.products.R
import com.arshapshap.products.feature.products.databinding.FragmentProductDetailsBinding
import com.arshapshap.products.feature.products.domain.model.Product
import com.arshapshap.products.feature.products.presentation.screen.productdetails.imagecarousel.ImageCarouselAdapter
import com.arshapshap.products.feature.products.presentation.screen.productdetails.imagecarousel.ImageCarouselLoader
import com.arshapshap.products.feature.products.presentation.screen.productdetails.contract.ProductDetailsEvent
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
        with (binding) {
            imagesViewPager2.adapter = ImageCarouselAdapter()

            val tabLayoutMediator = TabLayoutMediator(
                tabLayout,
                imagesViewPager2,
                true
            ) { _, _ -> }
            tabLayoutMediator.attach()

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.loadData()
                swipeRefreshLayout.isRefreshing = false
            }

            addToCartButton.setOnClickListener {
                viewModel.addToCart()
            }
        }
    }

    override fun subscribe() {
        viewModel.loading.observe(viewLifecycleOwner) {
            binding.loadingProgressBar.isGone = !it
            binding.contentLayout.isGone = it
        }
        viewModel.product.observe(viewLifecycleOwner) {
            if (it != null)
                showContent(it)
            binding.contentLayout.isGone = it == null
        }
        viewModel.error.observe(viewLifecycleOwner) {
            binding.errorLinearLayout.root.isGone = !shouldHideContent(it)
            binding.contentLayout.isGone = shouldHideContent(it)

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
            tabLayout.isGone = product.imagesUrl.size <= 1

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

    private fun showError(error: ProductDetailsEvent) {
        val (drawable, headline, hint) = getErrorData(error)

        if (error is ProductDetailsEvent.NoSuchFunctionality)
            requireContext().showDialogWithDrawable(drawable, headline, hint)
        else
            with (binding.errorLinearLayout) {
                root.isGone = false
                errorImageView.setImageDrawable(drawable)
                errorHeadlineTextView.text = headline
                errorHintTextView.text = hint
            }
    }

    private fun getErrorData(error: ProductDetailsEvent): Triple<Drawable?, String, String> {
        val drawableId = when (error) {
            ProductDetailsEvent.NoConnectionError -> R.drawable.ic_satellite
            ProductDetailsEvent.UnknownError -> R.drawable.ic_bug
            ProductDetailsEvent.ProductNotFoundError -> R.drawable.ic_mistery
            ProductDetailsEvent.NoSuchFunctionality -> R.drawable.ic_always_has_been
        }
        val headlineStringId = when (error) {
            ProductDetailsEvent.NoConnectionError -> R.string.no_contact
            ProductDetailsEvent.UnknownError -> R.string.something_broke_on_the_server
            ProductDetailsEvent.ProductNotFoundError -> R.string.something_weird
            ProductDetailsEvent.NoSuchFunctionality -> R.string.it_s_all_staged
        }
        val hintStringId = when (error) {
            ProductDetailsEvent.NoConnectionError -> R.string.check_your_connection_and_try_again
            ProductDetailsEvent.UnknownError -> R.string.don_t_worry_the_problem_will_be_solved_soon
            ProductDetailsEvent.ProductNotFoundError -> R.string.the_product_was_not_found_an_error_may_have_occurred
            ProductDetailsEvent.NoSuchFunctionality -> R.string.it_s_just_test_task
        }

        val drawable = ResourcesCompat.getDrawable(resources, drawableId, requireContext().theme)
        val headline = getString(headlineStringId)
        val hint = getString(hintStringId)

        return Triple(drawable, headline, hint)
    }

    private fun shouldHideContent(event: ProductDetailsEvent?) =
        event != null && event !is ProductDetailsEvent.NoSuchFunctionality

    private fun getImageCarouselAdapter() =
        binding.imagesViewPager2.adapter as ImageCarouselAdapter
}