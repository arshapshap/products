package com.arshapshap.products.feature.products.presentation.screen.productslist

import android.app.Dialog
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.MenuProvider
import androidx.core.view.isGone
import androidx.lifecycle.Lifecycle
import com.arshapshap.products.core.presentation.BaseFragment
import com.arshapshap.products.feature.products.R
import com.arshapshap.products.feature.products.databinding.FragmentProductsListBinding
import com.arshapshap.products.feature.products.presentation.screen.productslist.categoriesrecyclerview.CategoriesAdapter
import com.arshapshap.products.feature.products.presentation.screen.productslist.model.ProductsListError
import com.arshapshap.products.feature.products.presentation.screen.productslist.model.ProductsListError.NoConnectionError
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
                onCategoryFilterClick = viewModel::removeCategoryFilter,
                onLoadMore = viewModel::loadMore
            )
            categoriesRecyclerView.adapter = CategoriesAdapter(
                onSelectCategory = {
                    closeDrawer()
                    viewModel.setCategoryFilter(it)
                },
                onUnselectCategory = {
                    closeDrawer()
                    viewModel.removeCategoryFilter()
                }
            )
            productsRecyclerView.setOnScrollChangeListener { _, _, _, _, _ ->
                scrollUpImageButton.isGone = !productsRecyclerView.canScrollVertically(-1)

                if (!productsRecyclerView.canScrollVertically(1) && viewModel.canLoadMore)
                    viewModel.loadMore()
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
            binding.loadingProgressBar.isGone = !it
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

    private fun shouldShowFilterButton() =
        !viewModel.categories.value.isNullOrEmpty()

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.add_filter_button -> {
                if (viewModel.mainLoading.value == true || viewModel.categories.value?.isEmpty() == true)
                    return true
                else if (binding.categoriesDrawerLayout.isDrawerOpen(Gravity.RIGHT))
                    closeDrawer()
                else
                    openDrawer()
                return true
            }
            else -> false
        }
    }

    private fun openDrawer() {
        binding.categoriesDrawerLayout.openDrawer(Gravity.RIGHT)
    }

    private fun closeDrawer() {
        binding.categoriesDrawerLayout.closeDrawer(Gravity.RIGHT)
    }

    private fun showError(error: ProductsListError) {
        val (drawable, headline, hint) = getErrorData(error)

        if (shouldShowDialog(error))
            showDialogWithError(drawable, headline, hint)
        else
            with (binding.errorLinearLayout) {
                root.isGone = false
                errorImageView.setImageDrawable(drawable)
                errorHeadlineTextView.text = headline
                errorHintTextView.text = hint
            }
    }

    private fun shouldShowDialog(error: ProductsListError): Boolean =
        error is NoConnectionError && error.showDialog
                || error is ProductsListError.UnknownError && error.showDialog

    private fun showDialogWithError(
        drawable: Drawable?,
        headline: String,
        hint: String
    ) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.window?.decorView?.setBackgroundResource(com.arshapshap.products.core.designsystem.R.drawable.shape_normal_rounded_rectangle)
        dialog.setContentView(R.layout.dialog_error)

        val errorImageView: ImageView = dialog.findViewById(R.id.error_image_view)
        errorImageView.setImageDrawable(drawable)

        val headlineTextView: TextView = dialog.findViewById(R.id.error_headline_text_view)
        headlineTextView.text = headline

        val hintTextView: TextView = dialog.findViewById(R.id.error_hint_text_view)
        hintTextView.text = hint

        dialog.findViewById<Button>(R.id.ok_button).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun getErrorData(error: ProductsListError): Triple<Drawable?, String, String> {
        val drawableId = when (error) {
            is NoConnectionError -> R.drawable.ic_satellite
            is ProductsListError.UnknownError -> R.drawable.ic_bug
            ProductsListError.EmptyListError -> R.drawable.ic_forklift
        }
        val headlineStringId = when (error) {
            is NoConnectionError -> R.string.no_contact
            is ProductsListError.UnknownError -> R.string.something_broke_on_the_server
            ProductsListError.EmptyListError -> R.string.carrying_goods
        }
        val hintStringId = when (error) {
            is NoConnectionError -> R.string.check_your_connection_and_try_again
            is ProductsListError.UnknownError -> R.string.don_t_worry_the_problem_will_be_solved_soon
            ProductsListError.EmptyListError -> R.string.don_t_worry_new_products_will_be_available_soon
        }

        val drawable = ResourcesCompat.getDrawable(resources, drawableId, requireContext().theme)
        val headline = getString(headlineStringId)
        val hint = getString(hintStringId)

        return Triple(drawable, headline, hint)
    }

    private fun getProductsAdapter() =
        binding.productsRecyclerView.adapter as ProductsAdapter

    private fun getCategoriesAdapter() =
        binding.categoriesRecyclerView.adapter as CategoriesAdapter
}