package com.arshapshap.products.feature.products.presentation.screen.productslist

import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import com.arshapshap.products.core.presentation.BaseFragment
import com.arshapshap.products.feature.products.R
import com.arshapshap.products.feature.products.databinding.FragmentProductsListBinding
import com.arshapshap.products.feature.products.presentation.screen.productslist.model.ProductsListError
import com.arshapshap.products.feature.products.presentation.screen.productslist.productsrecyclerview.ProductsAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductsListFragment : BaseFragment<FragmentProductsListBinding, ProductsListViewModel>(
    FragmentProductsListBinding::inflate
) {

    override val viewModel: ProductsListViewModel by viewModel()

    override fun initViews() {
        with(binding) {
            productsRecyclerView.adapter = ProductsAdapter(
                onOpenDetails = viewModel::openDetails
            )

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.loadData()
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    override fun subscribe() {
        viewModel.loadData()

        viewModel.products.observe(viewLifecycleOwner) {
            getProductsAdapter().setList(it)
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            binding.loadingProgressBar.isGone = !it
        }

        viewModel.error.observe(viewLifecycleOwner) {
            binding.errorLinearLayout.isGone = it == null

            if (it != null)
                showError(it)
        }
    }

    private fun showError(error: ProductsListError) {
        with (binding) {
            val drawableId = when (error) {
                ProductsListError.NoConnection -> R.drawable.ic_satellite
                ProductsListError.Unknown -> R.drawable.ic_bug
                ProductsListError.EmptyList -> R.drawable.ic_forklift
            }
            val headlineStringId = when (error) {
                ProductsListError.NoConnection -> R.string.no_contact
                ProductsListError.Unknown -> R.string.something_broke_on_the_server
                ProductsListError.EmptyList -> R.string.carrying_goods
            }
            val hintStringId = when (error) {
                ProductsListError.NoConnection -> R.string.check_your_connection_and_try_again
                ProductsListError.Unknown -> R.string.don_t_worry_the_problem_will_be_solved_soon
                ProductsListError.EmptyList -> R.string.don_t_worry_new_products_will_be_available_soon
            }
            errorImageView.setImageDrawable(ResourcesCompat.getDrawable(resources, drawableId, requireContext().theme))
            errorHeadlineTextView.text = getString(headlineStringId)
            errorHintTextView.text = getString(hintStringId)
        }
    }

    private fun getProductsAdapter() =
        binding.productsRecyclerView.adapter as ProductsAdapter
}