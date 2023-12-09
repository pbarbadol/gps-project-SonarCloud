package com.unex.asee.ga02.beergo

import android.app.Application
import com.unex.asee.ga02.beergo.utils.AppContainer

class BeerGoApplication : Application() {
    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }
}