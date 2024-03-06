package com.arshapshap.products.feature.products.presentation.screen.productslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.arshapshap.products.core.presentation.BaseViewModel
import com.arshapshap.products.feature.products.domain.model.Product
import com.arshapshap.products.feature.products.domain.usecase.GetProductsUseCase
import com.arshapshap.products.feature.products.presentation.screen.productslist.model.ProductsListError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class ProductsListViewModel(
    private val getProductsUseCase: GetProductsUseCase
) : BaseViewModel() {

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    private val _loading = MutableLiveData(true)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<ProductsListError?>()
    val error: LiveData<ProductsListError?> = _error

    fun loadData() {
        _products.postValue(listOf())
        _error.postValue(null)
        _loading.postValue(true)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val products = getProductsUseCase()
                _products.postValue(products)

                if (products.isEmpty())
                    _error.postValue(ProductsListError.EmptyList)

            } catch (e: UnknownHostException) {
                _error.postValue(ProductsListError.NoConnection)
            } catch (e: Exception) {
                _error.postValue(ProductsListError.Unknown)
            } finally {
                _loading.postValue(false)
            }
        }
    }

    fun openDetails(productId: Int) {
        // router.openDetails(productId)
    }
}