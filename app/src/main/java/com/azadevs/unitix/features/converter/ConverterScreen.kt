package com.azadevs.unitix.features.converter

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
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
@Composable
fun ConvertScreen(
    category: Category?,
    onBack: () -> Unit,
    viewModel: ConverterViewModel = viewModel()
) {
    val units = remember {
        UnitItem.units.filter { it.category == category }
    }

    val clipboard = LocalClipboardManager.current
    var rotated by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (rotated) 180f else 0f,
        label = "swapRotation"
    )

    LaunchedEffect(category) {
        viewModel.onFromUnitChange(units.first().type)
        viewModel.onToUnitChange(units.last().type)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null
            )
        }

        Text(
            text = category?.title ?: "",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(20.dp))

        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .animateContentSize()
            ) {
                OutlinedTextField(
                    value = viewModel.inputText,
                    onValueChange = viewModel::onInputChange,
                    label = { Text(stringResource(R.string.value)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        if (viewModel.inputText.isNotEmpty()) {
                            IconButton(
                                onClick = viewModel::clearInput
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

                    IconButton(onClick = {
                        rotated = !rotated
                        viewModel.swapUnits()
                    }) {
                        Icon(
                            imageVector = Icons.Default.SwapVert,
                            contentDescription = stringResource(R.string.swap),
                            modifier = Modifier.rotate(rotation)
                        )
                    }

                    Text(stringResource(R.string.to))
                }

                UnitDropdown(
                    label = stringResource(R.string.from),
                    items = units,
                    selected = viewModel.fromUnit,
                    onSelect = viewModel::onFromUnitChange
                )

                Spacer(Modifier.height(12.dp))

                UnitDropdown(
                    label = stringResource(R.string.to),
                    items = units,
                    selected = viewModel.toUnit,
                    onSelect = viewModel::onToUnitChange
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
            modifier = Modifier.fillMaxWidth()
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
                    label = stringResource(R.string.result),
                    transitionSpec = {
                        (fadeIn() + scaleIn(initialScale = 0.98f)).togetherWith(
                            fadeOut() + scaleOut(targetScale = 1.02f)
                        )
                    }
                ) { value ->
                    Text(
                        text = value,
                        style = MaterialTheme.typography.displaySmall
                    )
                }

                IconButton(onClick = {
                    clipboard.setText(AnnotatedString(viewModel.resultText))
                }) {
                    Icon(
                        Icons.Default.ContentCopy,
                        contentDescription = stringResource(R.string.copy)
                    )
                }
            }
        }
    }

}

