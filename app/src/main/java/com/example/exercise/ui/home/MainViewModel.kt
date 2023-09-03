package com.example.exercise.ui.home

import androidx.lifecycle.viewModelScope
import com.example.exercise.models.api.tools.CacheStrategy
import com.example.exercise.models.api.tools.Result
import com.example.exercise.models.businessObjects.ExtendedDateValue
import com.example.exercise.models.useCases.FetchDatesUseCase
import com.example.exercise.ui.utils.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class Destination {
    data class Images(val date: ExtendedDateValue) : Destination()
}

sealed class MainState {
    data object Loading : MainState()

    data object Error : MainState()

    data class Redirect(val destination: Destination) : MainState()

    data class Ready(
        val dates: List<ExtendedDateValue>, val cacheStrategy: CacheStrategy
    ) : MainState()
}

class MainViewModel : BaseViewModel<MainState>(MainState.Loading) {
    fun fetchDates() = viewModelScope.launch(Dispatchers.IO) {
        mutableState.update { MainState.Loading }
        FetchDatesUseCase.listDates().collect {
            when (val result = it) {
                is Result.Error -> mutableState.update { MainState.Error }
                is Result.Success -> mutableState.update {
                    MainState.Ready(result.data, result.cacheStrategy)
                }
            }
        }
    }

    fun redirect(destination: Destination) {
        mutableState.update {
            MainState.Redirect(destination)
        }
    }
}
