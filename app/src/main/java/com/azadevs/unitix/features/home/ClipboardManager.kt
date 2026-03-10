package com.azadevs.unitix.features.home

import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.azadevs.unitix.data.model.UnitItem
import com.azadevs.unitix.data.model.UnitType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "unitix_settings")

data class ClipboardData(
    val value: String,
    val unit: UnitItem,
    val rawText: String
)

class UnitixClipboardManager(private val context: Context) {
    private val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    private val LAST_CLIP_KEY = stringPreferencesKey("last_clipboard_text")
    private val regex = Regex("""([0-9]+[.,]?[0-9]*)\s*([a-zA-Z°²³μ/]+)""")

    suspend fun checkClipboardForUnits(): ClipboardData? {
        if (!clipboard.hasPrimaryClip()) {
            Log.d("ClipboardManager", "No primary clip")
            return null
        }
        val items = clipboard.primaryClip
        if (items == null || items.itemCount == 0) {
            Log.d("ClipboardManager", "Primary clip is empty")
            return null
        }

        val text = items.getItemAt(0).coerceToText(context).toString().trim()
        if (text.isEmpty()) {
            Log.d("ClipboardManager", "Text is empty")
            return null
        }

        Log.d("ClipboardManager", "Clipboard text: $text")

        // Check datastore
        val lastSaved = context.dataStore.data.map { pref -> pref[LAST_CLIP_KEY] }.first()
        if (lastSaved == text) {
            Log.d("ClipboardManager", "Already prompted for this clip")
            return null // Already prompted for this specific clip
        }

        // Try parsing
        val match = regex.find(text)
        if (match == null) {
            Log.d("ClipboardManager", "No regex match found in text")
            return null
        }
        val rawValue = match.groupValues[1]
        val valueStr = if (rawValue.contains(",") && rawValue.contains(".")) {
            rawValue.replace(",", "")
        } else {
            rawValue.replace(',', '.')
        }
        val unitStr = match.groupValues[2].lowercase().trim()

        Log.d("ClipboardManager", "Parsed value: $valueStr, unit: $unitStr")

        if (valueStr.toDoubleOrNull() == null) {
            Log.d("ClipboardManager", "Value could not be parsed to double: $valueStr")
            return null
        }

        val unitItem = findUnitAbbr(unitStr)
        if (unitItem == null) {
            Log.d("ClipboardManager", "Unit abbreviation not found: $unitStr")
            return null
        }

        Log.d("ClipboardManager", "Successfully found unit: ${unitItem.label}")
        return ClipboardData(valueStr, unitItem, text)
    }

    suspend fun markAsShown(rawText: String) {
        context.dataStore.edit { pref ->
            pref[LAST_CLIP_KEY] = rawText
        }
    }

    private fun findUnitAbbr(abbr: String): UnitItem? {
        val targetType = when (abbr) {
            "m", "meter", "meters" -> UnitType.METER
            "km", "kilometer", "kilometers" -> UnitType.KILOMETER
            "cm", "centimeter", "centimeters" -> UnitType.CENTIMETER
            "mm", "millimeter", "millimeters" -> UnitType.MILLIMETER
            "in", "inch", "inches" -> UnitType.INCH
            "ft", "foot", "feet" -> UnitType.FOOT
            "yd", "yard", "yards" -> UnitType.YARD
            "mi", "mile", "miles" -> UnitType.MILE
            "g", "gram", "grams" -> UnitType.GRAM
            "kg", "kilo", "kilogram", "kilograms" -> UnitType.KILOGRAM
            "lb", "lbs", "pound", "pounds", "oz" -> UnitType.POUND
            "c", "°c", "celsius" -> UnitType.CELSIUS
            "f", "°f", "fahrenheit" -> UnitType.FAHRENHEIT
            "k", "kelvin" -> UnitType.KELVIN
            "km/h", "kmh" -> UnitType.KMH
            "m/s" -> UnitType.MS
            "mph" -> UnitType.MPH
            "m2", "m²", "sqm" -> UnitType.SQ_METER
            "km2", "km²", "sqkm" -> UnitType.SQ_KILOMETER
            "ha", "hectare" -> UnitType.HECTARE
            "ac", "acre", "acres" -> UnitType.ACRE
            "mi2", "mi²", "sqmi" -> UnitType.SQ_MILE
            "l", "liter", "liters" -> UnitType.LITER
            "ml", "milliliter", "milliliters" -> UnitType.MILLILITER
            "m3", "m³" -> UnitType.CUBIC_METER
            "gal", "gallon", "gallons" -> UnitType.GALLON
            "b", "byte", "bytes" -> UnitType.BYTE
            "kb", "kilobyte", "kilobytes" -> UnitType.KILOBYTE
            "mb", "megabyte", "megabytes" -> UnitType.MEGABYTE
            "gb", "gigabyte", "gigabytes" -> UnitType.GIGABYTE
            "tb", "terabyte", "terabytes" -> UnitType.TERABYTE
            "ms", "millisecond", "milliseconds" -> UnitType.MILLISECOND
            "s", "sec", "second", "seconds" -> UnitType.SECOND
            "min", "mins", "minute", "minutes" -> UnitType.MINUTE
            "h", "hr", "hrs", "hour", "hours" -> UnitType.HOUR
            "d", "day", "days" -> UnitType.DAY
            "wk", "week", "weeks" -> UnitType.WEEK
            "w", "watt", "watts" -> UnitType.WATT
            "kw", "kilowatt", "kilowatts" -> UnitType.KILOWATT
            "hp", "horsepower" -> UnitType.HORSEPOWER
            "pa", "pascal" -> UnitType.PASCAL
            "bar", "bars" -> UnitType.BAR
            "psi" -> UnitType.PSI
            "atm", "atmosphere" -> UnitType.ATMOSPHERE
            "j", "joule", "joules" -> UnitType.JOULE
            "kj", "kilojoule", "kilojoules" -> UnitType.KILOJOULE
            "cal", "calorie", "calories" -> UnitType.CALORIE
            "kcal", "kilocalorie", "kilocalories" -> UnitType.KILOCALORIE
            "°", "deg", "degree", "degrees" -> UnitType.DEGREE
            "rad", "radian", "radians" -> UnitType.RADIAN
            "grad", "gradian", "gradians" -> UnitType.GRADIAN
            else -> return null
        }
        return UnitItem.units.find { it.type == targetType }
    }
}
