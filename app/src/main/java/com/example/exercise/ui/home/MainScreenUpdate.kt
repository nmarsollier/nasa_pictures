package com.example.exercise.ui.home

import com.example.exercise.common.utils.StateEventObject

enum class MainScreenState {
    Update
}

class MainScreenUpdate : StateEventObject<MainScreenState>() {
    fun updateScreen() {
        MainScreenState.Update.sendToEvent()
    }
}