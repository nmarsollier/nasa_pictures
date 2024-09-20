package com.example.exercise.common.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.exercise.models.modelsModule
import com.example.exercise.ui.koinViewModelModule
import com.facebook.drawee.backends.pipeline.Fresco
import org.koin.android.ext.koin.androidContext
import org.koin.compose.KoinApplication
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Composable
fun KoinPreview(composable: @Composable () -> Unit) {
    val context = LocalContext.current

    if (!Fresco.hasBeenInitialized()) {
        Fresco.initialize(context)
    }

    MaterialTheme {
        KoinApplication(application = {
            androidContext(context)
            modules(modelsModule, koinViewModelModule)
        }, composable)
    }
}

inline fun <reified T> getKoinInstance(): T {
    return object : KoinComponent {
        val value: T by inject()
    }.value
}