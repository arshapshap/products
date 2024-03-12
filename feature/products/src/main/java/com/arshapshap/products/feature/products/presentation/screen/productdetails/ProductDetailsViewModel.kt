package com.arshapshap.products.feature.products.presentation.screen.productdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.arshapshap.products.core.presentation.BaseViewModel
import com.arshapshap.products.feature.products.domain.model.Product
import com.arshapshap.products.feature.products.domain.usecase.GetProductByIdUseCase
import com.arshapshap.products.feature.products.presentation.screen.productdetails.contract.ProductDetailsEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ProductDetailsViewModel internal constructor(
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val productId: Int
) : BaseViewModel() {

    private val _product = MutableLiveData<Product?>()
    internal val product: LiveData<Product?> = _product

    private val _loading = MutableLiveData(true)
    internal val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<ProductDetailsEvent?>()
    internal val error: LiveData<ProductDetailsEvent?> = _error

    init {
        loadData()
    }

    internal fun loadData() {
        _loading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val product = getProductByIdUseCase.invoke(productId)
                _product.postValue(product)

                if (product == null)
                    _error.postValue(ProductDetailsEvent.ProductNotFoundError)
            } catch (e: Exception) {
                when (e) {
                    is UnknownHostException -> _error.postValue(ProductDetailsEvent.NoConnectionError)
                    is SocketTimeoutException -> _error.postValue(ProductDetailsEvent.NoConnectionError)
                    else -> _error.postValue(ProductDetailsEvent.UnknownError)
                }
            } finally {
                _loading.postValue(false)
            }
        }
    }

    internal fun addToCart() {
        _error.postValue(ProductDetailsEvent.NoSuchFunctionality)
    }
}