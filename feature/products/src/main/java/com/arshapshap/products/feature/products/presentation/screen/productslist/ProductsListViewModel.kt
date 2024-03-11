package com.arshapshap.products.feature.products.presentation.screen.productslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.arshapshap.products.core.presentation.BaseViewModel
import com.arshapshap.products.feature.products.domain.model.Category
import com.arshapshap.products.feature.products.domain.model.Product
import com.arshapshap.products.feature.products.domain.usecase.GetCategoriesUseCase
import com.arshapshap.products.feature.products.domain.usecase.GetProductsByCategoryUseCase
import com.arshapshap.products.feature.products.domain.usecase.GetProductsUseCase
import com.arshapshap.products.feature.products.presentation.FeatureProductsRouter
import com.arshapshap.products.feature.products.presentation.screen.productslist.model.ProductsListError
import com.arshapshap.products.feature.products.presentation.screen.productslist.model.ProductsListError.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ProductsListViewModel internal constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val getProductsByCategoryUseCase: GetProductsByCategoryUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val router: FeatureProductsRouter
) : BaseViewModel() {

    private val _products = MutableLiveData<List<Product>>()
    internal val products: LiveData<List<Product>> = _products

    private val _categories = MutableLiveData<List<Category>>()
    internal val categories: LiveData<List<Category>> = _categories

    private val _mainLoading = MutableLiveData(true)
    internal val mainLoading: LiveData<Boolean> = _mainLoading

    private val _categoriesLoading = MutableLiveData(true)
    internal val categoriesLoading: LiveData<Boolean> = _categoriesLoading

    private val _loadingMoreItems = MutableLiveData(false)
    internal val loadingMoreItems: LiveData<Boolean> = _loadingMoreItems

    private var _canLoadMore = false
    internal val canLoadMore get() = _canLoadMore

    private val _categoryFilter = MutableLiveData<Category?>()
    internal val categoryFilter: LiveData<Category?> = _categoryFilter

    private val _error = MutableLiveData<ProductsListError?>()
    internal val error: LiveData<ProductsListError?> = _error

    private var _currentPage = 0

    init {
        loadData()
    }

    internal fun loadData() {
        loadProducts()
        loadCategories()
    }

    internal fun openDetails(productId: Int) {
        router.openProductDetails(productId)
    }

    internal fun setCategoryFilter(category: Category) {
        if (categoryFilter.value == category) return
        _categoryFilter.value = category
        loadProducts()
    }

    internal fun removeCategoryFilter() {
        if (categoryFilter.value == null) return
        _categoryFilter.value = null
        loadProducts()
    }

    internal fun loadMoreProducts() {
        _loadingMoreItems.postValue(true)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val productsList = categoryFilter.value?.let {
                    getProductsByCategoryUseCase(it, _currentPage + 1)
                } ?: getProductsUseCase(_currentPage + 1)
                _products.postValue((products.value ?: listOf()) + productsList.list)

                _currentPage += 1
                _canLoadMore = productsList.canLoadMore
            } catch (e: UnknownHostException) {
                _error.postValue(NoConnectionError(showDialog = true))
            } catch (e: Exception) {
                _error.postValue(UnknownError(showDialog = true))
            } finally {
                _loadingMoreItems.postValue(false)
            }
        }
    }

    private fun loadProducts() {
        _products.postValue(listOf())
        _currentPage = 0
        _error.postValue(null)
        _mainLoading.postValue(true)
        _canLoadMore = false
        _loadingMoreItems.postValue(false)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val productsList = categoryFilter.value?.let {
                    getProductsByCategoryUseCase(it)
                } ?: getProductsUseCase()

                _products.postValue(productsList.list)
                if (productsList.list.isEmpty())
                    _error.postValue(EmptyListError)
                _canLoadMore = productsList.canLoadMore
            } catch (e: Exception) {
                when (e) {
                    is UnknownHostException -> _error.postValue(NoConnectionError(showDialog = false))
                    is SocketTimeoutException -> _error.postValue(NoConnectionError(showDialog = false))
                    else -> _error.postValue(UnknownError(showDialog = false))
                }
            } finally {
                _loadingMoreItems.postValue(false)
                _mainLoading.postValue(false)
            }
        }
    }

    private fun loadCategories() {
        _categoriesLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val categories = getCategoriesUseCase()
                _categories.postValue(categories)
            } catch (e: Exception) {
                when (e) {
                    is UnknownHostException -> _error.postValue(NoConnectionError(showDialog = false))
                    is SocketTimeoutException -> _error.postValue(NoConnectionError(showDialog = false))
                    else -> _error.postValue(UnknownError(showDialog = false))
                }
            } finally {
                _categoriesLoading.postValue(false)
            }
        }
    }
}