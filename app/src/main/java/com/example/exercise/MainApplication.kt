package com.example.exercise

import android.app.Application
import com.example.exercise.models.modelsModule
import com.example.exercise.ui.koinViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin

class MainApplication : Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()

            androidContext(this@MainApplication)

            modules(
                modelsModule,
                koinViewModelModule
            )
        }
    }
}
