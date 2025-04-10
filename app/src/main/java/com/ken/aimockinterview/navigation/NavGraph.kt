package com.ken.aimockinterview.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.ken.aimockinterview.MainActivity
import com.ken.aimockinterview.components.OnboardingItem
import com.ken.aimockinterview.screens.AuthScreen
import com.ken.aimockinterview.screens.FeedbackScreen
import com.ken.aimockinterview.screens.HomeScreen
import com.ken.aimockinterview.screens.InterviewScreen
import com.ken.aimockinterview.screens.OnBoardScreen
import com.ken.aimockinterview.screens.RegistrationScreen
import com.ken.aimockinterview.screens.SplashScreen
import kotlin.reflect.typeOf

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavGraph(
    context: MainActivity,
) {
    val navController = rememberNavController()
    val startDestination = Routes.Splash

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable<Routes.Splash> {
            SplashScreen(navController, context)
        }
        composable<Routes.OnBoarding>(
            typeMap = mapOf(typeOf<List<OnboardingItem>>() to onBoardingType)
        ) {
            val onboardingItem = it.toRoute<Routes.OnBoarding>()
            OnBoardScreen(onBoardingItems = onboardingItem, context = context) {
                navController.popBackStack()
                navController.navigate(Routes.Login)
            }
        }
        composable<Routes.Login>(
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it } // Slide out to the left
                ) + fadeOut(animationSpec = tween(300))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it } // Slide in from the left when returning
                ) + fadeIn(animationSpec = tween(300))
            }
        ) {
            AuthScreen(navController)
//            SecondLoginScreen(navController = navController)
        }
        composable<Routes.Register>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it } // Slide in from the right
                ) + fadeIn(animationSpec = tween(300))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it } // Slide out to the right when popped
                ) + fadeOut(animationSpec = tween(300))
            }
        ) {
            RegistrationScreen(navController)
        }
        composable<Routes.Home>
        {
            HomeScreen(navController = navController, context = context)
        }
        composable<Routes.Interview> {
            val details = it.toRoute<Routes.Interview>()
            InterviewScreen(details, navController)
        }
        composable<Routes.Feedback> {
            val result = it.toRoute<Routes.Feedback>()
            FeedbackScreen(mockId = result.mockId, navController = navController)
        }
    }
}