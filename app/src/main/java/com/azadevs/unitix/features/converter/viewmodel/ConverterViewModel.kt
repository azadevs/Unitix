package com.azadevs.unitix.features.converter.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.azadevs.unitix.data.engine.ConverterEngine
import com.azadevs.unitix.data.model.UnitType
import java.text.DecimalFormat
import kotlin.math.abs

/**
 * Created by : Azamat Kalmurzaev
 * 26/01/26
 */
class ConverterViewModel : ViewModel() {

    private val engine = ConverterEngine()

    var inputText by mutableStateOf("")
        private set

    var fromUnit by mutableStateOf(UnitType.METER)
        private set

    var toUnit by mutableStateOf(UnitType.KILOMETER)
        private set

    var resultText by mutableStateOf("0")
        private set

    fun onInputChange(text: String) {
        inputText = text
        recalc()
    }

    fun onFromUnitChange(unit: UnitType) {
        fromUnit = unit
        recalc()
    }

    fun onToUnitChange(unit: UnitType) {
        toUnit = unit
        recalc()
    }

    private fun recalc() {
        val value = inputText.toDoubleOrNull()
        if (value == null) {
            resultText = "0"
            return
        }

        val result = engine.convert(value, fromUnit, toUnit)
        resultText = format(result)
    }

    private fun format(v: Double): String {
        val absVal = abs(v)

        return when (absVal) {
            0.0 -> "0"
            !in 0.0001..<1.0E9 -> {
                DecimalFormat("0.####E0").format(v)
            }

            else -> {
                val df = DecimalFormat("#,##0.####")
                df.format(v)
            }
        }
    }

    fun swapUnits() {
        val temp = fromUnit
        fromUnit = toUnit
        toUnit = temp
        recalc()
    }

    fun clearInput() {
        inputText = ""
        resultText = "0"
    }
}
