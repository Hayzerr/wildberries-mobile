package com.bolashak.wildberries.presentation.currency

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bolashak.wildberries.domain.manager.Currency
import com.bolashak.wildberries.presentation.theme.WBPurple
import com.bolashak.wildberries.presentation.theme.WBPurpleDark

@Composable
fun CurrencyScreen(
    onBack: () -> Unit,
    viewModel: CurrencyViewModel = hiltViewModel()
) {
    val selectedCurrency by viewModel.selectedCurrency.collectAsState()
    val exchangeRates by viewModel.exchangeRates.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val currencies = viewModel.getAvailableCurrencies()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F9))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(WBPurple, WBPurpleDark)
                    )
                )
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Icon(
                    Icons.Default.CurrencyExchange,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Currency",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = { viewModel.refreshRates() },
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = Color.White)
                    }
                }
            }
        }

        if (exchangeRates.isEmpty() && !isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color(0xFFFFF3CD), RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = "⚠️ Exchange rates not loaded. Tap refresh to try again.",
                    color = Color(0xFF856404),
                    fontSize = 14.sp
                )
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(currencies) { currency ->
                CurrencyItem(
                    currency = currency,
                    isSelected = currency == selectedCurrency,
                    rate = exchangeRates[currency.code],
                    onClick = { viewModel.selectCurrency(currency) }
                )
            }
        }

        if (exchangeRates.isNotEmpty()) {
            Text(
                text = "Rates are updated from open.er-api.com",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun CurrencyItem(
    currency: Currency,
    isSelected: Boolean,
    rate: Double?,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) WBPurple.copy(alpha = 0.1f) else Color.White,
        label = "bg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) WBPurple else Color.Transparent,
        label = "border"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(2.dp, borderColor, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(if (isSelected) 4.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        if (isSelected) WBPurple else Color(0xFFE0E0E0),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = currency.symbol,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color.White else Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = currency.code,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = currency.displayName,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                if (rate != null && currency != Currency.RUB) {
                    Text(
                        text = "1 ₽ = ${String.format("%.4f", rate)} ${currency.symbol}",
                        style = MaterialTheme.typography.labelSmall,
                        color = WBPurple
                    )
                }
            }

            if (isSelected) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = WBPurple,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
