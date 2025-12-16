package com.bolashak.wildberries.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bolashak.wildberries.presentation.cart.CartScreen
import com.bolashak.wildberries.presentation.catalog.CatalogScreen
import com.bolashak.wildberries.presentation.detail.ProductDetailScreen
import com.bolashak.wildberries.presentation.favorites.FavoritesScreen
import com.bolashak.wildberries.presentation.home.HomeScreen
import com.bolashak.wildberries.presentation.product_list.ProductListScreen
import com.bolashak.wildberries.presentation.profile.ProfileScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.Catalog.route) {
            CatalogScreen(navController)
        }
        composable(Screen.Cart.route) {
            CartScreen(navController)
        }
        composable(Screen.Favorites.route) {
            FavoritesScreen(navController)
        }
        composable(
            route = "profile?tab={tab}",
            arguments = listOf(navArgument("tab") { 
                defaultValue = "Main" 
                nullable = true
            })
        ) { backStackEntry ->
            val tab = backStackEntry.arguments?.getString("tab")
            ProfileScreen(initialTab = tab)
        }
        
        composable(
            route = Screen.ProductList.route,
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) {
            ProductListScreen(navController)
        }
        
        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) {
            ProductDetailScreen(navController)
        }
    }
}
