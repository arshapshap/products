package com.arshapshap.products

import android.app.Application
import com.arshapshap.products.core.network.di.coreNetworkModule
import com.arshapshap.products.di.appModule
import com.arshapshap.products.feature.products.di.featureProductsModule
import org.koin.core.context.GlobalContext.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(
                appModule,
                coreNetworkModule,
                featureProductsModule
            )
        }
    }
}