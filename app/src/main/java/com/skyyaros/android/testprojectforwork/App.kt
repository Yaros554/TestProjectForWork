package com.skyyaros.android.testprojectforwork

import android.app.Application

class App: Application() {
    companion object {
        lateinit var component: DaggerComponent
    }

    override fun onCreate() {
        super.onCreate()
        component = DaggerDaggerComponent.builder()
            .daggerModule(DaggerModule(this))
            .build()
    }
}