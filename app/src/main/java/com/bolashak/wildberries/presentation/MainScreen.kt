package com.bolashak.wildberries.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.bolashak.wildberries.presentation.components.WBBottomNavigation
import com.bolashak.wildberries.presentation.navigation.AppNavigation

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        containerColor = Color(0xFFF6F6F9),
        bottomBar = { WBBottomNavigation(navController) }
    ) { innerPadding ->
        AppNavigation(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
