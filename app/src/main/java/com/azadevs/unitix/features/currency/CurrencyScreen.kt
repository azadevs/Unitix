package com.azadevs.unitix.features.currency

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.azadevs.unitix.features.currency.component.CurrencyRateItem
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
                val filteredRates = remember(uiState.searchQuery, uiState.rates) {
                    uiState.rates.filter {
                        it.code.contains(uiState.searchQuery, ignoreCase = true)
                    }
                }
                val baseRate = remember(uiState.baseCurrency, uiState.rates) {
                    uiState.rates.find { it.code == uiState.baseCurrency }?.rate ?: 1.0
                }
                val formatter = remember { DecimalFormat("#,##0.0000") }

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(
                        items = filteredRates,
                        key = { it.code }
                    ) { model ->
                        val convertedRate = remember(baseRate, model.rate) {
                            if (baseRate > 0) (1.0 / baseRate) * model.rate else 0.0
                        }

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

