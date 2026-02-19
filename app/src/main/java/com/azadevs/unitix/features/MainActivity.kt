package com.azadevs.unitix.features

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.azadevs.unitix.core.ui.theme.UnitixTheme
import com.azadevs.unitix.features.utils.AppNav

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UnitixTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    AppNav()
                }
            }
        }
    }
}