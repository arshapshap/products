package com.arshapshap.products.feature.products.presentation.screen.productslist

import com.arshapshap.products.core.presentation.BaseFragment
import com.arshapshap.products.feature.products.databinding.FragmentProductsListBinding
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
        }
    }

    override fun subscribe() {
        viewModel.loadData()

        viewModel.products.observe(viewLifecycleOwner) {
            getProductsAdapter().setList(it)
        }
    }

    private fun getProductsAdapter() =
        binding.productsRecyclerView.adapter as ProductsAdapter
}