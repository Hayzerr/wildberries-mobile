package com.bolashak.wildberries.presentation.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Catalog : Screen("catalog")
    data object Cart : Screen("cart")
    data object Favorites : Screen("favorites")
    data object Profile : Screen("profile")
    
    data object ProductList : Screen("product_list/{category}") {
        fun createRoute(category: String) = "product_list/$category"
    }
    
    data object ProductDetail : Screen("product_detail/{productId}") {
        fun createRoute(productId: String) = "product_detail/$productId"
    }
}
