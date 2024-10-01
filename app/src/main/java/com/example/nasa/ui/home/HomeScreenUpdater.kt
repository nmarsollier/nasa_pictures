package com.nmarsollier.nasa.ui.home

import com.nmarsollier.nasa.common.utils.StateEventObject

enum class MainScreenState {
    Update
}

class HomeScreenUpdater : StateEventObject<MainScreenState>() {
    fun updateScreen() {
        MainScreenState.Update.sendToEvent()
    }
}