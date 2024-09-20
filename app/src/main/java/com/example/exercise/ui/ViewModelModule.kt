package com.example.exercise.ui

import com.example.exercise.ui.animatedPreiew.AnimatedPreviewViewModel
import com.example.exercise.ui.home.MainScreenUpdate
import com.example.exercise.ui.home.MainViewModel
import com.example.exercise.ui.imagePreview.ImagePreviewViewModel
import com.example.exercise.ui.images.ImagesViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val koinViewModelModule = module {
    viewModelOf(::AnimatedPreviewViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::ImagePreviewViewModel)
    viewModelOf(::ImagesViewModel)
    singleOf(::MainScreenUpdate)
}
