package com.example.exercise.ui.images

import androidx.lifecycle.viewModelScope
import com.example.exercise.common.vm.StateViewModel
import com.example.exercise.models.api.dates.refresh
import com.example.exercise.models.extendedDate.ExtendedDateValue
import com.example.exercise.models.extendedDate.FrescoUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

sealed interface ImagesDateState {
    data object Loading : ImagesDateState

    data class Ready(
        val date: ExtendedDateValue?
    ) : ImagesDateState
}

sealed interface ImagesDateEvent {
}

sealed interface ImagesDateAction {
    data class UpdateDate(val date: ExtendedDateValue?) : ImagesDateAction
}

class ImagesDateViewModel(
    private val frescoUtils: FrescoUtils
) : StateViewModel<ImagesDateState, ImagesDateEvent, ImagesDateAction>(ImagesDateState.Loading) {

    override fun reduce(action: ImagesDateAction) {
        when (action) {
            is ImagesDateAction.UpdateDate -> {
                action.date?.let { updateDate(action.date) } ?: run { updateDate() }
            }
        }
    }

    fun updateDate(date: ExtendedDateValue?) = viewModelScope.launch(Dispatchers.IO) {
        date.asReadyState.sendToState()
    }

    fun updateDate() = MainScope().launch(Dispatchers.IO) {
        (state.value as? ImagesDateState.Ready)
            ?.date
            ?.refresh(frescoUtils)
            ?.asReadyState
            ?.sendToState()
    }

    private val ExtendedDateValue?.asReadyState: ImagesDateState.Ready
        get() = ImagesDateState.Ready(this)
}
