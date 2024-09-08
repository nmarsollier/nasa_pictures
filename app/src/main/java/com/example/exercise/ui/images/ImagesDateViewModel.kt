package com.example.exercise.ui.images

import androidx.lifecycle.viewModelScope
import com.example.exercise.models.api.dates.refresh
import com.example.exercise.models.extendedDate.ExtendedDateValue
import com.example.exercise.models.extendedDate.FrescoUtils
import com.example.exercise.ui.utils.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
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
        date.asReadyState.sendToState()
    }

    override fun updateDate() = MainScope().launch(Dispatchers.IO) {
        (state.value as? ImagesDateState.Ready)
            ?.date
            ?.refresh(frescoUtils)
            ?.asReadyState
            ?.sendToState()
    }

    private val ExtendedDateValue?.asReadyState: ImagesDateState.Ready
        get() = ImagesDateState.Ready(this)
}
