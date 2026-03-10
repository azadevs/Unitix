package com.azadevs.unitix.features.home.component

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.ChangeHistory
import androidx.compose.material.icons.rounded.CropSquare
import androidx.compose.material.icons.rounded.FlashOn
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Memory
import androidx.compose.material.icons.rounded.MonitorWeight
import androidx.compose.material.icons.rounded.Opacity
import androidx.compose.material.icons.rounded.Science
import androidx.compose.material.icons.rounded.Speed
import androidx.compose.material.icons.rounded.Straighten
import androidx.compose.material.icons.rounded.Thermostat
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
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
    val hapticFeedback = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.93f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "bounce"
    )

    LaunchedEffect(isPressed) {
        if (isPressed) {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }
    }

    val icon = when (category) {
        Category.LENGTH -> Icons.Rounded.Straighten
        Category.WEIGHT -> Icons.Rounded.MonitorWeight
        Category.TEMPERATURE -> Icons.Rounded.Thermostat
        Category.SPEED -> Icons.Rounded.Speed
        Category.AREA -> Icons.Rounded.CropSquare
        Category.VOLUME -> Icons.Rounded.Opacity
        Category.DATA -> Icons.Rounded.Memory
        Category.TIME -> Icons.Rounded.Timer
        Category.POWER -> Icons.Rounded.FlashOn
        Category.PRESSURE -> Icons.Rounded.Science
        Category.ENERGY -> Icons.Rounded.LocalFireDepartment
        Category.ANGLE -> Icons.Rounded.ChangeHistory
        Category.CURRENCY -> Icons.Rounded.AttachMoney
    }

    val gradientColors = when (category) {
        Category.LENGTH -> listOf(Color(0xFF3B82F6), Color(0xFF60A5FA))
        Category.WEIGHT -> listOf(Color(0xFF8B5CF6), Color(0xFFA78BFA))
        Category.TEMPERATURE -> listOf(Color(0xFFF59E0B), Color(0xFFFBBF24))
        Category.SPEED -> listOf(Color(0xFF10B981), Color(0xFF34D399))
        Category.AREA -> listOf(Color(0xFFEC4899), Color(0xFFF472B6))
        Category.VOLUME -> listOf(Color(0xFF06B6D4), Color(0xFF22D3EE))
        Category.DATA -> listOf(Color(0xFF6366F1), Color(0xFF818CF8))
        Category.TIME -> listOf(Color(0xFFF43F5E), Color(0xFFFB7185))
        Category.POWER -> listOf(Color(0xFFEAB308), Color(0xFFFDE047))
        Category.PRESSURE -> listOf(Color(0xFF14B8A6), Color(0xFF2DD4BF))
        Category.ENERGY -> listOf(Color(0xFFF97316), Color(0xFFFB923C))
        Category.ANGLE -> listOf(Color(0xFF8B5CF6), Color(0xFFA78BFA))
        Category.CURRENCY -> listOf(Color(0xFF22C55E), Color(0xFF4ADE80))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(24.dp))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.15f),
                shape = RoundedCornerShape(24.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null, // Disable default ripple for custom bounce
                onClick = onClick
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(gradientColors)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = category.title,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                text = category.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}