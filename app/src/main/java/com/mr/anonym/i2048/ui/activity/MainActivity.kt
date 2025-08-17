package com.mr.anonym.i2048.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.mr.anonym.i2048.ui.screens.gameScreen.GameScreen
import com.mr.anonym.i2048.ui.theme.I2048Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            I2048Theme {
                GameScreen(navController)
            }
        }
    }
}