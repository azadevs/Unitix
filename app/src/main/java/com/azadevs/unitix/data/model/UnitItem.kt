package com.azadevs.unitix.data.model

/**
 * Created by : Azamat Kalmurzaev
 * 26/01/26
 */
data class UnitItem(
    val type: UnitType,
    val label: String,
    val category: Category
) {
    companion object {
        val units = listOf(
            UnitItem(UnitType.METER, "Meter (m)", Category.LENGTH),
            UnitItem(UnitType.KILOMETER, "Kilometer (km)", Category.LENGTH),
            UnitItem(UnitType.CENTIMETER, "Centimeter (cm)", Category.LENGTH),
            UnitItem(UnitType.INCH, "Inch (in)", Category.LENGTH),
            UnitItem(UnitType.FOOT, "Foot (ft)", Category.LENGTH),

            UnitItem(UnitType.GRAM, "Gram (g)", Category.WEIGHT),
            UnitItem(UnitType.KILOGRAM, "Kilogram (kg)", Category.WEIGHT),
            UnitItem(UnitType.POUND, "Pound (lb)", Category.WEIGHT),

            UnitItem(UnitType.CELSIUS, "Celsius (°C)", Category.TEMPERATURE),
            UnitItem(UnitType.FAHRENHEIT, "Fahrenheit (°F)", Category.TEMPERATURE),
            UnitItem(UnitType.KELVIN, "Kelvin (K)", Category.TEMPERATURE),

            UnitItem(UnitType.KMH, "km/h", Category.SPEED),
            UnitItem(UnitType.MS, "m/s", Category.SPEED),
            UnitItem(UnitType.MPH, "mph", Category.SPEED)
        )
    }
}
