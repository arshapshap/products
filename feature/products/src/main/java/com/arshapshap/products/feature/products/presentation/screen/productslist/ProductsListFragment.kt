package com.arshapshap.products.feature.products.presentation.screen.productslist

import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.MenuProvider
import androidx.core.view.isGone
import androidx.lifecycle.Lifecycle
import com.arshapshap.products.core.designsystem.extensions.showDialogWithDrawable
import com.arshapshap.products.core.presentation.BaseFragment
import com.arshapshap.products.feature.products.R
import com.arshapshap.products.feature.products.databinding.FragmentProductsListBinding
import com.arshapshap.products.feature.products.presentation.screen.productslist.categoriesrecyclerview.CategoriesAdapter
import com.arshapshap.products.feature.products.presentation.screen.productslist.contract.ProductsListEvent
import com.arshapshap.products.feature.products.presentation.screen.productslist.contract.ProductsListEvent.NoConnectionError
import com.arshapshap.products.feature.products.presentation.screen.productslist.productsrecyclerview.ProductsAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProductsListFragment : BaseFragment<FragmentProductsListBinding, ProductsListViewModel>(
    FragmentProductsListBinding::inflate
), MenuProvider {

    override val viewModel: ProductsListViewModel by viewModel()

    override fun initViews() {
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        with(binding) {
            productsRecyclerView.adapter = ProductsAdapter(
                onOpenDetails = viewModel::openDetails,
                onCategoryClick = viewModel::setCategoryFilter,
                onRemoveCategoryFilter = viewModel::removeCategoryFilter,
                onLoadMore = viewModel::loadMoreProducts
            )
            categoriesRecyclerView.adapter = CategoriesAdapter(
                onSelectCategory = viewModel::setCategoryFilter,
                onUnselectCategory = viewModel::removeCategoryFilter
            )
            productsRecyclerView.setOnScrollChangeListener { _, _, _, _, _ ->
                scrollUpImageButton.isGone = !productsRecyclerView.canScrollVertically(-1)

                if (!productsRecyclerView.canScrollVertically(1) && viewModel.canLoadMore)
                    viewModel.loadMoreProducts()
            }
            swipeRefreshLayout.setOnRefreshListener {
                closeDrawer()
                viewModel.loadData()
                swipeRefreshLayout.isRefreshing = false
            }
            scrollUpImageButton.setOnClickListener {
                productsRecyclerView.smoothScrollToPosition(0)
            }
        }
    }

    override fun subscribe() {
        viewModel.products.observe(viewLifecycleOwner) {
            getProductsAdapter().setList(it)
        }
        viewModel.categories.observe(viewLifecycleOwner) {
            getCategoriesAdapter().setList(it)
        }
        viewModel.categoryFilter.observe(viewLifecycleOwner) {
            getCategoriesAdapter().setSelectedCategory(it)
        }
        viewModel.mainLoading.observe(viewLifecycleOwner) {
            binding.mainLoadingProgressBar.isGone = !it
        }
        viewModel.categoriesLoading.observe(viewLifecycleOwner) {
            requireActivity().invalidateOptionsMenu()
        }
        viewModel.categoryFilter.observe(viewLifecycleOwner) {
            getProductsAdapter().setCategoryFilter(it)
        }
        viewModel.loadingMoreItems.observe(viewLifecycleOwner) {
            getProductsAdapter().setLoadMoreButton(visible = viewModel.canLoadMore, isLoading = it)
        }
        viewModel.error.observe(viewLifecycleOwner) {
            if (it != null)
                showError(it)
            else
                binding.errorLinearLayout.root.isGone = true
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_products_list, menu)
        menu.findItem(R.id.add_filter_button).isVisible = shouldShowFilterButton()
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.add_filter_button -> {
                if (binding.categoriesDrawerLayout.isDrawerOpen(Gravity.RIGHT))
                    closeDrawer()
                else
                    openDrawer()
                return true
            }

            else -> false
        }
    }

    private fun showError(error: ProductsListEvent) {
        val (drawable, headline, hint) = getErrorData(error)

        if (shouldShowDialogWithError(error))
            requireContext().showDialogWithDrawable(drawable, headline, hint)
        else
            with(binding.errorLinearLayout) {
                root.isGone = false
                errorImageView.setImageDrawable(drawable)
                errorHeadlineTextView.text = headline
                errorHintTextView.text = hint
            }
    }

    private fun shouldShowDialogWithError(error: ProductsListEvent): Boolean =
        error is NoConnectionError && error.showDialog
                || error is ProductsListEvent.UnknownError && error.showDialog

    private fun getErrorData(error: ProductsListEvent): Triple<Drawable?, String, String> {
        val drawableId = when (error) {
            is NoConnectionError -> R.drawable.ic_satellite
            is ProductsListEvent.UnknownError -> R.drawable.ic_bug
            ProductsListEvent.EmptyListError -> R.drawable.ic_forklift
        }
        val headlineStringId = when (error) {
            is NoConnectionError -> R.string.no_contact
            is ProductsListEvent.UnknownError -> R.string.something_broke_on_the_server
            ProductsListEvent.EmptyListError -> R.string.carrying_goods
        }
        val hintStringId = when (error) {
            is NoConnectionError -> R.string.check_your_connection_and_try_again
            is ProductsListEvent.UnknownError -> R.string.don_t_worry_the_problem_will_be_solved_soon
            ProductsListEvent.EmptyListError -> R.string.don_t_worry_new_products_will_be_available_soon
        }

        val drawable = ResourcesCompat.getDrawable(resources, drawableId, requireContext().theme)
        val headline = getString(headlineStringId)
        val hint = getString(hintStringId)

        return Triple(drawable, headline, hint)
    }

    private fun shouldShowFilterButton() =
        !viewModel.categories.value.isNullOrEmpty()

    private fun openDrawer() =
        binding.categoriesDrawerLayout.openDrawer(Gravity.RIGHT)

    private fun closeDrawer() =
        binding.categoriesDrawerLayout.closeDrawer(Gravity.RIGHT)

    private fun getProductsAdapter() =
        binding.productsRecyclerView.adapter as ProductsAdapter

    private fun getCategoriesAdapter() =
        binding.categoriesRecyclerView.adapter as CategoriesAdapter
}