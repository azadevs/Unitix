package com.azadevs.unitix.features.utils

import com.azadevs.unitix.data.model.Category
import kotlinx.serialization.Serializable

/**
 * Created by : Azamat Kalmurzaev
 * 26/01/26
 */

@Serializable
object HomeScreenRoute

@Serializable
data class ConverterScreenRoute(val category: Category)