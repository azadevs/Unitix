package com.azadevs.unitix.features.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Update
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.azadevs.unitix.R

@Composable
fun SettingsScreen() {
    val isDark = isSystemInDarkTheme()
    val glowColor = remember(isDark) {
        if (isDark) Color(0xFF6366F1).copy(alpha = 0.12f)
        else Color(0xFF818CF8).copy(alpha = 0.15f)
    }

    val context = LocalContext.current
    val versionName = remember {
        try {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "1.0"
        } catch (e: Exception) {
            "1.0"
        }
    }

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Subtle glow at top
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(glowColor, Color.Transparent),
                            center = Offset(200f, 0f),
                            radius = 800f
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 24.dp)
                ) {
                    Text(
                        text = stringResource(R.string.nav_settings),
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.settings_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(Modifier.height(8.dp))

                // Appearance section
                SettingsSectionLabel(stringResource(R.string.settings_section_appearance))

                SettingsCard(
                    icon = Icons.Rounded.Palette,
                    iconBackground = Brush.linearGradient(
                        listOf(Color(0xFF6366F1), Color(0xFF818CF8))
                    ),
                    title = stringResource(R.string.settings_theme),
                    subtitle = stringResource(
                        if (isDark) R.string.settings_theme_dark else R.string.settings_theme_light
                    )
                )

                Spacer(Modifier.height(8.dp))

                // About section
                SettingsSectionLabel(stringResource(R.string.settings_section_about))

                SettingsCard(
                    icon = Icons.Rounded.Info,
                    iconBackground = Brush.linearGradient(
                        listOf(Color(0xFF10B981), Color(0xFF34D399))
                    ),
                    title = stringResource(R.string.settings_version),
                    subtitle = "v$versionName"
                )

                Spacer(Modifier.height(2.dp))

                SettingsCard(
                    icon = Icons.Rounded.Code,
                    iconBackground = Brush.linearGradient(
                        listOf(Color(0xFF0EA5E9), Color(0xFF38BDF8))
                    ),
                    title = stringResource(R.string.settings_developer),
                    subtitle = "Azamat Kalmurzaev"
                )

                Spacer(Modifier.height(2.dp))

                SettingsCard(
                    icon = Icons.Rounded.Star,
                    iconBackground = Brush.linearGradient(
                        listOf(Color(0xFFF59E0B), Color(0xFFFBBF24))
                    ),
                    title = stringResource(R.string.settings_rate),
                    subtitle = stringResource(R.string.settings_rate_sub)
                )

                Spacer(Modifier.height(2.dp))

                SettingsCard(
                    icon = Icons.Rounded.Update,
                    iconBackground = Brush.linearGradient(
                        listOf(Color(0xFFEC4899), Color(0xFFF472B6))
                    ),
                    title = stringResource(R.string.settings_currency_update),
                    subtitle = stringResource(R.string.settings_currency_update_sub)
                )

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun SettingsSectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
    )
}

@Composable
private fun SettingsCard(
    icon: ImageVector,
    iconBackground: Brush,
    title: String,
    subtitle: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 3.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f),
                shape = RoundedCornerShape(20.dp)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(iconBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
