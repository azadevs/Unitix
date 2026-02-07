package com.azadevs.unitix.features.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.azadevs.unitix.data.model.Category

/**
 * Created by : Azamat Kalmurzaev
 * 26/01/26
 */
@Composable
fun CategoryCard(
    category: Category,
    onClick: () -> Unit
) {
    val icon = when (category) {
        Category.LENGTH -> "📏"
        Category.WEIGHT -> "⚖️"
        Category.TEMPERATURE -> "🌡️"
        Category.SPEED -> "🚀"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = icon,
                style = MaterialTheme.typography.headlineLarge
            )

            Text(
                text = category.title,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}