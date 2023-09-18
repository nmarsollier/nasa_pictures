package com.example.exercise.ui.images

import androidx.lifecycle.viewModelScope
import com.example.exercise.models.businessObjects.DateValue
import com.example.exercise.models.businessObjects.ExtendedDateValue
import com.example.exercise.models.database.image.FrescoUtils
import com.example.exercise.ui.utils.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class ImagesDateState {
    data object Loading : ImagesDateState()

    data class Ready(
        val date: ExtendedDateValue?
    ) : ImagesDateState()
}

interface ImagesDateReducer {
    fun updateDate(date: ExtendedDateValue?): Job
    fun updateDate(): Job

    companion object
}

class ImagesDateViewModel(
    private val frescoUtils: FrescoUtils
) : BaseViewModel<ImagesDateState>(ImagesDateState.Loading), ImagesDateReducer {
    override fun updateDate(date: ExtendedDateValue?) = viewModelScope.launch(Dispatchers.IO) {
        mutableState.update { ImagesDateState.Ready(date) }
    }

    override fun updateDate() = MainScope().launch(Dispatchers.IO) {
        (state.value as? ImagesDateState.Ready)?.date?.date?.let { date ->
            mutableState.update { ImagesDateState.Ready(frescoUtils.toDatesData(DateValue(date))) }
        }
    }
}
