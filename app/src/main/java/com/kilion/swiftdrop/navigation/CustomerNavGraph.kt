package com.kilion.swiftdrop.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.kilion.swiftdrop.dashboard.customer.ChangePasswordScreen
import com.kilion.swiftdrop.dashboard.customer.ChatScreen
import com.kilion.swiftdrop.dashboard.customer.CheckoutScreen
import com.kilion.swiftdrop.dashboard.customer.CustomerHomeScreen
import com.kilion.swiftdrop.dashboard.customer.CustomerProfileScreen
import com.kilion.swiftdrop.dashboard.customer.CustomerViewModel
import com.kilion.swiftdrop.dashboard.customer.ItemDetailScreen
import com.kilion.swiftdrop.dashboard.customer.LeaveReviewScreen
import com.kilion.swiftdrop.dashboard.customer.ManageAccountScreen
import com.kilion.swiftdrop.dashboard.customer.ManageAddressScreen
import com.kilion.swiftdrop.dashboard.customer.OrderHistoryScreen
import com.kilion.swiftdrop.dashboard.customer.SettingsScreen

fun NavGraphBuilder.customerNavGraph(navController: NavController) {
    navigation(startDestination = "customerHome", route = "customer_graph") { 
        composable("customerHome") { 
            val viewModel: CustomerViewModel = hiltViewModel()
            CustomerHomeScreen(navController = navController, viewModel = viewModel)
        }
        composable("checkout") { 
            CheckoutScreen(navController = navController)
        }
        composable("order_history") { 
            val viewModel: CustomerViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()
            OrderHistoryScreen(uiState = uiState, onGetOrders = { viewModel.loadMyOrders() })
        }
        composable("customer_profile") { 
            CustomerProfileScreen(navController = navController)
        }
        composable("settings") {
            SettingsScreen(navController = navController)
        }
        composable("manage_addresses") {
            ManageAddressScreen()
        }
        composable("manage_account") {
            ManageAccountScreen(navController = navController)
        }
        composable("change_password") {
            ChangePasswordScreen(navController = navController)
        }
        composable(
            route = "leave_review/{orderId}",
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId")
            LeaveReviewScreen(navController = navController, orderId = orderId)
        }
        composable(
            route = "item_detail/{itemId}",
            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId")
            ItemDetailScreen(itemId = itemId)
        }
        composable(
            route = "chat/{conversationId}",
            arguments = listOf(navArgument("conversationId") { type = NavType.StringType })
        ) { backStackEntry ->
            val conversationId = backStackEntry.arguments?.getString("conversationId")
            if (conversationId != null) {
                ChatScreen(conversationId = conversationId)
            }
        }
    }
}