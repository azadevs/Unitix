package com.azadevs.unitix.features.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.azadevs.unitix.data.model.Category
import com.azadevs.unitix.features.converter.ConvertScreen
import com.azadevs.unitix.features.home.HomeScreen

/**
 * Created by : Azamat Kalmurzaev
 * 26/01/26
 */
@Composable
fun AppNav(modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HomeScreenRoute,
        modifier = modifier
    ) {
        composable<HomeScreenRoute> {
            HomeScreen(
                onCategoryClick = { category ->
                    navController.navigate(ConverterScreenRoute(category.name))
                },
                onSmartClipboardClick = { category, value, unitName ->
                    navController.navigate(ConverterScreenRoute(category.name, value, unitName))
                }
            )
        }

        composable<ConverterScreenRoute> { data ->
            val route = data.toRoute<ConverterScreenRoute>()
            val category = Category.entries.find { entry ->
                entry.name == route.categoryName
            }
            ConvertScreen(
                category = category,
                initialValue = route.initialValue,
                initialFromUnitName = route.initialFromUnitName,
                onBack = {
                    navController.navigateUp()
                }
            )
        }

    }

}