package com.eltescode.modules.features.modules.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.eltescode.modules.core.navigation.Routes
import com.eltescode.modules.core.ui.theme.ModulesTheme
import com.eltescode.modules.features.modules.presentation.all_modules_preview.AllModulesPreviewScreen
import com.eltescode.modules.features.modules.presentation.main_screen.MainScreen
import com.eltescode.modules.features.modules.presentation.module_screen.ModuleScreen
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

@AndroidEntryPoint
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val snackBarHostState = remember { SnackbarHostState() }
            val navController = rememberNavController()

            ModulesTheme {

                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
                    modifier = Modifier.fillMaxSize(),
                    content = {

                        NavHost(
                            navController = navController,
                            startDestination = Routes.MAIN_SCREEN
                        ) {

                            composable(Routes.MAIN_SCREEN) {
                                MainScreen(
                                    snackBarHostState = snackBarHostState,
                                    onNextScreen = { navController.navigate(it) }
                                )
                            }

                            composable(Routes.ALL_MODULES_PREVIEW_SCREEN) {
                                AllModulesPreviewScreen(onBack = {
                                    navController.popBackStack()
                                })
                            }

                            composable(
                                Routes.MODULE_SCREEN + "/{id}",
                                arguments = listOf(
                                    navArgument("id") { type = NavType.StringType }
                                )
                            ) {
                                val id = UUID.fromString(it.arguments?.getString("id"))
                                ModuleScreen(
                                    snackBarHostState = snackBarHostState,
                                    passedId = id,
                                    onBackPress = { navController.popBackStack() }
                                )
                            }
                        }
                    })
            }
        }
    }
}

