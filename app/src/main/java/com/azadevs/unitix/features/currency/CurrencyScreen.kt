package com.azadevs.unitix.features.currency

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import java.text.DecimalFormat
import java.util.Locale

/**
 * Created by : Azamat Kalmurzaev
 * 03/10/26
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyScreen(
    viewModel: CurrencyViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            var expanded by remember { mutableStateOf(false) }
            TopAppBar(
                title = { Text("Currency Rates") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Box {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { expanded = true }
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val countryCode =
                                if (uiState.baseCurrency.length >= 2) uiState.baseCurrency.substring(
                                    0,
                                    2
                                ).lowercase(Locale.getDefault()) else "xx"
                            val flagUrl = "https://flagcdn.com/w80/${countryCode}.png"
                            AsyncImage(
                                model = flagUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .width(28.dp)
                                    .height(20.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .border(
                                        0.5.dp,
                                        Color.LightGray.copy(alpha = 0.5f),
                                        RoundedCornerShape(4.dp)
                                    )
                                    .background(Color.White),
                                contentScale = androidx.compose.ui.layout.ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = uiState.baseCurrency,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = "Select Base Currency"
                            )
                        }

                        if (expanded) {
                            AlertDialog(
                                onDismissRequest = { expanded = false },
                                title = { Text("Select Base Currency") },
                                text = {
                                    LazyColumn(modifier = Modifier
                                        .fillMaxWidth()
                                        .height(300.dp)) {
                                        items(uiState.rates) { rateModel ->
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable {
                                                        viewModel.onBaseCurrencySelected(rateModel.code)
                                                        expanded = false
                                                    }
                                                    .padding(vertical = 12.dp, horizontal = 8.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                val cCode =
                                                    if (rateModel.code.length >= 2) rateModel.code.substring(
                                                        0,
                                                        2
                                                    ).lowercase(Locale.getDefault()) else "xx"
                                                AsyncImage(
                                                    model = "https://flagcdn.com/w80/${cCode}.png",
                                                    contentDescription = null,
                                                    modifier = Modifier
                                                        .width(32.dp)
                                                        .height(22.dp)
                                                        .clip(RoundedCornerShape(4.dp))
                                                        .background(Color.LightGray),
                                                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                                                )
                                                Spacer(modifier = Modifier.width(16.dp))
                                                Text(
                                                    text = rateModel.code,
                                                    style = MaterialTheme.typography.titleMedium
                                                )
                                            }
                                        }
                                    }
                                },
                                confirmButton = {
                                    TextButton(onClick = { expanded = false }) {
                                        Text("Close")
                                    }
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Search Box
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = viewModel::onSearchQueryChanged,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                label = { Text("Search currency") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.error != null) {
                Text(
                    text = uiState.error!!,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }

            Box(modifier = Modifier.fillMaxSize()) {
                val filteredRates = uiState.rates.filter {
                    it.code.contains(uiState.searchQuery, ignoreCase = true)
                }

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filteredRates) { model ->
                        val baseRate =
                            uiState.rates.find { it.code == uiState.baseCurrency }?.rate ?: 1.0

                        // For a general application: rate relative to baseCurrency
                        val convertedRate = if (baseRate > 0) {
                            (1.0 / baseRate) * model.rate
                        } else 0.0

                        val formatter = DecimalFormat("#,##0.0000")

                        CurrencyRateItem(
                            model = model,
                            displayRate = formatter.format(convertedRate),
                            isSelected = model.code == uiState.baseCurrency
                        )
                    }
                }

                if (uiState.isLoading && uiState.rates.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(48.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun CurrencyRateItem(
    model: CurrencyUiModel,
    displayRate: String,
    isSelected: Boolean
) {
    val bgColor =
        if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
    val textColor =
        if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
    val trendColor = if (model.isUp) Color(0xFF10B981) else Color(0xFFEF4444)
    val trendIcon =
        if (model.isUp) Icons.AutoMirrored.Filled.TrendingUp else Icons.AutoMirrored.Filled.TrendingDown

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            val countryCode = if (model.code.length >= 2) model.code.substring(0, 2)
                .lowercase(Locale.getDefault()) else "xx"
            val flagUrl = "https://flagcdn.com/w80/${countryCode}.png"

            AsyncImage(
                model = flagUrl,
                contentDescription = "${model.code} flag",
                modifier = Modifier
                    .width(44.dp)
                    .height(30.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .border(0.5.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                    .background(Color.White),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = model.code,
                    style = MaterialTheme.typography.titleMedium,
                    color = textColor,
                    fontWeight = FontWeight.Bold
                )
                if (model.code != "USD") {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = trendIcon,
                            contentDescription = null,
                            tint = trendColor,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = String.format(Locale.US, "%.2f%%", model.trendPercentage),
                            style = MaterialTheme.typography.labelSmall,
                            color = trendColor,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        Text(
            text = displayRate,
            style = MaterialTheme.typography.titleMedium,
            color = textColor,
            fontWeight = FontWeight.SemiBold
        )
    }
}
