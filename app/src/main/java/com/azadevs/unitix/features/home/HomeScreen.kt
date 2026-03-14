package com.azadevs.unitix.features.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.azadevs.unitix.R
import com.azadevs.unitix.data.model.Category
import com.azadevs.unitix.features.home.component.CategoryCard
import kotlinx.coroutines.launch

/**
 * Created by : Azamat Kalmurzaev
 * 26/01/26
 */
@Composable
fun HomeScreen(
    onCategoryClick: (Category) -> Unit = {},
    onSmartClipboardTrigger: (String, String, String) -> Unit = { _, _, _ -> }
) {
    val categories = Category.entries

    val isDark = androidx.compose.foundation.isSystemInDarkTheme()
    val glowColor = remember(isDark) {
        if (isDark) Color(0xFF10B981).copy(alpha = 0.15f) else Color(0xFF34D399).copy(alpha = 0.2f)
    }

    val primaryColor = MaterialTheme.colorScheme.primary
    val textGradient = remember(primaryColor) {
        Brush.linearGradient(
            colors = listOf(primaryColor, Color(0xFF0EA5E9)),
            start = Offset.Zero,
            end = Offset.Infinite
        )
    }

    val context = androidx.compose.ui.platform.LocalContext.current
    val clipboardManager =
        remember { com.azadevs.unitix.features.home.UnitixClipboardManager(context) }
    var clipboardData by remember {
        mutableStateOf<com.azadevs.unitix.features.home.ClipboardData?>(
            null
        )
    }
    val coroutineScope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                coroutineScope.launch {
                    kotlinx.coroutines.delay(500)
                    val result = clipboardManager.checkClipboardForUnits()
                    if (result != null) {
                        clipboardData = result
                    }
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(glowColor, Color.Transparent),
                            center = Offset(1000f, 0f),
                            radius = 900f
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 24.dp)
                ) {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.displaySmall.merge(
                            TextStyle(
                                brush = textGradient,
                                fontWeight = FontWeight.ExtraBold
                            )
                        )
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = stringResource(R.string.home_desc),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 24.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(categories) { category ->
                        CategoryCard(category = category) {
                            onCategoryClick(category)
                        }
                    }
                }
                val versionName = remember {
                    try {
                        context.packageManager.getPackageInfo(context.packageName, 0).versionName
                            ?: "1.0"
                    } catch (e: Exception) {
                        "1.0"
                    }
                }

                Text(
                    text = stringResource(R.string.app_version, versionName),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    textAlign = TextAlign.Center
                )
            }

            AnimatedVisibility(
                visible = clipboardData != null,
                enter = slideInVertically(initialOffsetY = { -it - 50 }),
                exit = slideOutVertically(targetOffsetY = { -it - 50 }),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = innerPadding.calculateTopPadding() + 16.dp,
                        start = 16.dp,
                        end = 16.dp
                    )
                    .align(Alignment.TopCenter)
            ) {
                clipboardData?.let { data ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f)
                        ),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                stringResource(R.string.clipboard_found_title, data.rawText),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                stringResource(
                                    R.string.clipboard_convert_desc,
                                    data.unit.category.title
                                ),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                            )
                            Spacer(Modifier.height(16.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(onClick = {
                                    coroutineScope.launch {
                                        clipboardManager.markAsShown(data.rawText)
                                        clipboardData = null
                                    }
                                }) {
                                    Text(stringResource(R.string.dismiss))
                                }
                                Spacer(Modifier.width(8.dp))
                                Button(onClick = {
                                    coroutineScope.launch {
                                        clipboardManager.markAsShown(data.rawText)
                                        clipboardData = null
                                        onSmartClipboardTrigger(
                                            data.unit.category.name,
                                            data.value,
                                            data.unit.type.name
                                        )
                                    }
                                }) {
                                    Text(stringResource(R.string.yes_convert))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
