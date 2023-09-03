package com.example.exercise

import android.app.Application
import android.content.Context
import com.example.exercise.ui.utils.FrescoUtils

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeLibrary(applicationContext)
    }

    companion object {
        lateinit var context: Context

        fun initializeLibrary(ctx: Context) {
            context = ctx
            FrescoUtils.initFresco(ctx)
        }
    }
}