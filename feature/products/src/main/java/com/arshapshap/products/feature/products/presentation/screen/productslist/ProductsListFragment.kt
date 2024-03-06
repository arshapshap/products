package com.arshapshap.products.feature.products.presentation.screen.productslist

import com.arshapshap.products.core.presentation.BaseFragment
import com.arshapshap.products.feature.products.databinding.FragmentProductsListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductsListFragment : BaseFragment<FragmentProductsListBinding, ProductsListViewModel>(
    FragmentProductsListBinding::inflate
) {

    override val viewModel: ProductsListViewModel by viewModel()

    override fun initViews() {
        //TODO("Not yet implemented")
    }

    override fun subscribe() {
        //TODO("Not yet implemented")
    }

}