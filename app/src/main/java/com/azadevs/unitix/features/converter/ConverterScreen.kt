package com.azadevs.unitix.features.converter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.azadevs.unitix.data.model.Category
import com.azadevs.unitix.data.model.UnitItem
import com.azadevs.unitix.data.model.UnitType
import com.azadevs.unitix.features.converter.viewmodel.ConverterViewModel

/**
 * Created by : Azamat Kalmurzaev
 * 26/01/26
 */
@Composable
fun ConvertScreen(
    category: Category,
    onBack: () -> Unit,
    vm: ConverterViewModel = viewModel<ConverterViewModel>()
) {
    val units = remember {
        UnitItem.units.filter { it.category == category }
    }

    LaunchedEffect(category) {
        vm.onFromUnitChange(units.first().type)
        vm.onToUnitChange(units.last().type)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextButton(onClick = onBack) {
            Text("Back")
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = category.title,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = vm.inputText,
            onValueChange = vm::onInputChange,
            label = { Text("Value") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        UnitDropdown(
            label = "From",
            items = units,
            selected = vm.fromUnit,
            onSelect = vm::onFromUnitChange
        )

        Spacer(Modifier.height(12.dp))

        UnitDropdown(
            label = "To",
            items = units,
            selected = vm.toUnit,
            onSelect = vm::onToUnitChange
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Result",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = vm.resultText,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UnitDropdown(
    label: String,
    items: List<UnitItem>,
    selected: UnitType,
    onSelect: (UnitType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val selectedItem = items.first { it.type == selected }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedItem.label,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(type = MenuAnchorType.PrimaryEditable)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item.label) },
                    onClick = {
                        onSelect(item.type)
                        expanded = false
                    }
                )
            }
        }
    }
}
