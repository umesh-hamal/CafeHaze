package com.umesh.cafehaze.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.*
import com.umesh.cafehaze.view.components.BottomBar
import com.umesh.cafehaze.view.screens.*
import com.umesh.cafehaze.viewmodel.*

@Composable
fun MainScreen() {

    val navController = rememberNavController()

    val menuViewModel: MenuViewModel = hiltViewModel()
    val orderViewModel: OrderViewModel = hiltViewModel()
    val paymentViewModel: PaymentViewModel = hiltViewModel()
    val billViewModel: BillViewModel = hiltViewModel()
    val dashboardViewModel: DashboardViewModel = hiltViewModel()
    val menuItems by menuViewModel.menuItems.collectAsState()


    Scaffold(containerColor =
        Color(0xFFF7F6E5)
    ){ innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {

            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {

                composable("home") {
                    HomeScreen(
                        menuViewModel = menuViewModel,
                        orderViewModel = orderViewModel,
                    )
                }

                composable("fav") {
                    FavoriteScreen(
                        menuViewModel = menuViewModel,
                        orderViewModel = orderViewModel,
                        innerPadding = innerPadding
                    )
                }

                composable("billing") {
                    BillingScreen(
                        orderViewModel = orderViewModel,
                        menuItems = menuItems,
                        paymentViewModel = paymentViewModel
                    )
                }

                composable("dashboard") {
                    DashboardScreen(
                        navController = navController,
                        dashboardViewModel = dashboardViewModel,
                        billViewModel = billViewModel,
                        menuViewModel = menuViewModel
                        )
                }


                composable("transaction_screen") {
                    TransactionScreen(
                        billViewModel = billViewModel,
                        innerPadding = innerPadding,
                        navController = navController,
                        onBack = { navController.popBackStack() },
                        menuItems = menuItems
                    )
                }

                composable("month_list_screen") {
                    MonthListScreen(
                        innerPadding = innerPadding,
                        navController = navController,
                        billViewModel = billViewModel,
                        onBack = { navController.popBackStack() }
                    )
                }

                composable("month_transactions/{month}") { backStackEntry ->

                    val month = backStackEntry.arguments?.getString("month") ?: ""

                    MonthTransactionsScreen(
                        billViewModel = billViewModel,
                        selectedMonth = month,
                        innerPadding = innerPadding,
                        menuItems = menuItems,
                        onBack = { navController.popBackStack() }
                    )
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp)
            ) {
                BottomBar(navController = navController)
            }
        }
    }
}