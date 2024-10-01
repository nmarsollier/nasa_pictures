package com.nmarsollier.nasa

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.nmarsollier.nasa.common.navigation.AppNavActions
import com.nmarsollier.fitfat.ui.common.navigation.AppNavigationHost
import org.koin.compose.getKoin
import org.koin.dsl.module

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        supportActionBar?.hide()

        setContent { AppContent() }
    }
}

@Composable
fun AppContent() {
    val koin = getKoin()
    val navController = rememberNavController()

    remember(navController) {
        AppNavActions(navController).also { mavActions ->
            koin.loadModules(
                listOf(
                    module {
                        single { mavActions }
                    }
                ),
                allowOverride = true
            )
        }
    }

    AppNavigationHost()
}
