package com.kilion.swiftdrop.dashboard.rider

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

fun NavGraphBuilder.riderNavGraph(navController: NavController) {
    navigation(startDestination = "riderHome", route = "rider_graph") {
        composable("riderHome") {
            RiderHomeScreen(navController = navController)
        }
        composable("rider_profile") {
            RiderProfileScreen(navController = navController)
        }
        composable("rider_delivery_history") {
            RiderDeliveryHistoryScreen(navController)
        }
        composable("rider_earnings") {
            RiderEarningsScreen(navController)
        }
    }
}