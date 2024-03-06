package com.arshapshap.products.presentation

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.arshapshap.products.R
import com.arshapshap.products.core.presentation.BaseActivity
import com.arshapshap.products.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(
    ActivityMainBinding::inflate
) {


    override fun initViews() {
        configureToolbar(getNavController())
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun configureToolbar(navController: NavController) {
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    private fun getNavController() =
        (supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment).navController
}