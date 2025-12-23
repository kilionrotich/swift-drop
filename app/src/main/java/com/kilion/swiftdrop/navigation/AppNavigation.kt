package com.kilion.swiftdrop.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kilion.swiftdrop.auth.login.ForgotPasswordScreen
import com.kilion.swiftdrop.auth.login.LoginScreen
import com.kilion.swiftdrop.auth.login.OtpVerificationScreen
import com.kilion.swiftdrop.auth.login.ResetPasswordScreen
import com.kilion.swiftdrop.auth.login.register.RegisterScreen
import com.kilion.swiftdrop.dashboard.rider.riderNavGraph
import com.kilion.swiftdrop.dashboard.shop.shopNavGraph

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController)
        }
        composable("register") {
            RegisterScreen(navController)
        }
        composable("forgot_password") {
            ForgotPasswordScreen(navController)
        }
        composable("otp_verification") {
            OtpVerificationScreen(navController)
        }
        composable("reset_password") {
            ResetPasswordScreen(navController)
        }

        // Graphs
        customerNavGraph(navController)
        riderNavGraph(navController)
        shopNavGraph(navController)
    }
}