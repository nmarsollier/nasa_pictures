package com.nmarsollier.nasa.common.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.nmarsollier.nasa.models.modelsModule
import com.nmarsollier.nasa.ui.koinViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.compose.KoinApplication
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Composable
fun KoinPreview(composable: @Composable () -> Unit) {
    val context = LocalContext.current

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