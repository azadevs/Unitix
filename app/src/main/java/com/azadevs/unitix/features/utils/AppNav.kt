package com.azadevs.unitix.features.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
            HomeScreen { category ->
                navController.navigate(ConverterScreenRoute(category.name))
            }
        }

        composable<ConverterScreenRoute> { data ->
            val categoryName = data.arguments?.getString("categoryName")
            val category = Category.entries.find { entry ->
                entry.name == categoryName
            }
            ConvertScreen(
                category = category, {
                    navController.navigateUp()
                })
        }

    }

}