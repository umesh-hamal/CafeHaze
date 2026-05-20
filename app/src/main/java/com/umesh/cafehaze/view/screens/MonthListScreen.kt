package com.umesh.cafehaze.view.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.umesh.cafehaze.viewmodel.BillViewModel
import com.umesh.cafehaze.utils.parseToIst
import java.time.format.DateTimeFormatter

private val Background = Color(0xFFF7F6F3)
private val PrimaryText = Color(0xFF1C1917)
private val SecondaryText = Color(0xFF78716C)
private val Accent = Color(0xFF7C3AED)

@Composable
fun MonthListScreen(
    innerPadding: PaddingValues,
    navController: NavController,
    billViewModel: BillViewModel,
    onBack: () -> Unit
) {

    val bills by billViewModel.bills.collectAsState()

    val months = bills.mapNotNull { data ->
        try {
            parseToIst(data.bill.createdAt)
                .toLocalDate()
                .withDayOfMonth(1)
        } catch (_: Exception) {
            null
        }
    }.distinct()

    val grouped = months
        .groupBy { it.year }
        .toSortedMap(compareByDescending { it })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 16.dp,
                end = 16.dp,
                bottom = innerPadding.calculateBottomPadding()
            )
    ) {

        // 🔥 HEADER CARD (matches your Transaction screen)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp),
            shape = RoundedCornerShape(28.dp),
            color = Color.White,
            shadowElevation = 10.dp
        ) {

            Row(
                modifier = Modifier.padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Surface(
                    modifier = Modifier.size(44.dp),
                    shape = CircleShape,
                    color = Color(0xFFF1F5F9),
                    onClick = onBack
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = PrimaryText
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "Select Month",
                        fontWeight = FontWeight.Bold,
                        color = PrimaryText
                    )

                    Text(
                        text = "${months.size} available months",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SecondaryText
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 📅 LIST
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {

            grouped.forEach { (year, monthsList) ->

                item {
                    Text(
                        text = year.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Accent,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                items(
                    monthsList.sortedByDescending { it.monthValue }
                ) { month ->

                    val monthName = month.format(
                        DateTimeFormatter.ofPattern("MMMM")
                    )

                    val key =
                        "${month.year}-${month.monthValue.toString().padStart(2, '0')}"

                    MonthCard(
                        monthName = monthName,
                        year = month.year,
                        onClick = {
                            navController.navigate("month_transactions/$key")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MonthCard(
    monthName: String,
    year: Int,
    onClick: () -> Unit
) {

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 6.dp, // softer
        border = BorderStroke(1.dp, Color(0xFFF1F1F1))
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {

                Text(
                    text = monthName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryText
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "View transactions",
                    style = MaterialTheme.typography.bodySmall,
                    color = SecondaryText
                )
            }

            Surface(
                shape = CircleShape,
                color = Color(0xFFF8FAFC)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = SecondaryText,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(18.dp)
                )
            }
        }
    }
}