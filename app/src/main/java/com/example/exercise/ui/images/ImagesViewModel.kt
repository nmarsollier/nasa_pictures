package com.example.exercise.ui.images

import androidx.lifecycle.viewModelScope
import com.example.exercise.models.api.tools.CacheStrategy
import com.example.exercise.models.api.tools.Result
import com.example.exercise.models.businessObjects.ExtendedDateValue
import com.example.exercise.models.businessObjects.ImageValue
import com.example.exercise.models.useCases.FetchImagesUseCase
import com.example.exercise.ui.utils.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class Destination {
    data class Preview(val image: ImageValue) : Destination()
    data class Animate(val date: ExtendedDateValue) : Destination()
}

sealed class ImagesState {
    data object Loading : ImagesState()

    data object Error : ImagesState()

    data class Redirect(val destination: Destination) : ImagesState()

    data class Ready(
        val images: List<ImageValue>, val cacheStrategy: CacheStrategy
    ) : ImagesState()
}

class ImagesViewModel(initialState: ImagesState = ImagesState.Loading) :
    BaseViewModel<ImagesState>(initialState) {
    fun fetchImages(date: ExtendedDateValue?) = viewModelScope.launch(Dispatchers.IO) {
        mutableState.update { ImagesState.Loading }

        val queryDate = date?.date ?: run {
            mutableState.update { ImagesState.Error }
            return@launch
        }

        FetchImagesUseCase.fetchImages(queryDate).collect {
            when (val result = it) {
                is Result.Error -> mutableState.update { ImagesState.Error }
                is Result.Success -> mutableState.update {
                    ImagesState.Ready(result.data, result.cacheStrategy)
                }
            }
        }
    }

    fun redirect(destination: Destination) {
        mutableState.update {
            ImagesState.Redirect(destination)
        }
    }
}
