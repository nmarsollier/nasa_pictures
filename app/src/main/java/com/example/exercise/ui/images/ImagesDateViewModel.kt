package com.example.exercise.ui.images

import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import com.example.exercise.common.vm.StateViewModel
import com.example.exercise.models.api.dates.refresh
import com.example.exercise.models.extendedDate.ExtendedDateValue
import com.example.exercise.models.extendedDate.FrescoUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

sealed interface ImagesDateState {
    @Stable
    data object Loading : ImagesDateState

    @Stable
    data class Ready(
        val date: ExtendedDateValue?
    ) : ImagesDateState
}

sealed interface ImagesDateAction {
    @Stable
    data class UpdateDate(val date: ExtendedDateValue?) : ImagesDateAction
}

class ImagesDateViewModel(
    private val frescoUtils: FrescoUtils
) : StateViewModel<ImagesDateState, Unit, ImagesDateAction>(ImagesDateState.Loading) {

    override fun reduce(action: ImagesDateAction) {
        when (action) {
            is ImagesDateAction.UpdateDate -> {
                action.date?.let { updateDate(action.date) } ?: run { updateDate() }
            }
        }
    }

    private fun updateDate(date: ExtendedDateValue?) = viewModelScope.launch(Dispatchers.IO) {
        date.asReadyState.sendToState()
    }

    private fun updateDate() = MainScope().launch(Dispatchers.IO) {
        (state.value as? ImagesDateState.Ready)
            ?.date
            ?.refresh(frescoUtils)
            ?.asReadyState
            ?.sendToState()
    }

    private val ExtendedDateValue?.asReadyState: ImagesDateState.Ready
        get() = ImagesDateState.Ready(this)
}
