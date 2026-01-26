package com.azadevs.unitix.data.engine

import com.azadevs.unitix.data.model.UnitType

/**
 * Created by : Azamat Kalmurzaev
 * 26/01/26
 */
class ConverterEngine {

    fun convert(
        value: Double,
        from: UnitType,
        to: UnitType
    ): Double {
        if (from == to) return value

        return when (from) {

            UnitType.METER -> convertLength(value, 1.0, to)
            UnitType.KILOMETER -> convertLength(value, 1000.0, to)
            UnitType.CENTIMETER -> convertLength(value, 0.01, to)
            UnitType.INCH -> convertLength(value, 0.0254, to)
            UnitType.FOOT -> convertLength(value, 0.3048, to)

            UnitType.GRAM -> convertWeight(value, 1.0, to)
            UnitType.KILOGRAM -> convertWeight(value, 1000.0, to)
            UnitType.POUND -> convertWeight(value, 453.592, to)

            UnitType.CELSIUS -> convertTempFromC(value, to)
            UnitType.FAHRENHEIT -> convertTempFromF(value, to)
            UnitType.KELVIN -> convertTempFromK(value, to)

            UnitType.KMH -> convertSpeed(value, 1.0, to)
            UnitType.MS -> convertSpeed(value, 3.6, to)
            UnitType.MPH -> convertSpeed(value, 1.60934, to)
        }
    }

    private fun convertLength(value: Double, toMeter: Double, to: UnitType): Double {
        val meters = value * toMeter
        return when (to) {
            UnitType.METER -> meters
            UnitType.KILOMETER -> meters / 1000.0
            UnitType.CENTIMETER -> meters / 0.01
            UnitType.INCH -> meters / 0.0254
            UnitType.FOOT -> meters / 0.3048
            else -> meters
        }
    }

    private fun convertWeight(value: Double, toGram: Double, to: UnitType): Double {
        val grams = value * toGram
        return when (to) {
            UnitType.GRAM -> grams
            UnitType.KILOGRAM -> grams / 1000.0
            UnitType.POUND -> grams / 453.592
            else -> grams
        }
    }

    private fun convertTempFromC(v: Double, to: UnitType): Double =
        when (to) {
            UnitType.CELSIUS -> v
            UnitType.FAHRENHEIT -> v * 9 / 5 + 32
            UnitType.KELVIN -> v + 273.15
            else -> v
        }

    private fun convertTempFromF(v: Double, to: UnitType): Double =
        when (to) {
            UnitType.CELSIUS -> (v - 32) * 5 / 9
            UnitType.FAHRENHEIT -> v
            UnitType.KELVIN -> (v - 32) * 5 / 9 + 273.15
            else -> v
        }

    private fun convertTempFromK(v: Double, to: UnitType): Double =
        when (to) {
            UnitType.CELSIUS -> v - 273.15
            UnitType.FAHRENHEIT -> (v - 273.15) * 9 / 5 + 32
            UnitType.KELVIN -> v
            else -> v
        }

    private fun convertSpeed(value: Double, toKmh: Double, to: UnitType): Double {
        val kmh = value * toKmh
        return when (to) {
            UnitType.KMH -> kmh
            UnitType.MS -> kmh / 3.6
            UnitType.MPH -> kmh / 1.60934
            else -> kmh
        }
    }
}