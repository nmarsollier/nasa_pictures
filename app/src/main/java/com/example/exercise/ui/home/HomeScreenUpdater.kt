package com.example.exercise.ui.home

import com.example.exercise.common.utils.StateEventObject

enum class MainScreenState {
    Update
}

class HomeScreenUpdater : StateEventObject<MainScreenState>() {
    fun updateScreen() {
        MainScreenState.Update.sendToEvent()
    }
}