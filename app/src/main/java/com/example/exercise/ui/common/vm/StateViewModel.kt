package com.example.exercise.ui.common.vm

/**
 * Abstract class that models states, events and actions for all ViewModels.
 * Each ViewModel should follow this pattern to increase code consistency and reduce repetition.
 * NOTE: Don't modify this class by adding more functionality, this one is just for state management.
 *
 * S = State from VM to UI, represents the current data that the view must show
 * E = Event form VM to UI, non related with the UI state (effects)
 * A = Actions : user interaction from the UI to the ViewModel
 */
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class StateViewModel<S : Any, E : Any, A : Any>(
    initialState: S,
) : ViewModel() {
    // UI state
    private val mutableState: MutableStateFlow<S> by lazy {
        MutableStateFlow(initialState)
    }
    val state: StateFlow<S> by lazy {
        mutableState.asStateFlow()
    }

    // Events
    private val eventChannel = Channel<E>()
    val event: Flow<E> by lazy { eventChannel.receiveAsFlow() }

    // sets the state object as current state
    fun S.sendToState() {
        // Skip duplicated states to prevent ui reload
        if (this == mutableState.value) return

        mutableState.value = this
    }

    // sets the event object as current state
    fun E.sendToEvent() {
        val thisEvent = this
        viewModelScope.launch {
            eventChannel.send(thisEvent)
        }
    }

    abstract fun reduce(action: A)
}
