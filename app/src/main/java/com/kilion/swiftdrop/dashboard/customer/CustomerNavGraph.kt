package com.kilion.swiftdrop.dashboard.customer

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

sealed class CustomerRoutes(val route: String) {
    object Home : CustomerRoutes("customer_home")
    object Orders : CustomerRoutes("customer_orders")
}

@Composable
fun CustomerNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = CustomerRoutes.Home.route
    ) {
        composable(CustomerRoutes.Home.route) {
            CustomerHomeScreen(navController = navController)
        }
        composable(CustomerRoutes.Orders.route) {
            CustomerOrdersScreen(navController = navController)
        }
    }
}