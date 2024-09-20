package com.example.exercise.common.navigation

import androidx.navigation.NavHostController
import com.example.exercise.common.utils.toJson
import com.example.exercise.models.api.images.ImageValue
import com.example.exercise.models.extendedDate.ExtendedDateValue

class AppNavActions(
    val navController: NavHostController
) {
    fun goUp() {
        navController.navigateUp()
    }

    fun goImagesList(date: ExtendedDateValue) {
        navController.navigate("${AppGraph.ImagesScreen.name}/${date.toJson()}") {
            launchSingleTop = true
        }
    }

    fun goImagePreview(image: ImageValue) {
        navController.navigate("${AppGraph.ImagePreview.name}/${image.toJson()}") {
            launchSingleTop = true
        }
    }

    fun goImageAnimation(date: ExtendedDateValue) {
        navController.navigate("${AppGraph.ImageAnimation.name}/${date.toJson()}") {
            launchSingleTop = true
        }
    }
}
