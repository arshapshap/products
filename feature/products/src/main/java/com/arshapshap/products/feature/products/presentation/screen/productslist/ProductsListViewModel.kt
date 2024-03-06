package com.arshapshap.products.feature.products.presentation.screen.productslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.arshapshap.products.core.presentation.BaseViewModel
import com.arshapshap.products.feature.products.domain.model.Product
import com.arshapshap.products.feature.products.domain.usecase.GetProductsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductsListViewModel(
    private val getProductsUseCase: GetProductsUseCase
) : BaseViewModel() {

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            val products = getProductsUseCase()
            _products.postValue(products)
//            val products = listOf(
//                Product(
//                    id = 1,
//                    title = "iPhone 9",
//                    description = "An apple mobile which is nothing like apple",
//                    price = 549,
//                    discountPercentage = 4.5,
//                    priceWithoutDiscount = 575,
//                    rating = 4.69,
//                    stock = 94,
//                    brand = "Apple",
//                    category = "smartphones",
//                    thumbnailUrl = "https://cdn.dummyjson.com/product-images/1/thumbnail.jpg",
//                    imagesUrl = listOf()
//                ),
//                Product(
//                    id = 2,
//                    title = "iPhone X",
//                    description = "SIM-Free, Model A19211 6.5-inch Super Retina HD display with OLED technology A12 Bionic chip with ...",
//                    price = 899,
//                    discountPercentage = 17.94,
//                    priceWithoutDiscount = 1095,
//                    rating = 4.44,
//                    stock = 34,
//                    brand = "Apple",
//                    category = "smartphones",
//                    thumbnailUrl = "https://cdn.dummyjson.com/product-images/2/thumbnail.jpg",
//                    imagesUrl = listOf()
//                ),
//                Product(
//                    id = 3,
//                    title = "Samsung Universe 9",
//                    description = "Samsung's new variant which goes beyond Galaxy to the Universe",
//                    price = 1249,
//                    discountPercentage = 15.46,
//                    priceWithoutDiscount = 1477,
//                    rating = 4.09,
//                    stock = 36,
//                    brand = "Samsung",
//                    category = "smartphones",
//                    thumbnailUrl = "https://cdn.dummyjson.com/product-images/3/thumbnail.jpg",
//                    imagesUrl = listOf()
//                ),
//                Product(
//                    id = 4,
//                    title = "OPPOF19",
//                    description = "OPPO F19 is officially announced on April 2021.",
//                    price = 280,
//                    discountPercentage = 28.29,
//                    priceWithoutDiscount = 390,
//                    rating = 4.3,
//                    stock = 123,
//                    brand = "OPPO",
//                    category = "smartphones",
//                    thumbnailUrl = "https://cdn.dummyjson.com/product-images/4/thumbnail.jpg",
//                    imagesUrl = listOf()
//                ),
//                Product(
//                    id = 5,
//                    title = "Huawei P30",
//                    description = "Huawei’s re-badged P30 Pro New Edition was officially unveiled yesterday in Germany and now the device has made its way to the UK.",
//                    price = 499,
//                    discountPercentage = 0.0,
//                    priceWithoutDiscount = 499,
//                    rating = 4.09,
//                    stock = 32,
//                    brand = "Huawei",
//                    category = "smartphones",
//                    thumbnailUrl = "https://cdn.dummyjson.com/product-images/5/thumbnail.jpg",
//                    imagesUrl = listOf()
//                )
//            )
//            _products.postValue(products)
        }
    }

    fun openDetails(productId: Int) {
        // router.openDetails(productId)
    }
}