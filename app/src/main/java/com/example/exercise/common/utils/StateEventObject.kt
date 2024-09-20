package com.example.exercise.common.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

abstract class StateEventObject<S : Any> {
    private val mutableUpdateFlow = MutableSharedFlow<S?>(replay = 0)
    val updateFlow: SharedFlow<S?> = mutableUpdateFlow

    fun S.sendToEvent() {
        val thisEvent = this
        CoroutineScope(Dispatchers.IO).launch {
            mutableUpdateFlow.emit(thisEvent)
        }
    }
}