package com.arshapshap.products.navigation

import androidx.navigation.NavController
import com.arshapshap.products.R
import com.arshapshap.products.feature.products.presentation.FeatureProductsRouter
import com.arshapshap.products.feature.products.presentation.screen.productdetails.ProductDetailsFragment

class Navigator : FeatureProductsRouter {

    private var navController: NavController? = null

    fun attachNavController(navController: NavController, graph: Int) {
        navController.setGraph(graph)
        this.navController = navController
    }

    fun detachNavController(navController: NavController) {
        if (this.navController == navController) {
            this.navController = null
        }
    }

    override fun openProductDetails(productId: Int) {
        navController?.navigate(
            R.id.action_products_fragment_to_product_details_fragment,
            ProductDetailsFragment.createBundle(productId)
        )
    }
}