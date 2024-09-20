package com.example.exercise.ui

import com.example.exercise.ui.imageAnimation.ImageAnimationViewModel
import com.example.exercise.ui.home.HomeScreenUpdater
import com.example.exercise.ui.home.HomeViewModel
import com.example.exercise.ui.imagePreview.ImagePreviewViewModel
import com.example.exercise.ui.imagesList.ImagesListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val koinViewModelModule = module {
    viewModelOf(::ImageAnimationViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::ImagePreviewViewModel)
    viewModelOf(::ImagesListViewModel)
    singleOf(::HomeScreenUpdater)
}
