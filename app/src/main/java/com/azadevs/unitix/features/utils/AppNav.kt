package com.azadevs.unitix.features.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.azadevs.unitix.data.local.UnitixDatabase
import com.azadevs.unitix.data.model.Category
import com.azadevs.unitix.data.repository.HistoryRepository
import com.azadevs.unitix.features.converter.ConvertScreen
import com.azadevs.unitix.features.converter.viewmodel.ConverterViewModel
import com.azadevs.unitix.features.converter.viewmodel.ConverterViewModelFactory
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
                }
            )
        }

        composable<ConverterScreenRoute> { data ->
            val categoryName = data.arguments?.getString("categoryName")
            val category = Category.entries.find { entry ->
                entry.name == categoryName
            }

            val context = LocalContext.current
            val database = remember { UnitixDatabase.getDatabase(context) }
            val repository = remember { HistoryRepository(database.historyDao()) }
            val factory = remember { ConverterViewModelFactory(repository) }
            val viewModel: ConverterViewModel = viewModel(factory = factory)

            ConvertScreen(
                category = category,
                onBack = {
                    navController.navigateUp()
                },
                viewModel = viewModel
            )
        }

    }

}