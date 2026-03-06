package com.azadevs.unitix.features.converter

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.azadevs.unitix.R
import com.azadevs.unitix.data.model.Category
import com.azadevs.unitix.data.model.UnitItem
import com.azadevs.unitix.features.converter.component.UnitDropdown
import com.azadevs.unitix.features.converter.viewmodel.ConverterViewModel

/**
 * Created by : Azamat Kalmurzaev
 * 26/01/26
 */
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("DEPRECATION")
@Composable
fun ConvertScreen(
    category: Category?,
    onBack: () -> Unit,
    viewModel: ConverterViewModel = viewModel()
) {
    val hapticFeedback = LocalHapticFeedback.current

    val units = remember {
        UnitItem.units.filter { it.category == category }
    }

    val clipboard = LocalClipboardManager.current
    var rotated by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (rotated) 180f else 0f,
        label = "swapRotation"
    )

    val swapInteractionSource = remember { MutableInteractionSource() }
    val isSwapPressed by swapInteractionSource.collectIsPressedAsState()
    val swapScale by animateFloatAsState(
        targetValue = if (isSwapPressed) 0.85f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "swapBounce"
    )

    LaunchedEffect(isSwapPressed) {
        if (isSwapPressed) {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }
    }

    LaunchedEffect(category) {
        viewModel.onFromUnitChange(units.first().type)
        viewModel.onToUnitChange(units.last().type)
    }

    val gradientColors = when (category) {
        Category.LENGTH -> listOf(Color(0xFF3B82F6), Color(0xFF60A5FA))
        Category.WEIGHT -> listOf(Color(0xFF8B5CF6), Color(0xFFA78BFA))
        Category.TEMPERATURE -> listOf(Color(0xFFF59E0B), Color(0xFFFBBF24))
        Category.SPEED -> listOf(Color(0xFF10B981), Color(0xFF34D399))
        null -> listOf(Color.Transparent, Color.Transparent)
    }

    val glowColor = gradientColors.firstOrNull()?.copy(alpha = 0.2f) ?: Color.Transparent

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                title = {
                    Text(
                        text = category?.title ?: "",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(glowColor, Color.Transparent),
                            center = Offset(800f, 0f),
                            radius = 1000f
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(Modifier.height(16.dp))
                Card(
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            Color.White.copy(alpha = 0.15f),
                            RoundedCornerShape(20.dp)
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .animateContentSize()
                    ) {
                        OutlinedTextField(
                            value = viewModel.inputText,
                            onValueChange = { newText ->
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                viewModel.onInputChange(newText)
                            },
                            label = { Text(stringResource(R.string.value)) },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = if (category == Category.TEMPERATURE) KeyboardType.Text else KeyboardType.Decimal
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            trailingIcon = {
                                if (viewModel.inputText.isNotEmpty()) {
                                    IconButton(
                                        onClick = {
                                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                            viewModel.clearInput()
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = stringResource(R.string.clear)
                                        )
                                    }
                                }
                            }
                        )

                        Spacer(Modifier.height(12.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Text(stringResource(R.string.from))

                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .scale(swapScale)
                                    .clip(CircleShape)
                                    .background(Brush.linearGradient(gradientColors))
                                    .clickable(
                                        interactionSource = swapInteractionSource,
                                        indication = null
                                    ) {
                                        rotated = !rotated
                                        viewModel.swapUnits()
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SwapVert,
                                    contentDescription = stringResource(R.string.swap),
                                    modifier = Modifier.rotate(rotation),
                                    tint = Color.White
                                )
                            }

                            Text(stringResource(R.string.to))
                        }

                        UnitDropdown(
                            label = stringResource(R.string.from),
                            items = units,
                            selected = viewModel.fromUnit,
                            onSelect = { unit ->
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                viewModel.onFromUnitChange(unit)
                            }
                        )

                        Spacer(Modifier.height(12.dp))

                        UnitDropdown(
                            label = stringResource(R.string.to),
                            items = units,
                            selected = viewModel.toUnit,
                            onSelect = { unit ->
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                viewModel.onToUnitChange(unit)
                            }
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                Text(
                    text = stringResource(R.string.result),
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(8.dp))

                Card(
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            Color.White.copy(alpha = 0.2f),
                            RoundedCornerShape(24.dp)
                        ),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Brush.linearGradient(gradientColors))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            AnimatedContent(
                                targetState = viewModel.resultText,
                                label = "counterAnimation",
                                transitionSpec = {
                                    if (targetState > initialState) {
                                        (slideInVertically { height -> height } + fadeIn()).togetherWith(
                                            slideOutVertically { height -> -height } + fadeOut())
                                    } else {
                                        (slideInVertically { height -> -height } + fadeIn()).togetherWith(
                                            slideOutVertically { height -> height } + fadeOut())
                                    }
                                }
                            ) { value ->
                                Text(
                                    text = value,
                                    style = MaterialTheme.typography.displaySmall,
                                    color = Color.White,
                                    maxLines = 1,
                                    modifier = Modifier.weight(1f, false)
                                )
                            }

                            IconButton(onClick = {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                clipboard.setText(AnnotatedString(viewModel.resultText))
                            }) {
                                Icon(
                                    Icons.Default.ContentCopy,
                                    contentDescription = stringResource(R.string.copy),
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
