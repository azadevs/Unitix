package com.azadevs.unitix.features.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.azadevs.unitix.data.local.UnitixDatabase
import com.azadevs.unitix.data.model.Category
import com.azadevs.unitix.data.network.CurrencyApi
import com.azadevs.unitix.data.repository.CurrencyRepository
import com.azadevs.unitix.data.repository.HistoryRepository
import com.azadevs.unitix.features.converter.ConvertScreen
import com.azadevs.unitix.features.converter.viewmodel.ConverterViewModel
import com.azadevs.unitix.features.converter.viewmodel.ConverterViewModelFactory
import com.azadevs.unitix.features.currency.CurrencyScreen
import com.azadevs.unitix.features.currency.CurrencyViewModel
import com.azadevs.unitix.features.currency.CurrencyViewModelFactory
import com.azadevs.unitix.features.home.HomeScreen
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
                    if (category == Category.CURRENCY) {
                        navController.navigate(CurrencyScreenRoute)
                    } else {
                        navController.navigate(ConverterScreenRoute(category.name))
                    }
                },
                onSmartClipboardTrigger = { categoryName, value, unitType ->
                    navController.navigate(ConverterScreenRoute(categoryName, value, unitType))
                }
            )
        }

        composable<ConverterScreenRoute> { data ->
            val args = data.toRoute<ConverterScreenRoute>()
            val category = Category.entries.find { entry ->
                entry.name == args.categoryName
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
                viewModel = viewModel,
                prefillValue = args.prefillValue,
                prefillUnitType = args.prefillUnitType
            )
        }

        composable<CurrencyScreenRoute> {
            val context = LocalContext.current
            val database = remember { UnitixDatabase.getDatabase(context) }
            val retrofit = remember {
                Retrofit.Builder()
                    .baseUrl(CurrencyApi.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            val api = remember { retrofit.create(CurrencyApi::class.java) }
            val repository = remember { CurrencyRepository(api, database.currencyDao()) }
            val factory = remember { CurrencyViewModelFactory(repository) }
            val viewModel: CurrencyViewModel = viewModel(factory = factory)

            CurrencyScreen(
                viewModel = viewModel,
                onBack = { navController.navigateUp() }
            )
        }

    }

}