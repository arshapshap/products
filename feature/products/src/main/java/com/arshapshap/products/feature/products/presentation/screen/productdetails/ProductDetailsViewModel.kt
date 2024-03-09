package com.arshapshap.products.feature.products.presentation.screen.productdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.arshapshap.products.core.presentation.BaseViewModel
import com.arshapshap.products.feature.products.domain.model.Product
import com.arshapshap.products.feature.products.domain.usecase.GetProductByIdUseCase
import com.arshapshap.products.feature.products.presentation.screen.productdetails.model.ProductDetailsError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class ProductDetailsViewModel internal constructor(
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val productId: Int
) : BaseViewModel() {

    private val _product = MutableLiveData<Product?>()
    internal val product: LiveData<Product?> = _product

    private val _loading = MutableLiveData(true)
    internal val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<ProductDetailsError?>()
    internal val error: LiveData<ProductDetailsError?> = _error

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
                    _error.postValue(ProductDetailsError.ProductNotFoundError)
            } catch (e: UnknownHostException) {
                _error.postValue(ProductDetailsError.NoConnectionError)
            } catch (e: Exception) {
                _error.postValue(ProductDetailsError.UnknownError)
            } finally {
                _loading.postValue(false)
            }
        }
    }
}