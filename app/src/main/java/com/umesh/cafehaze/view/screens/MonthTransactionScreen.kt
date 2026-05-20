package com.umesh.cafehaze.view.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.dp
import com.umesh.cafehaze.model.data.MenuItem
import com.umesh.cafehaze.utils.parseToIst
import com.umesh.cafehaze.view.components.TransactionCard
import com.umesh.cafehaze.view.components.TransactionDetailBottomSheet
import com.umesh.cafehaze.viewmodel.BillViewModel
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthTransactionsScreen(
    selectedMonth: String,
    billViewModel: BillViewModel,
    innerPadding: PaddingValues,
    menuItems: List<MenuItem>,
    onBack: () -> Unit
) {

    val bills by billViewModel.bills.collectAsState()

    var selectedBillId by rememberSaveable { mutableStateOf<Int?>(null) }

    val selectedData = bills.find { it.bill.billId == selectedBillId }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val filteredBills = bills.filter { data ->
        try {
            val date = parseToIst(data.bill.createdAt)
            val key = "${date.year}-${date.monthValue.toString().padStart(2, '0')}"
            key == selectedMonth
        } catch (_: Exception) {
            false
        }
    }

    val groupedBills = filteredBills.groupBy {
        try {
            parseToIst(it.bill.createdAt).toLocalDate()
        } catch (_: Exception) {
            null
        }
    }

    val monthlyTotal = filteredBills.sumOf { it.bill.totalAmount }

    val monthTitle = try {
        val parts = selectedMonth.split("-")
        val year = parts[0].toInt()
        val month = parts[1].toInt()
        val date = YearMonth.of(year, month)

        date.month.name.lowercase()
            .replaceFirstChar { it.uppercase() } + " $year"

    } catch (_: Exception) {
        selectedMonth
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
,

        verticalArrangement = Arrangement.spacedBy(16.dp),

        contentPadding = PaddingValues(top = 8.dp, bottom = 120.dp)
    ) {

        // HEADER
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                shape = RoundedCornerShape(28.dp), // 👈 big rounded corners
                color = Color(0xFFF8F7F2),         // soft background
                tonalElevation = 2.dp,
                shadowElevation = 6.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 18.dp),

                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Surface(
                        modifier = Modifier.size(44.dp),
                        shape = CircleShape,
                        color = Color.White,
                        shadowElevation = 4.dp,
                        onClick = onBack
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))


                        Text(
                            text = monthTitle,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF475569)
                        )

                }
            }
        }


        // HERO CARD
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(34.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(0xFF0F172A),
                                Color(0xFF1E293B),
                                Color(0xFF0B1220)
                            )
                        )
                    )
                    .padding(26.dp)
            ) {

                Column {

                    Text(
                        "Monthly Revenue",
                        color = Color(0xFF94A3B8)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        "₹%,.0f".format(monthlyTotal),
                        color = Color.White,
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        GlassInfoCard(
                            "Transactions",
                            "${filteredBills.size}",
                            Modifier.weight(1f)
                        )

                        GlassInfoCard(
                            "Month",
                            monthTitle,
                            Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        item {
            Text(
                "Transactions",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF020617)
            )
        }

        groupedBills.forEach { (date, billsForDay) ->

            if (date != null) {

                stickyHeader {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()

                            .padding(vertical = 6.dp)
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {

                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(Color.White)
                                    .border(
                                        1.dp,
                                        Color(0xFFE5E7EB),
                                        RoundedCornerShape(50)
                                    )
                                    .padding(horizontal = 14.dp, vertical = 8.dp),

                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(Color(0xFF6366F1), CircleShape)
                                )

                                Spacer(modifier = Modifier.width(10.dp))

                                Text(
                                    text = date.format(
                                        DateTimeFormatter.ofPattern("dd MMM yyyy")
                                    ),
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF020617)
                                )
                            }
                        }
                    }
                }
            }

            items(billsForDay ?: emptyList()) { data ->

                val time = try {
                    parseToIst(data.bill.createdAt)
                        .format(DateTimeFormatter.ofPattern("hh:mm a"))
                } catch (_: Exception) {
                    ""
                }

                TransactionCard(
                    bill = data.bill,
                    time = time,
                    onClick = { selectedBillId = data.bill.billId }
                )
            }
        }
    }

    if (selectedData != null) {
        TransactionDetailBottomSheet(
            selectedData = selectedData,
            menuItems = menuItems,
            onDismiss = { selectedBillId = null },
            sheetState = sheetState
        )
    }
}
@Composable
fun GlassInfoCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0x14FFFFFF))
            .border(
                width = 1.dp,
                color = Color(0x22FFFFFF),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(
                horizontal = 14.dp,
                vertical = 12.dp
            )
    ) {

        Text(
            text = title,
            color = Color(0xFF9CA3AF),
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = value,
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}