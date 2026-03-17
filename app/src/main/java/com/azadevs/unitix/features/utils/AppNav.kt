package com.azadevs.unitix.features.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.azadevs.unitix.data.local.UnitixDatabase
import com.azadevs.unitix.data.model.Category
import com.azadevs.unitix.data.network.CurrencyApi
import com.azadevs.unitix.data.repository.CurrencyRepository
import com.azadevs.unitix.data.repository.HistoryRepository
import com.azadevs.unitix.features.components.GlassBottomNavigationBar
import com.azadevs.unitix.features.converter.ConvertScreen
import com.azadevs.unitix.features.converter.viewmodel.ConverterViewModel
import com.azadevs.unitix.features.converter.viewmodel.ConverterViewModelFactory
import com.azadevs.unitix.features.currency.CurrencyScreen
import com.azadevs.unitix.features.currency.CurrencyViewModel
import com.azadevs.unitix.features.currency.CurrencyViewModelFactory
import com.azadevs.unitix.features.home.HomeScreen
import com.azadevs.unitix.features.settings.SettingsScreen
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by : Azamat Kalmurzaev
 * 26/01/26
 */
@Composable
fun AppNav(modifier: Modifier = Modifier) {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar by remember {
        derivedStateOf {
            navBackStackEntry?.destination?.route?.let { route ->
                !route.contains("ConverterScreenRoute")
            } ?: true
        }
    }

    // Hoist DI dependencies to AppNav scope to avoid re-creation on navigation
    val context = LocalContext.current
    val database = remember { UnitixDatabase.getDatabase(context) }
    val retrofit = remember {
        Retrofit.Builder()
            .baseUrl(CurrencyApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val currencyApi = remember { retrofit.create(CurrencyApi::class.java) }
    val currencyRepository = remember { CurrencyRepository(currencyApi, database.currencyDao()) }
    val currencyFactory = remember { CurrencyViewModelFactory(currencyRepository) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                GlassBottomNavigationBar(
                    navController = navController,
                    currentDestination = currentDestination
                )
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = HomeScreenRoute,
            modifier = Modifier.fillMaxSize()
        ) {
            composable<HomeScreenRoute> {
                HomeScreen(
                    onCategoryClick = { category ->
                        navController.navigate(ConverterScreenRoute(category.name))
                    },
                    onSmartClipboardTrigger = { categoryName, value, unitType ->
                        navController.navigate(ConverterScreenRoute(categoryName, value, unitType))
                    }
                )
            }

            composable<ConverterScreenRoute> { data ->
                val args = data.toRoute<ConverterScreenRoute>()
                val category = Category.entries.find { it.name == args.categoryName }

                val repository = remember { HistoryRepository(database.historyDao()) }
                val factory = remember { ConverterViewModelFactory(repository) }
                val viewModel: ConverterViewModel = viewModel(factory = factory)

                ConvertScreen(
                    category = category,
                    onBack = { navController.navigateUp() },
                    viewModel = viewModel,
                    prefillValue = args.prefillValue,
                    prefillUnitType = args.prefillUnitType
                )
            }

            composable<CurrencyScreenRoute> {
                val viewModel: CurrencyViewModel = viewModel(factory = currencyFactory)
                CurrencyScreen(viewModel = viewModel)
            }

            composable<SettingsScreenRoute> {
                SettingsScreen()
            }
        }
    }
}