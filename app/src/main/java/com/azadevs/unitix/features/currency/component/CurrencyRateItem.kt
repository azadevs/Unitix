package com.azadevs.unitix.features.currency.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.azadevs.unitix.features.currency.CurrencyDisplayModel
import java.util.Locale

@Composable
fun CurrencyRateItem(
    model: CurrencyDisplayModel,
    isSelected: Boolean
) {
    val primaryContainer = MaterialTheme.colorScheme.primaryContainer
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant
    val onPrimaryContainer = MaterialTheme.colorScheme.onPrimaryContainer
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant

    val bgColor = remember(isSelected, primaryContainer, surfaceVariant) {
        if (isSelected) primaryContainer else surfaceVariant
    }
    val textColor = remember(isSelected, onPrimaryContainer, onSurfaceVariant) {
        if (isSelected) onPrimaryContainer else onSurfaceVariant
    }
    val trendColor = remember(model.isUp) {
        if (model.isUp) Color(0xFF10B981) else Color(0xFFEF4444)
    }
    val trendIcon = remember(model.isUp) {
        if (model.isUp) Icons.AutoMirrored.Filled.TrendingUp else Icons.AutoMirrored.Filled.TrendingDown
    }
    val countryCode = remember(model.code) {
        if (model.code.length >= 2) model.code.substring(0, 2)
            .lowercase(Locale.getDefault()) else "xx"
    }
    val flagUrl = remember(countryCode) { "https://flagcdn.com/w80/${countryCode}.png" }
    val trendText = remember(model.trendPercentage) {
        String.format(Locale.US, "%.2f%%", model.trendPercentage)
    }

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
                            text = trendText,
                            style = MaterialTheme.typography.labelSmall,
                            color = trendColor,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        Text(
            text = model.formattedRate,
            style = MaterialTheme.typography.titleMedium,
            color = textColor,
            fontWeight = FontWeight.SemiBold
        )
    }
}