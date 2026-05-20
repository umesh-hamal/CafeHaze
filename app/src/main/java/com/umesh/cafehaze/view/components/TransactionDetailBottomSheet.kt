package com.umesh.cafehaze.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.umesh.cafehaze.model.data.BillWithItems
import com.umesh.cafehaze.model.data.MenuItem
import com.umesh.cafehaze.utils.parseToIst
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailBottomSheet(
    selectedData: BillWithItems,
    menuItems: List<MenuItem>,
    onDismiss: () -> Unit,
    sheetState: SheetState
) {

    val bill = selectedData.bill

    ModalBottomSheet(

        onDismissRequest = onDismiss,

        sheetState = sheetState,

        containerColor = Color(0xFFFFFBF7),

        dragHandle = {

            Box(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .size(
                        width = 44.dp,
                        height = 5.dp
                    )
                    .background(
                        Color(0xFFE5E7EB),
                        RoundedCornerShape(50)
                    )
            )
        }

    ) {

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),

            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                bottom = 30.dp
            ),

            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // HEADER

            item {

                Row(
                    modifier = Modifier.fillMaxWidth(),

                    horizontalArrangement = Arrangement.SpaceBetween,

                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(modifier = Modifier.padding(top = 22.dp)){

                        Text(
                            text = "Transaction Details",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF111827)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Bill #${bill.billId}",
                            color = Color(0xFF6B7280),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Surface(
                        color = Color(0xFFDCFCE7),
                        shape = RoundedCornerShape(20.dp)
                    ) {

                        Text(
                            text = "Paid",

                            modifier = Modifier.padding(
                                horizontal = 14.dp,
                                vertical = 8.dp
                            ),

                            color = Color(0xFF15803D),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }


            item {

                Card(

                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),

                    shape = RoundedCornerShape(28.dp),

                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 3.dp
                    )

                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    ) {

                        Text(
                            text = "Total Amount",
                            color = Color(0xFF6B7280)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "₹${bill.totalAmount}",

                            style = MaterialTheme.typography.headlineMedium,

                            fontWeight = FontWeight.Bold,

                            color = Color(0xFF7C3AED)
                        )
                    }
                }
            }


            item {

                Card(

                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),

                    shape = RoundedCornerShape(24.dp),

                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )

                ) {

                    Column(
                        modifier = Modifier.padding(20.dp),

                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {

                        DetailRow(
                            title = "Bill ID",
                            value = "#${bill.billId}"
                        )

                        HorizontalDivider(
                            color = Color(0xFFF3F4F6)
                        )

                        DetailRow(
                            title = "Items",
                            value = "${selectedData.items.size}"
                        )

                        HorizontalDivider(
                            color = Color(0xFFF3F4F6)
                        )

                        DetailRow(
                            title = "Date",
                            value = parseToIst(
                                bill.createdAt
                            ).format(
                                DateTimeFormatter.ofPattern(
                                    "dd MMM yyyy"
                                )
                            )
                        )

                        HorizontalDivider(
                            color = Color(0xFFF3F4F6)
                        )

                        DetailRow(
                            title = "Time",
                            value = parseToIst(
                                bill.createdAt
                            ).format(
                                DateTimeFormatter.ofPattern(
                                    "hh:mm a"
                                )
                            )
                        )
                    }
                }
            }


            item {

                Card(

                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),

                    shape = RoundedCornerShape(24.dp),

                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )

                ) {

                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {

                        Text(
                            text = "Items",

                            style = MaterialTheme.typography.titleLarge,

                            fontWeight = FontWeight.Bold,

                            color = Color(0xFF111827)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        selectedData.items.forEachIndexed { index, item ->

                            Row(
                                modifier = Modifier.fillMaxWidth(),

                                horizontalArrangement = Arrangement.SpaceBetween,

                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {

                                    Text(
                                        text = menuItems.find {
                                            it.id == item.menuItemId
                                        }?.name ?: "Unknown Item",

                                        style = MaterialTheme.typography.titleMedium,

                                        fontWeight = FontWeight.SemiBold,

                                        color = Color(0xFF111827)
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = "Qty: ${item.quantity}",

                                        color = Color(0xFF6B7280),

                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }

                                Surface(
                                    color = Color(0xFFF3F4F6),
                                    shape = RoundedCornerShape(14.dp)
                                ) {

                                    Text(
                                        text = "₹${
                                            item.priceAtTime * item.quantity
                                        }",

                                        modifier = Modifier.padding(
                                            horizontal = 12.dp,
                                            vertical = 8.dp
                                        ),

                                        color = Color(0xFF7C3AED),

                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            if (index != selectedData.items.lastIndex) {

                                Spacer(modifier = Modifier.height(18.dp))

                                HorizontalDivider(
                                    color = Color(0xFFF3F4F6)
                                )

                                Spacer(modifier = Modifier.height(18.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailRow(
    title: String,
    value: String
) {

    Row(
        modifier = Modifier.fillMaxWidth(),

        horizontalArrangement = Arrangement.SpaceBetween,

        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = title,

            color = Color(0xFF6B7280),

            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = value,

            color = Color(0xFF111827),

            fontWeight = FontWeight.SemiBold,

            style = MaterialTheme.typography.bodyLarge
        )
    }
}