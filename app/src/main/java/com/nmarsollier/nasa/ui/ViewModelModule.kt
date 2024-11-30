package com.nmarsollier.nasa.ui

import com.nmarsollier.nasa.ui.home.HomeScreenUpdater
import com.nmarsollier.nasa.ui.home.HomeViewModel
import com.nmarsollier.nasa.ui.imageAnimation.ImageAnimationViewModel
import com.nmarsollier.nasa.ui.imagePreview.ImagePreviewViewModel
import com.nmarsollier.nasa.ui.imagesList.ImagesListViewModel
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
