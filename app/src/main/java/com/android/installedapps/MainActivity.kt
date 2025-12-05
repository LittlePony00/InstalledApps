package com.android.installedapps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.android.installedapps.core.navigation.AppNavigation
import com.android.installedapps.ui.theme.InstalledAppsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            InstalledAppsTheme {
                val navController = rememberNavController()
                AppNavigation(navController = navController)
            }
        }
    }
}
