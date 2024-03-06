package com.arshapshap.products

import android.app.Application
import org.koin.core.context.GlobalContext.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(
            )
        }
    }
}