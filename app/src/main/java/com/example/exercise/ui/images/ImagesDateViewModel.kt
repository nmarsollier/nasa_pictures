package com.example.exercise.ui.images

import androidx.lifecycle.viewModelScope
import com.example.exercise.models.businessObjects.DateValue
import com.example.exercise.models.businessObjects.ExtendedDateValue
import com.example.exercise.ui.utils.BaseViewModel
import com.example.exercise.ui.utils.toDatesData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class ImagesDateState {
    data object Loading : ImagesDateState()

    data class Ready(
        val date: ExtendedDateValue?
    ) : ImagesDateState()
}

class ImagesDateViewModel(initialState: ImagesDateState = ImagesDateState.Loading) :
    BaseViewModel<ImagesDateState>(initialState) {
    fun updateDate(date: ExtendedDateValue?) = viewModelScope.launch(Dispatchers.IO) {
        mutableState.update { ImagesDateState.Ready(date) }
    }

    fun updateDate() = MainScope().launch(Dispatchers.IO) {
        (state.value as? ImagesDateState.Ready)?.date?.date?.let { date ->
            mutableState.update { ImagesDateState.Ready(DateValue(date).toDatesData()) }
        }
    }
}
