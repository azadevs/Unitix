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

            // AREA
            UnitType.SQ_METER -> convertArea(value, 1.0, to)
            UnitType.SQ_KILOMETER -> convertArea(value, 1_000_000.0, to)
            UnitType.HECTARE -> convertArea(value, 10_000.0, to)
            UnitType.ACRE -> convertArea(value, 4046.86, to)
            UnitType.SQ_MILE -> convertArea(value, 2_589_988.11, to)

            // VOLUME
            UnitType.LITER -> convertVolume(value, 1.0, to)
            UnitType.MILLILITER -> convertVolume(value, 0.001, to)
            UnitType.CUBIC_METER -> convertVolume(value, 1000.0, to)
            UnitType.GALLON -> convertVolume(value, 3.78541, to)

            // DATA
            UnitType.BYTE -> convertData(value, 1.0, to)
            UnitType.KILOBYTE -> convertData(value, 1024.0, to)
            UnitType.MEGABYTE -> convertData(value, 1024.0 * 1024.0, to)
            UnitType.GIGABYTE -> convertData(value, 1024.0 * 1024.0 * 1024.0, to)
            UnitType.TERABYTE -> convertData(value, 1024.0 * 1024.0 * 1024.0 * 1024.0, to)

            // TIME
            UnitType.MILLISECOND -> convertTime(value, 0.001, to)
            UnitType.SECOND -> convertTime(value, 1.0, to)
            UnitType.MINUTE -> convertTime(value, 60.0, to)
            UnitType.HOUR -> convertTime(value, 3600.0, to)
            UnitType.DAY -> convertTime(value, 86400.0, to)
            UnitType.WEEK -> convertTime(value, 604800.0, to)

            // POWER
            UnitType.WATT -> convertPower(value, 1.0, to)
            UnitType.KILOWATT -> convertPower(value, 1000.0, to)
            UnitType.HORSEPOWER -> convertPower(value, 745.7, to)

            // PRESSURE
            UnitType.PASCAL -> convertPressure(value, 1.0, to)
            UnitType.BAR -> convertPressure(value, 100000.0, to)
            UnitType.PSI -> convertPressure(value, 6894.76, to)
            UnitType.ATMOSPHERE -> convertPressure(value, 101325.0, to)

            // ENERGY
            UnitType.JOULE -> convertEnergy(value, 1.0, to)
            UnitType.KILOJOULE -> convertEnergy(value, 1000.0, to)
            UnitType.CALORIE -> convertEnergy(value, 4.184, to)
            UnitType.KILOCALORIE -> convertEnergy(value, 4184.0, to)

            // ANGLE
            UnitType.DEGREE -> convertAngle(value, 1.0, to)
            UnitType.RADIAN -> convertAngle(value, 57.2958, to)
            UnitType.GRADIAN -> convertAngle(value, 0.9, to)
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

    private fun convertArea(value: Double, toSqMeter: Double, to: UnitType): Double {
        val sqMeters = value * toSqMeter
        return when (to) {
            UnitType.SQ_METER -> sqMeters
            UnitType.SQ_KILOMETER -> sqMeters / 1_000_000.0
            UnitType.HECTARE -> sqMeters / 10_000.0
            UnitType.ACRE -> sqMeters / 4046.86
            UnitType.SQ_MILE -> sqMeters / 2_589_988.11
            else -> sqMeters
        }
    }

    private fun convertVolume(value: Double, toLiter: Double, to: UnitType): Double {
        val liters = value * toLiter
        return when (to) {
            UnitType.LITER -> liters
            UnitType.MILLILITER -> liters / 0.001
            UnitType.CUBIC_METER -> liters / 1000.0
            UnitType.GALLON -> liters / 3.78541
            else -> liters
        }
    }

    private fun convertData(value: Double, toByte: Double, to: UnitType): Double {
        val bytes = value * toByte
        return when (to) {
            UnitType.BYTE -> bytes
            UnitType.KILOBYTE -> bytes / 1024.0
            UnitType.MEGABYTE -> bytes / (1024.0 * 1024.0)
            UnitType.GIGABYTE -> bytes / (1024.0 * 1024.0 * 1024.0)
            UnitType.TERABYTE -> bytes / (1024.0 * 1024.0 * 1024.0 * 1024.0)
            else -> bytes
        }
    }

    private fun convertTime(value: Double, toSecond: Double, to: UnitType): Double {
        val seconds = value * toSecond
        return when (to) {
            UnitType.MILLISECOND -> seconds / 0.001
            UnitType.SECOND -> seconds
            UnitType.MINUTE -> seconds / 60.0
            UnitType.HOUR -> seconds / 3600.0
            UnitType.DAY -> seconds / 86400.0
            UnitType.WEEK -> seconds / 604800.0
            else -> seconds
        }
    }

    private fun convertPower(value: Double, toWatt: Double, to: UnitType): Double {
        val watts = value * toWatt
        return when (to) {
            UnitType.WATT -> watts
            UnitType.KILOWATT -> watts / 1000.0
            UnitType.HORSEPOWER -> watts / 745.7
            else -> watts
        }
    }

    private fun convertPressure(value: Double, toPascal: Double, to: UnitType): Double {
        val pascals = value * toPascal
        return when (to) {
            UnitType.PASCAL -> pascals
            UnitType.BAR -> pascals / 100000.0
            UnitType.PSI -> pascals / 6894.76
            UnitType.ATMOSPHERE -> pascals / 101325.0
            else -> pascals
        }
    }

    private fun convertEnergy(value: Double, toJoule: Double, to: UnitType): Double {
        val joules = value * toJoule
        return when (to) {
            UnitType.JOULE -> joules
            UnitType.KILOJOULE -> joules / 1000.0
            UnitType.CALORIE -> joules / 4.184
            UnitType.KILOCALORIE -> joules / 4184.0
            else -> joules
        }
    }

    private fun convertAngle(value: Double, toDegree: Double, to: UnitType): Double {
        val degrees = value * toDegree
        return when (to) {
            UnitType.DEGREE -> degrees
            UnitType.RADIAN -> degrees / 57.2958
            UnitType.GRADIAN -> degrees / 0.9
            else -> degrees
        }
    }
}