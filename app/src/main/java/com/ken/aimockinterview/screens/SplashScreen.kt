package com.ken.aimockinterview.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ken.aimockinterview.MainActivity
import com.ken.aimockinterview.R
import com.ken.aimockinterview.components.OnboardingItem
import com.ken.aimockinterview.navigation.Routes
import com.ken.aimockinterview.utils.OnBoardingUtils
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController, context: MainActivity) {
    val onBoardingUtils by lazy {
        OnBoardingUtils(context)
    }
    LaunchedEffect(key1 = Unit) {
        delay(3000)
        val onboardingItems = listOf(
            OnboardingItem(
                image = R.drawable.img1,
                title = "Welcome to Our App",
                description = "Explore the latest features and enhancements in our app."
            ),
            OnboardingItem(
                image = R.drawable.img2,
                title = "Discover Amazing Features",
                description = "Discover new ways to interact with our app."
            ),
            OnboardingItem(
                image = R.drawable.img3,
                title = "Get Started Today",
                description = "Join us and experience the difference."
            )
        )

        if (onBoardingUtils.userIsLoggedIn()) {
            navController.popBackStack()
            navController.navigate(Routes.Home)
        } else {
            if (onBoardingUtils.isOnBoardingDone()) {
                navController.popBackStack()
                navController.navigate(Routes.Login)
            } else {
                navController.popBackStack()
                navController.navigate(Routes.OnBoarding(onboardingItems))
            }
        }
    }
    SplashScreenUI()
}

@Composable
fun SplashScreenUI(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(R.drawable.splashlogo),
            contentDescription = "ChatBot",
            modifier = modifier.padding(horizontal = 25.dp)
        )

        Spacer(modifier = modifier.height(25.dp))

        Text(
            text = "Welcome to\n AI Mock Interview",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Black,
        )
    }
}