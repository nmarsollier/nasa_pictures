package com.example.exercise.common.vm

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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

abstract class StateViewModel<S : Any, E : Any, A : Any>(
    initialState: S,
) : ViewModel() {
    // UI state
    private val mutableState by lazy {
        MutableStateFlow<S>(initialState)
    }
    val state by lazy { mutableState }

    // Events
    private val mutableEvent by lazy { MutableSharedFlow<E>(replay = 0) }
    val event by lazy { mutableEvent }

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
            mutableEvent.emit(thisEvent)
        }
    }

    abstract fun reduce(action: A)
}
