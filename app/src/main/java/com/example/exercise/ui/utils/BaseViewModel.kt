package com.example.exercise.ui.utils

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel<T : Any>(initial: T) : ViewModel() {
    protected val mutableState: MutableStateFlow<T> by lazy {
        MutableStateFlow(initial)
    }

    val state: StateFlow<T> by lazy {
        mutableState.asStateFlow()
    }
}