package com.absut.nutrivision.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.absut.nutrivision.data.OnboardingPrefs
import com.absut.nutrivision.model.NutritionRecord
import com.absut.nutrivision.ui.screens.DetailScreen
import com.absut.nutrivision.ui.screens.HomeScreen
import com.absut.nutrivision.ui.screens.OnBoardScreen
import com.absut.nutrivision.ui.screens.PreviewScreen
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
object HomeRoute
@Serializable
object OnboardingRoute
@Serializable
object PreviewRoute
@Serializable
object DetailRoute

@Composable
fun AppNavHost(
    viewModel: MainViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val onboardingPref = OnboardingPrefs.isOnboardingDoneFlow(context).collectAsState(initial = null).value
    val onboardingDone = onboardingPref ?: false

    val startDest = if (onboardingDone) HomeRoute else OnboardingRoute
    val viewState by viewModel.viewState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = startDest
    ) {
        composable<OnboardingRoute> {
            OnBoardScreen(
                onContinue = {
                    scope.launch {
                        OnboardingPrefs.setOnboardingDone(context, true)
                        navController.navigate(HomeRoute) {
                            popUpTo(OnboardingRoute) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable<HomeRoute> {
            HomeScreen(
                viewModel = viewModel,
                onImageCaptured = { bitmap, path ->
                    viewModel.onImageCaptured(bitmap,path)
                    navController.navigate(PreviewRoute)
                },
                onRecordClick = { record ->
                    //store the selected record in the ViewModel or SavedStateHandle
                    // before navigating.
                    viewModel.setSelectedRecord(record)
                    navController.navigate(DetailRoute)
                }
            )
        }

        composable<PreviewRoute> {
            LaunchedEffect(viewState.nutritionResult) {
                viewState.nutritionResult?.let {
                    navController.navigate(DetailRoute) {
                        popUpTo(PreviewRoute) { inclusive = true }
                    }
                }
            }

            PreviewScreen(
                viewState = viewState,
                onGetNutrition = { bitmap ->
                    viewModel.generateNutrition(bitmap)
                },
                onClose = {
                    viewModel.clearNutritionResult()
                    navController.popBackStack()
                },
                onChangePhoto = {},
                onRemovePhoto = {}
            )
        }

        composable<DetailRoute> {
            DisposableEffect(Unit) {
                onDispose {
                    viewModel.clearNutritionResult()
                    viewModel.setSelectedRecord(null)
                }
            }

            DetailScreen(
                nutritionResult = viewState.nutritionResult,
                record = viewModel.getSelectedRecord(),
                onNavigateBack = {
                    navController.popBackStack()
                },
                onDelete = { record ->
                    viewModel.deleteRecord(record)
                    navController.popBackStack()
                }
            )
        }
    }
}