package com.bolashak.wildberries.presentation.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bolashak.wildberries.domain.manager.PurchasedItem
import com.bolashak.wildberries.presentation.currency.CurrencyScreen
import com.bolashak.wildberries.presentation.theme.WBPurple
import com.bolashak.wildberries.presentation.theme.WBPurpleDark
import com.bolashak.wildberries.presentation.theme.WBRed
import kotlinx.coroutines.delay

@Composable
fun ProfileScreen(
    initialTab: String? = null,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    var currentScreen by remember(initialTab) { mutableStateOf(initialTab ?: "Main") }
    val purchases by viewModel.purchases.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F9))
    ) {
        when (currentScreen) {
            "Main" -> ProfileMain(
                onNavigate = { currentScreen = it },
                purchaseCount = purchases.size
            )
            "Deliveries" -> DeliveriesScreen(onBack = { currentScreen = "Main" })
            "Purchases" -> PurchasesScreen(
                onBack = { currentScreen = "Main" },
                purchases = purchases
            )
            "Personal" -> PersonalDataScreen(onBack = { currentScreen = "Main" })
            "Currency" -> CurrencyScreen(onBack = { currentScreen = "Main" })
        }
    }
}

@Composable
fun ProfileMain(onNavigate: (String) -> Unit, purchaseCount: Int = 0) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(WBPurple, WBPurpleDark)
                    )
                )
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = WBPurple,
                        modifier = Modifier.size(48.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Welcome!",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Wildberries Shopper",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            ProfileMenuItem(
                title = "My Deliveries",
                icon = Icons.Default.ShoppingBag,
                badgeCount = 2,
                onClick = { onNavigate("Deliveries") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            ProfileMenuItem(
                title = "Purchases",
                icon = Icons.Default.CheckCircle,
                badgeCount = purchaseCount,
                onClick = { onNavigate("Purchases") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            ProfileMenuItem(
                title = "Personal Data",
                icon = Icons.Default.Person,
                onClick = { onNavigate("Personal") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            ProfileMenuItem(
                title = "Currency",
                icon = Icons.Default.CurrencyExchange,
                onClick = { onNavigate("Currency") }
            )
        }
    }
}

@Composable
fun ProfileMenuItem(title: String, icon: ImageVector, badgeCount: Int = 0, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = WBPurple)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = title, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
            if (badgeCount > 0) {
                Box(
                    modifier = Modifier
                        .background(WBRed, CircleShape)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(text = badgeCount.toString(), color = Color.White, style = MaterialTheme.typography.labelSmall)
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
        }
    }
}

@Composable
fun SubScreenHeader(title: String, onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }
        Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun DeliveriesScreen(onBack: () -> Unit) {
    Column {
        SubScreenHeader("My Deliveries", onBack)
        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(2) {
                DeliveryItem(status = "On the way", date = "Dec 20")
            }
            items(1) {
                DeliveryItem(status = "Ready for pickup", date = "Dec 16", isReady = true)
            }
        }
    }
}

@Composable
fun DeliveryItem(status: String, date: String, isReady: Boolean = false) {
    Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(12.dp)) {
        Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
            Text(text = "Order #849302", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Status: $status", color = if (isReady) WBPurple else Color.Gray)
            Text(text = "Estimated: $date", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun PurchasesScreen(onBack: () -> Unit, purchases: List<PurchasedItem>) {
    Column {
        SubScreenHeader("Purchases", onBack)
        
        if (purchases.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.ShoppingBag,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No purchases yet",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray
                    )
                    Text(
                        text = "Items you buy will appear here",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(purchases) { index, item ->
                    var visible by remember { mutableStateOf(false) }
                    LaunchedEffect(Unit) {
                        delay(index * 50L)
                        visible = true
                    }
                    
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn() + slideInVertically { it / 2 }
                    ) {
                        PurchaseItemCard(item)
                    }
                }
            }
        }
    }
}

@Composable
fun PurchaseItemCard(item: PurchasedItem) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.product.imageUrl.ifEmpty { "https://via.placeholder.com/100" })
                    .crossfade(true)
                    .build(),
                contentDescription = item.product.name,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.product.brand,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray
                )
                Text(
                    text = item.product.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Delivered ${item.purchaseDate}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF4CAF50)
                    )
                }
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${item.product.price} ₽",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = WBPurple
                )
                Text(
                    text = "x${item.quantity}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun PersonalDataScreen(onBack: () -> Unit) {
    Column {
        SubScreenHeader("Personal Data", onBack)
        Column(modifier = Modifier.padding(16.dp)) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Name: Bolashak Kulmukhambetov", fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Phone: +77054498490")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Email: bolashak040206@mail.ru")
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = WBPurple),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Edit Profile")
            }
        }
    }
}
