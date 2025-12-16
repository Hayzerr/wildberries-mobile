package com.bolashak.wildberries.presentation.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Tablet
import androidx.compose.material.icons.filled.Toys
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bolashak.wildberries.domain.model.CategoryType
import com.bolashak.wildberries.presentation.navigation.Screen
import com.bolashak.wildberries.presentation.theme.WBPurple

@Composable
fun CatalogScreen(
    navController: NavController,
    viewModel: CatalogViewModel = hiltViewModel()
) {
    val categories by viewModel.categories.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(WBPurple)
                .padding(16.dp)
        ) {
            Text(
                text = "Catalog",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(categories) { category ->
                CategoryItem(
                    name = category.type.displayName,
                    count = category.count,
                    icon = getIconForCategory(category.type),
                    onClick = {
                        navController.navigate(Screen.ProductList.createRoute(category.type.id))
                    }
                )
            }
        }
    }
}

@Composable
fun CategoryItem(
    name: String,
    count: Int,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF6F6F9))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = WBPurple,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "$count items",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
        }
    }
}

fun getIconForCategory(type: CategoryType): ImageVector {
    return when (type) {
        CategoryType.SMARTPHONES -> Icons.Default.Smartphone
        CategoryType.LAPTOPS -> Icons.Default.Computer
        CategoryType.TABLETS -> Icons.Default.Tablet
        CategoryType.HEADPHONES -> Icons.Default.Headphones
        CategoryType.SMART_WATCHES -> Icons.Default.Watch
        CategoryType.TSHIRTS -> Icons.Default.Checkroom
        CategoryType.JEANS -> Icons.Default.Checkroom
        CategoryType.DRESSES -> Icons.Default.Checkroom
        CategoryType.JACKETS -> Icons.Default.Checkroom
        CategoryType.SNEAKERS -> Icons.Default.FitnessCenter
        CategoryType.BOOTS -> Icons.Default.FitnessCenter
        CategoryType.BACKPACKS -> Icons.Default.ShoppingBag
        CategoryType.WATCHES -> Icons.Default.Watch
        CategoryType.COSMETICS -> Icons.Default.Face
        CategoryType.PERFUMES -> Icons.Default.Face
        CategoryType.BEDDING -> Icons.Default.Home
        CategoryType.KITCHENWARE -> Icons.Default.Kitchen
        CategoryType.SPORTSWEAR -> Icons.Default.FitnessCenter
        CategoryType.TOYS -> Icons.Default.Toys
        CategoryType.BOOKS -> Icons.Default.MenuBook
    }
}
