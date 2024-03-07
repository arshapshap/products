package com.arshapshap.products.feature.products.presentation.screen.productslist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.arshapshap.products.core.presentation.BaseViewModel
import com.arshapshap.products.feature.products.domain.model.Category
import com.arshapshap.products.feature.products.domain.model.Product
import com.arshapshap.products.feature.products.domain.usecase.GetProductsByCategoryUseCase
import com.arshapshap.products.feature.products.domain.usecase.GetProductsUseCase
import com.arshapshap.products.feature.products.presentation.screen.productslist.model.ProductsListEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class ProductsListViewModel(
    private val getProductsUseCase: GetProductsUseCase,
    private val getProductsByCategoryUseCase: GetProductsByCategoryUseCase,
) : BaseViewModel() {

    private val _products = MutableLiveData<List<Product>>()
    internal val products: LiveData<List<Product>> = _products

    private val _mainLoading = MutableLiveData(true)
    internal val mainLoading: LiveData<Boolean> = _mainLoading

    private val _loadingMoreItems = MutableLiveData(false)
    internal val loadingMoreItems: LiveData<Boolean> = _loadingMoreItems

    private val _showLoadMoreButton = MutableLiveData(false)
    internal val showLoadMoreButton: LiveData<Boolean> = _showLoadMoreButton

    private val _categoryFilter = MutableLiveData<Category?>()
    internal val categoryFilter: LiveData<Category?> = _categoryFilter

    private val _error = MutableLiveData<ProductsListEvent?>()
    internal val error: LiveData<ProductsListEvent?> = _error

    private var _currentPage = 0

    internal fun loadData() {
        _products.postValue(listOf())
        _currentPage = 0
        _error.postValue(null)
        _mainLoading.postValue(true)
        _showLoadMoreButton.postValue(false)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val productsList = categoryFilter.value?.let {
                    getProductsByCategoryUseCase(it)
                } ?: getProductsUseCase()

                _products.postValue(productsList.list)

                if (productsList.list.isEmpty())
                    _error.postValue(ProductsListEvent.ShowEmptyListError)
                _showLoadMoreButton.postValue(productsList.canLoadMore)
            } catch (e: UnknownHostException) {
                _error.postValue(ProductsListEvent.ShowNoConnectionError(showDialog = false))
            } catch (e: Exception) {
                Log.e("VIEWMODEL", e.message ?: "")
                _error.postValue(ProductsListEvent.ShowUnknownError(showDialog = false))
            } finally {
                _mainLoading.postValue(false)
            }
        }
    }

    internal fun openDetails(productId: Int) {
        // router.openDetails(productId)
    }

    internal fun setCategoryFilter(category: Category) {
        if (categoryFilter.value == category) return
        _categoryFilter.value = category
        loadData()
    }

    internal fun removeCategoryFilter() {
        if (categoryFilter.value == null) return
        _categoryFilter.value = null
        loadData()
    }

    internal fun loadMore() {
        _loadingMoreItems.postValue(true)

        _currentPage += 1
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val productsList = categoryFilter.value?.let {
                    getProductsByCategoryUseCase(it, _currentPage)
                } ?: getProductsUseCase(_currentPage)
                _products.postValue((products.value ?: listOf()) + productsList.list)

                _showLoadMoreButton.postValue(productsList.canLoadMore)
            } catch (e: UnknownHostException) {
                _error.postValue(ProductsListEvent.ShowNoConnectionError(showDialog = true))
            } catch (e: Exception) {
                _error.postValue(ProductsListEvent.ShowUnknownError(showDialog = true))
            } finally {
                _loadingMoreItems.postValue(false)
            }
        }
    }
}