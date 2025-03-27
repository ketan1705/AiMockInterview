package com.ken.aimockinterview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ken.aimockinterview.navigation.NavGraph
import com.ken.aimockinterview.ui.theme.AiMockInterviewTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AiMockInterviewTheme {
                MyApp(context = this)

            }
        }
    }
}

@Composable
fun MyApp(modifier: Modifier = Modifier, context: MainActivity) {
    Box(modifier = modifier.fillMaxSize())
    {
        NavGraph(context)
    }
}