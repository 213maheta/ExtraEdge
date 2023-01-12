package com.extraedge.practical_test

import android.app.Application
import com.extraedge.practical_test.koin.mymodule
import com.extraedge.practical_test.koin.networkModule
import com.extraedge.practical_test.koin.rocketDB
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            // declare used Android context
            androidContext(this@MyApp)
            // declare modules

            modules(rocketDB, networkModule ,mymodule)
        }
    }
}