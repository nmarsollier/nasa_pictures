package com.nmarsollier.fitfat.ui.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.exercise.common.navigation.AppGraph
import com.example.exercise.common.navigation.AppNavActions
import com.example.exercise.common.utils.jsonToObject
import com.example.exercise.models.api.images.ImageValue
import com.example.exercise.models.extendedDate.ExtendedDateValue
import com.example.exercise.ui.imageAnimation.ImageAnimationScreen
import com.example.exercise.ui.home.HomeScreen
import com.example.exercise.ui.imagePreview.ImagePreviewScreen
import com.example.exercise.ui.imagesList.ImagesListScreen
import org.koin.compose.koinInject

@Composable
fun AppNavigationHost(
    appNavActionProvider: AppNavActions = koinInject(),
) {
    val actions by rememberUpdatedState(appNavActionProvider)

    NavHost(
        navController = actions.navController,
        startDestination = AppGraph.Home.name,
    ) {
        composable(
            route = AppGraph.Home.name,
        ) {
            HomeScreen()
        }

        composable(
            route = "${AppGraph.ImagesScreen.name}/{date}",
            arguments = listOf(navArgument("date") { type = NavType.StringType }),
        ) {
            val measureJson = it.arguments?.getString("date")
            val date = measureJson?.jsonToObject<ExtendedDateValue>()
                ?: throw IllegalArgumentException("Invalid parameter")

            ImagesListScreen(date = date)
        }

        composable(
            route = "${AppGraph.ImageAnimation.name}/{date}",
            arguments = listOf(navArgument("date") { type = NavType.StringType }),
        ) {
            val measureJson = it.arguments?.getString("date")
            val date = measureJson?.jsonToObject<ExtendedDateValue>()
                ?: throw IllegalArgumentException("Invalid parameter")

            ImageAnimationScreen(date = date)
        }

        composable(
            route = "${AppGraph.ImagePreview.name}/{image}",
            arguments = listOf(navArgument("image") { type = NavType.StringType }),
        ) {
            val measureJson = it.arguments?.getString("image")
            val image = measureJson?.jsonToObject<ImageValue>()
                ?: throw IllegalArgumentException("Invalid parameter")

            ImagePreviewScreen(imageValue = image)
        }
    }
}
