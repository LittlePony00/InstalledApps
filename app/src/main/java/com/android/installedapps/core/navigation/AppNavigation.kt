package com.android.installedapps.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.android.installedapps.feature.app_detail.presentation.AppDetailScreen
import com.android.installedapps.feature.apps_list.presentation.AppsListScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.APPS_LIST
    ) {
        composable(NavRoutes.APPS_LIST) {
            AppsListScreen(
                onNavigateToDetail = { packageName ->
                    navController.navigate(NavRoutes.appDetail(packageName))
                }
            )
        }

        composable(
            route = NavRoutes.APP_DETAIL,
            arguments = listOf(
                navArgument("packageName") { type = NavType.StringType }
            )
        ) {
            AppDetailScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
