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
            UnitItem(UnitType.MPH, "mph", Category.SPEED),

            UnitItem(UnitType.SQ_METER, "Square Meter (m²)", Category.AREA),
            UnitItem(UnitType.SQ_KILOMETER, "Square Kilometer (km²)", Category.AREA),
            UnitItem(UnitType.HECTARE, "Hectare (ha)", Category.AREA),
            UnitItem(UnitType.ACRE, "Acre (ac)", Category.AREA),
            UnitItem(UnitType.SQ_MILE, "Square Mile (mi²)", Category.AREA),

            UnitItem(UnitType.LITER, "Liter (L)", Category.VOLUME),
            UnitItem(UnitType.MILLILITER, "Milliliter (mL)", Category.VOLUME),
            UnitItem(UnitType.CUBIC_METER, "Cubic Meter (m³)", Category.VOLUME),
            UnitItem(UnitType.GALLON, "Gallon (gal)", Category.VOLUME),

            UnitItem(UnitType.BYTE, "Byte (B)", Category.DATA),
            UnitItem(UnitType.KILOBYTE, "Kilobyte (KB)", Category.DATA),
            UnitItem(UnitType.MEGABYTE, "Megabyte (MB)", Category.DATA),
            UnitItem(UnitType.GIGABYTE, "Gigabyte (GB)", Category.DATA),
            UnitItem(UnitType.TERABYTE, "Terabyte (TB)", Category.DATA),

            UnitItem(UnitType.MILLISECOND, "Millisecond (ms)", Category.TIME),
            UnitItem(UnitType.SECOND, "Second (s)", Category.TIME),
            UnitItem(UnitType.MINUTE, "Minute (min)", Category.TIME),
            UnitItem(UnitType.HOUR, "Hour (h)", Category.TIME),
            UnitItem(UnitType.DAY, "Day (d)", Category.TIME),
            UnitItem(UnitType.WEEK, "Week (wk)", Category.TIME),

            UnitItem(UnitType.WATT, "Watt (W)", Category.POWER),
            UnitItem(UnitType.KILOWATT, "Kilowatt (kW)", Category.POWER),
            UnitItem(UnitType.HORSEPOWER, "Horsepower (hp)", Category.POWER),

            UnitItem(UnitType.PASCAL, "Pascal (Pa)", Category.PRESSURE),
            UnitItem(UnitType.BAR, "Bar (bar)", Category.PRESSURE),
            UnitItem(UnitType.PSI, "Pound per Sq Inch (psi)", Category.PRESSURE),
            UnitItem(UnitType.ATMOSPHERE, "Atmosphere (atm)", Category.PRESSURE),

            UnitItem(UnitType.JOULE, "Joule (J)", Category.ENERGY),
            UnitItem(UnitType.KILOJOULE, "Kilojoule (kJ)", Category.ENERGY),
            UnitItem(UnitType.CALORIE, "Calorie (cal)", Category.ENERGY),
            UnitItem(UnitType.KILOCALORIE, "Kilocalorie (kcal)", Category.ENERGY),

            UnitItem(UnitType.DEGREE, "Degree (°)", Category.ANGLE),
            UnitItem(UnitType.RADIAN, "Radian (rad)", Category.ANGLE),
            UnitItem(UnitType.GRADIAN, "Gradian (grad)", Category.ANGLE)
        )
    }
}
