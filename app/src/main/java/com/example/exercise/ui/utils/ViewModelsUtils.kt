package com.example.exercise.ui.utils

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.annotation.MainThread
import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.Composable
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel

@VisibleForTesting
val mockedViewModels = hashMapOf<String, Lazy<out ViewModel>>()

@VisibleForTesting
fun mockViewModel(vm: ViewModel) {
    mockedViewModels[vm::class.simpleName.toString()] = lazy { vm }
}

@VisibleForTesting
fun cleanMockedViewModels() {
    mockedViewModels.clear()
}

@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.providedViewModels(
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<VM> {
    val className = VM::class.simpleName.toString()
    mockedViewModels[className]?.let {
        return it as Lazy<VM>
    }

    return this.viewModels(extrasProducer, factoryProducer)
}

@Suppress("MissingJvmstatic")
@Composable
inline fun <reified VM : ViewModel> providedViewModel(
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    },
    key: String? = null,
    factory: ViewModelProvider.Factory? = null,
    extras: CreationExtras = if (viewModelStoreOwner is HasDefaultViewModelProviderFactory) {
        viewModelStoreOwner.defaultViewModelCreationExtras
    } else {
        CreationExtras.Empty
    }
): VM {
    val className = VM::class.simpleName.toString()
    mockedViewModels[className]?.let {
        return (it as Lazy<VM>).value
    }

    return viewModel(viewModelStoreOwner, key, factory, extras)
}
