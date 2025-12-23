package com.kilion.swiftdrop.dashboard.shop

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

fun NavGraphBuilder.shopNavGraph(navController: NavController) {
    navigation(startDestination = "shopHome", route = "shop_graph") {
        composable("shopHome") {
            ShopHomeScreen(navController = navController)
        }
        composable("shop_orders") {
            val viewModel: ShopViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()
            ShopOrdersScreen(uiState = uiState, onAcceptOrder = {}, onGetOrders = {})
        }
        composable("shop_order_history") {
            // ShopOrderHistoryScreen(navController = navController)
        }
        composable("shop_analytics") {
            ShopAnalyticsScreen()
        }
        composable("shop_profile") {
            ShopProfileScreen(navController = navController)
        }
        composable("add_item") {
            val viewModel: ShopViewModel = hiltViewModel()
            ShopAddItemsScreen(navController = navController, viewModel = viewModel)
        }
    }
}