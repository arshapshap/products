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
import com.arshapshap.products.feature.products.presentation.screen.productslist.model.ProductsListEvent
import com.arshapshap.products.feature.products.presentation.screen.productslist.model.ProductsListEvent.ShowNoConnectionError
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
                onLoadMore = viewModel::loadMore
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

        viewModel.mainLoading.observe(viewLifecycleOwner) {
            binding.loadingProgressBar.isGone = !it
        }

        viewModel.loadingMoreItems.observe(viewLifecycleOwner) {
            getProductsAdapter().setLoading(it)
        }

        viewModel.showLoadMoreButton.observe(viewLifecycleOwner) {
            getProductsAdapter().showLoadMoreButton(it)
        }

        viewModel.error.observe(viewLifecycleOwner) {
            if (it != null)
                showError(it)
            else
                binding.errorLinearLayout.root.isGone = true
        }
    }

    private fun showError(error: ProductsListEvent) {
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

    private fun shouldShowDialog(error: ProductsListEvent): Boolean =
        error is ShowNoConnectionError && error.showDialog
                || error is ProductsListEvent.ShowUnknownError && error.showDialog

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

    private fun getErrorData(error: ProductsListEvent): Triple<Drawable?, String, String> {
        val drawableId = when (error) {
            is ShowNoConnectionError -> R.drawable.ic_satellite
            is ProductsListEvent.ShowUnknownError -> R.drawable.ic_bug
            ProductsListEvent.ShowEmptyListError -> R.drawable.ic_forklift
        }
        val headlineStringId = when (error) {
            is ShowNoConnectionError -> R.string.no_contact
            is ProductsListEvent.ShowUnknownError -> R.string.something_broke_on_the_server
            ProductsListEvent.ShowEmptyListError -> R.string.carrying_goods
        }
        val hintStringId = when (error) {
            is ShowNoConnectionError -> R.string.check_your_connection_and_try_again
            is ProductsListEvent.ShowUnknownError -> R.string.don_t_worry_the_problem_will_be_solved_soon
            ProductsListEvent.ShowEmptyListError -> R.string.don_t_worry_new_products_will_be_available_soon
        }

        val drawable = ResourcesCompat.getDrawable(resources, drawableId, requireContext().theme)
        val headline = getString(headlineStringId)
        val hint =getString(hintStringId)

        return Triple(drawable, headline, hint)
    }

    private fun getProductsAdapter() =
        binding.productsRecyclerView.adapter as ProductsAdapter
}