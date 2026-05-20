package com.umesh.cafehaze.view.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.umesh.cafehaze.model.data.MenuItem
import com.umesh.cafehaze.utils.parseToIst
import com.umesh.cafehaze.view.components.TransactionCard
import com.umesh.cafehaze.view.components.TransactionDetailBottomSheet
import com.umesh.cafehaze.viewmodel.BillViewModel
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val PrimaryText = Color(0xFF0F172A)
private val SecondaryText = Color(0xFF64748B)
private val AccentPurple = Color(0xFF7C3AED)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    billViewModel: BillViewModel,
    innerPadding: PaddingValues,
    navController: NavController,
    onBack: () -> Unit,
    menuItems: List<MenuItem>
) {

    val bills by billViewModel.bills.collectAsState()
    var selectedBillId by rememberSaveable { mutableStateOf<Int?>(null) }
    val selectedData = bills.find { it.bill.billId == selectedBillId }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val zone = ZoneId.of("Asia/Kolkata")
    val today = LocalDate.now(zone)
    val yesterday = today.minusDays(1)

    var selectedChip by remember { mutableStateOf("TODAY") }

    val parsed = bills.mapNotNull {
        try { parseToIst(it.bill.createdAt) to it } catch (_: Exception) { null }
    }

    val filtered = when (selectedChip) {
        "TODAY" -> parsed.filter { it.first.toLocalDate() == today }
        "MONTH" -> parsed.filter {
            it.first.month == today.month && it.first.year == today.year
        }
        else -> parsed
    }

    val grouped = filtered.groupBy { it.first.toLocalDate() }
    val sortedDates = grouped.keys.sortedDescending()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Header(onBack, filtered.size)

        FilterRow(selectedChip, navController) {
            selectedChip = it
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            if (grouped.isEmpty()) {
                item { EmptyState() }
            } else {

                sortedDates.forEach { date ->

                    stickyHeader {
                        DateHeader(date, today, yesterday)
                    }

                    items(grouped[date] ?: emptyList()) { (dateTime, data) ->

                        val bill = data.bill

                        val time = dateTime.format(
                            DateTimeFormatter.ofPattern("hh:mm a")
                        )

                        TransactionCard(
                            bill = bill,
                            time = time,
                            onClick = { selectedBillId = bill.billId }
                        )
                    }
                }
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
fun DateHeader(
    date: LocalDate,
    today: LocalDate,
    yesterday: LocalDate
) {

    val text = when (date) {
        today -> "Today"
        yesterday -> "Yesterday"
        else -> date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(vertical = 6.dp)
    ) {

        Surface(
            shape = RoundedCornerShape(50),
            color = Color(0xFFF1F5F9),
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(Color(0xFF7C3AED), CircleShape)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = text,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF0F172A)
                )
            }
        }
    }
}



@Composable
fun Header(onBack: () -> Unit, count: Int) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(28.dp),
        color = Color.White,
        shadowElevation = 8.dp
    ) {

        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Surface(
                modifier = Modifier.size(46.dp),
                shape = CircleShape,
                color = Color(0xFFF1F5F9),
                onClick = onBack
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text("Transaction History", fontWeight = FontWeight.Bold)
                Text("$count Transactions", color = SecondaryText)
            }
        }
    }
}

@Composable
fun FilterRow(
    selected: String,
    navController: NavController,
    onSelect: (String) -> Unit
) {

    LazyRow(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        item {
            FilterChipItem("All", selected == "ALL", Icons.Default.GridView, Color(0xFF8B5CF6)) {
                onSelect("ALL")
            }
        }

        item {
            FilterChipItem("Today", selected == "TODAY", Icons.Default.DateRange, Color(0xFF10B981)) {
                onSelect("TODAY")
            }
        }

        item {
            FilterChipItem("This Month", selected == "MONTH", Icons.Default.CalendarMonth, Color(0xFF3B82F6)) {
                onSelect("MONTH")
            }
        }

        item {
            FilterChipItem("Select Month", false, Icons.Default.CalendarMonth, Color(0xFFEC4899)) {
                navController.navigate("month_list_screen")
            }
        }
    }
}

@Composable
fun EmptyState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 60.dp),
        contentAlignment = Alignment.Center
    ) {
        Text("No transactions", color = SecondaryText)
    }
}
@Composable
fun FilterChipItem(
    text: String,
    selected: Boolean,
    icon: ImageVector,
    iconColor: Color,
    onClick: () -> Unit
) {

    val scale by animateFloatAsState(
        targetValue = if (selected) 1.03f else 1f,
        label = ""
    )

    Surface(
        onClick = onClick,
        modifier = Modifier.scale(scale),
        shape = RoundedCornerShape(24.dp),
        color = if (selected) AccentPurple else Color(0xFFFFFCF8),
        border = BorderStroke(
            1.dp,
            if (selected) AccentPurple else Color(0xFFE8DFD6)
        ),
        shadowElevation = if (selected) 6.dp else 1.dp
    ) {

        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        if (selected)
                            Color.White.copy(alpha = 0.15f)
                        else
                            Color(0xFFF4EFE9)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    icon,
                    contentDescription = null,
                    tint = if (selected) Color.White else iconColor,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = text,
                color = if (selected) Color.White else PrimaryText,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}