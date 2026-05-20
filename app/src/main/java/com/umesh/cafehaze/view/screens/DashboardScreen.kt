package com.umesh.cafehaze.view.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.umesh.cafehaze.model.data.BillWithItems
import com.umesh.cafehaze.utils.getWeeekLabels
import com.umesh.cafehaze.view.components.PremiumStatCard
import com.umesh.cafehaze.view.components.SalesOverviewCard
import com.umesh.cafehaze.view.components.TransactionCard
import com.umesh.cafehaze.view.components.TransactionDetailBottomSheet
import com.umesh.cafehaze.viewmodel.BillViewModel
import com.umesh.cafehaze.viewmodel.DashboardViewModel
import com.umesh.cafehaze.viewmodel.MenuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    dashboardViewModel: DashboardViewModel,
    billViewModel: BillViewModel,
    menuViewModel: MenuViewModel,
) {

    val state by dashboardViewModel
        .uiState
        .collectAsState()

    val bills by billViewModel
        .bills
        .collectAsState()

    val menuItems by menuViewModel
        .menuItems
        .collectAsState()

    var selectedBillId by rememberSaveable {
        mutableStateOf<Int?>(null)
    }

    var selectedTransaction by remember {
        mutableStateOf<BillWithItems?>(null)
    }

    val sheetState =
        rememberModalBottomSheetState()

    val animatedToday by animateFloatAsState(
        targetValue = state.todayTotal.toFloat(),
        label = ""
    )

    val animatedMonth by animateFloatAsState(
        targetValue = state.monthTotal.toFloat(),
        label = ""
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(
            bottom = 16.dp
        )
    ) {

        // STATS
        item {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                PremiumStatCard(
                    title = "Today",
                    value = animatedToday,
                    previousValue =
                        state.yesterdayTotal.toFloat(),
                    accent = Color(0xFF6366F1),
                    modifier = Modifier.weight(1f)
                )

                PremiumStatCard(
                    title = "This Month",
                    value = animatedMonth,
                    previousValue =
                        state.lastMonthTotal.toFloat(),
                    accent = Color(0xFF10B981),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // GRAPH
        item {

            Box(
                modifier =
                    Modifier.padding(
                        horizontal = 16.dp
                    )
            ) {

                SalesOverviewCard(
                    onlineData =
                        state.onlineGraph,
                    offlineData =
                        state.offlineGraph,
                    labels = getWeeekLabels()
                )
            }
        }

        // RECENT TRANSACTIONS
        item {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 100.dp
                    ),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(
                    containerColor =
                        Color(0xFFFFFBF5)
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = Color(0xFFF2E9DC)
                ),
                elevation =
                    CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    )
            ) {

                Column(
                    modifier = Modifier.padding(
                        horizontal = 20.dp,
                        vertical = 20.dp
                    )
                ) {

                    // HEADER
                    Row(
                        modifier =
                            Modifier.fillMaxWidth(),
                        horizontalArrangement =
                            Arrangement.SpaceBetween,
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Column {

                            Text(
                                text =
                                    "Recent Transactions",
                                style =
                                    MaterialTheme
                                        .typography
                                        .titleLarge,
                                fontWeight =
                                    FontWeight.Bold,
                                color =
                                    Color(0xFF2B2118)
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(4.dp)
                            )

                            Text(
                                text =
                                    "Latest 5 orders",
                                style =
                                    MaterialTheme
                                        .typography
                                        .bodyMedium,
                                color =
                                    Color(0xFF9A8F85)
                            )
                        }

                        Surface(
                            shape =
                                RoundedCornerShape(
                                    14.dp
                                ),
                            color =
                                Color(0xFFF4EDE4)
                        ) {

                            TextButton(
                                onClick = {
                                    navController.navigate(
                                        "transaction_screen"
                                    )
                                },
                                contentPadding =
                                    PaddingValues(
                                        horizontal = 8.dp,
                                        vertical = 2.dp
                                    )
                            ) {

                                Text(
                                    text = "See All",
                                    color =
                                        Color(
                                            0xFF7A5C3E
                                        ),
                                    fontWeight =
                                        FontWeight
                                            .SemiBold
                                )
                            }
                        }
                    }

                    Spacer(
                        modifier =
                            Modifier.height(
                                18.dp
                            )
                    )

                    HorizontalDivider(
                        thickness = 1.dp,
                        color =
                            Color(0xFFF0E8DD)
                    )

                    Spacer(
                        modifier =
                            Modifier.height(
                                12.dp
                            )
                    )

                    if (state.loading) {

                        Box(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        vertical = 28.dp
                                    ),
                            contentAlignment =
                                Alignment.Center
                        ) {

                            CircularProgressIndicator()
                        }

                    } else if (
                        state.recentTransactions
                            .isEmpty()
                    ) {

                        Box(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        vertical = 28.dp
                                    ),
                            contentAlignment =
                                Alignment.Center
                        ) {

                            Text(
                                text =
                                    "No transactions yet",
                                color =
                                    Color(
                                        0xFF8E8E93
                                    )
                            )
                        }

                    } else {

                        state.recentTransactions
                            .forEachIndexed {
                                    index,
                                    transaction ->

                                Surface(
                                    shape =
                                        RoundedCornerShape(
                                            20.dp
                                        ),
                                    color =
                                        Color(
                                            0xFFFFFDF9
                                        )
                                ) {

                                    TransactionCard(
                                        bill =
                                            transaction.bill,
                                        time =
                                            transaction.time,
                                        onClick = {

                                            selectedBillId =
                                                transaction
                                                    .bill
                                                    .billId

                                            selectedTransaction =
                                                bills.find {
                                                    it.bill.billId ==
                                                            transaction.bill.billId
                                                }
                                        }
                                    )
                                }

                                if (
                                    index !=
                                    state
                                        .recentTransactions
                                        .lastIndex
                                ) {

                                    Spacer(
                                        modifier =
                                            Modifier.height(
                                                10.dp
                                            )
                                    )
                                }
                            }
                    }
                }
            }
        }
    }

    selectedTransaction?.let { data ->

        TransactionDetailBottomSheet(
            selectedData = data,
            menuItems = menuItems,
            onDismiss = {
                selectedTransaction = null
            },
            sheetState = sheetState
        )
    }
}