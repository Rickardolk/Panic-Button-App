package com.example.panicbutton.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.panicbutton.model.Preference
import com.example.panicbutton.model.Preference.getUserRole
import com.example.panicbutton.model.Preference.isUserLoggedIn
import com.example.panicbutton.view.components.OnBoarding
import com.example.panicbutton.view.screens.DashboardAdminScreen
import com.example.panicbutton.view.screens.DashboardUserScreen
import com.example.panicbutton.view.screens.DataRekapScreen
import com.example.panicbutton.view.screens.DetailRekapScreen
import com.example.panicbutton.view.screens.HelpScreen
import com.example.panicbutton.view.screens.LoginScreen
import com.example.panicbutton.view.screens.RegisterScreen
import com.example.panicbutton.view.screens.ResetPasswordScreen
import com.example.panicbutton.view.screens.UserProfileScreen

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val isOnboardingShow = Preference.isOnboardingShow(context)

    val startDestination = remember{
        if (!isOnboardingShow) {
            "onboarding"
        } else if (isUserLoggedIn(context)) {
            when (getUserRole(context)) {
                "admin" -> "dashboard_admin"
                else -> "dashboard_user"
            }
        } else {
            "login"
        }
    }

    NavHost(navController = navController, startDestination = startDestination) {

        composable("onboarding") {
            OnBoarding(
                navController = navController,
                context = context
            )
        }

        composable("login") {
            LoginScreen(
                context = context,
                navController = navController
            )
        }

        composable("register") {
            RegisterScreen(
                navController = navController,
                context = context
            )
        }

        composable("dashboard_user") {
            DashboardUserScreen(
                context = context,
                navController = navController
            )
        }

        composable("dashboard_admin"){
            DashboardAdminScreen(
                navController = navController,
                context = context
            )
        }

        composable("reset_password") {
            ResetPasswordScreen(
                navController = navController,
                context = context
            )
        }

        composable("detail_rekap/{houseNumber}") { backStackEntry ->
            val nomorRumah = backStackEntry.arguments?.getString("houseNumber")
            DetailRekapScreen(
                navController = navController,
                houseNumber = nomorRumah ?: ""
            )
        }

        composable("data_rekap") {
            DataRekapScreen(
                navController = navController
            )
        }

        composable("user_profile") {
            UserProfileScreen(
                context = context,
                navController = navController
            )
        }

        composable(
            "help",
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(500))},
            exitTransition = {slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(500))}
            ) {
                HelpScreen(
                    navController = navController
            )
        }
    }
}