package com.example.exercise.ui.utils

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel<T : Any>(initial: T) : ViewModel() {
    private val mutableState: MutableStateFlow<T> by lazy {
        MutableStateFlow(initial)
    }

    val state: StateFlow<T> by lazy {
        mutableState.asStateFlow()
    }

    fun T.sendToState() {
        mutableState.update { this }
    }
}