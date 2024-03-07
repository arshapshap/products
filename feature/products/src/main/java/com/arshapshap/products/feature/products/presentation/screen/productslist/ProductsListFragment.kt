package com.arshapshap.products.feature.products.presentation.screen.productslist

import android.app.Dialog
import android.graphics.drawable.Drawable
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import com.arshapshap.products.core.presentation.BaseFragment
import com.arshapshap.products.feature.products.R
import com.arshapshap.products.feature.products.databinding.FragmentProductsListBinding
import com.arshapshap.products.feature.products.presentation.screen.productslist.model.ProductsListError
import com.arshapshap.products.feature.products.presentation.screen.productslist.model.ProductsListError.NoConnectionError
import com.arshapshap.products.feature.products.presentation.screen.productslist.productsrecyclerview.ProductsAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductsListFragment : BaseFragment<FragmentProductsListBinding, ProductsListViewModel>(
    FragmentProductsListBinding::inflate
) {

    override val viewModel: ProductsListViewModel by viewModel()

    override fun initViews() {
        with(binding) {
            productsRecyclerView.adapter = ProductsAdapter(
                onOpenDetails = viewModel::openDetails,
                onCategoryClick = viewModel::setCategoryFilter,
                onCategoryFilterClick = viewModel::removeCategoryFilter,
                onLoadMore = viewModel::loadMore
            )

            productsRecyclerView.setOnScrollChangeListener { _, _, _, _, _ ->
                scrollUpImageButton.isGone = !productsRecyclerView.canScrollVertically(-1)

                if (!productsRecyclerView.canScrollVertically(1) && viewModel.canLoadMore)
                    viewModel.loadMore()
            }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.loadData()
                swipeRefreshLayout.isRefreshing = false
            }

            scrollUpImageButton.setOnClickListener {
                productsRecyclerView.smoothScrollToPosition(0)
            }
        }
    }

    override fun subscribe() {
        viewModel.loadData()
        viewModel.products.observe(viewLifecycleOwner) {
            getProductsAdapter().setProductsList(it)
        }
        viewModel.mainLoading.observe(viewLifecycleOwner) {
            binding.loadingProgressBar.isGone = !it
        }
        viewModel.categoryFilter.observe(viewLifecycleOwner) {
            getProductsAdapter().setCategoryFilter(it)
            if (it != null)
                binding.productsRecyclerView.smoothScrollToPosition(0)
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
}