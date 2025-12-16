package com.bolashak.wildberries.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.bolashak.wildberries.presentation.navigation.Screen
import com.bolashak.wildberries.presentation.theme.WBPurple


data class BottomNavItem(
    val name: String,
    val route: String,
    val icon: ImageVector
)

@Composable
fun WBBottomNavigation(
    navController: NavController
) {
    val items = listOf(
        BottomNavItem("Main", Screen.Home.route, Icons.Default.Home),
        BottomNavItem("Catalog", Screen.Catalog.route, Icons.Default.Search),
        BottomNavItem("Cart", Screen.Cart.route, Icons.Default.ShoppingCart),
        BottomNavItem("Favorites", Screen.Favorites.route, Icons.Default.Favorite),
        BottomNavItem("Profile", "profile?tab=Main", Icons.Default.AccountCircle),
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color.White,
        contentColor = Color.Black,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(28.dp),
                spotColor = Color.Black.copy(alpha = 0.25f),
                ambientColor = Color.Black.copy(alpha = 0.15f)
            )
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(28.dp),
                spotColor = WBPurple.copy(alpha = 0.1f)
            ),
        tonalElevation = 0.dp
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route || 
                (item.route == "profile?tab=Main" && currentRoute?.startsWith("profile") == true)
            NavigationBarItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.name) },
                label = { Text(text = item.name, style = MaterialTheme.typography.labelSmall) },
                selected = selected,
                onClick = {
                    val targetRoute = item.route
                    if (currentRoute != item.route) {
                        navController.navigate(targetRoute) {
                            popUpTo(Screen.Home.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = WBPurple,
                    selectedTextColor = WBPurple,
                    indicatorColor = WBPurple.copy(alpha = 0.12f),
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray
                )
            )
        }
    }
}
