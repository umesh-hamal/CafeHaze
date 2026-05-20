package com.umesh.cafehaze.view.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.umesh.cafehaze.model.data.BillItem
import com.umesh.cafehaze.model.data.MenuItem
import com.umesh.cafehaze.viewmodel.OrderViewModel

@Composable
fun ExpandedBillSection(
    billItems: List<BillItem>,
    billId: Int,
    billTotal: Double,
    isLocked: Boolean,
    menuMap: Map<Int, MenuItem>,
    orderViewModel: OrderViewModel,
    onShowQr: (Double, Int) -> Unit,
    secondaryText: Color,
    accentBrown: Color
) {

    HorizontalDivider(
        color = Color(0xFFE8DED2)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFDF8F2))
            .padding(18.dp)
    ) {

        if (billItems.isEmpty()) {

            Text(
                text = "No items added",

                color = secondaryText,

                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            OutlinedButton(
                onClick = {

                    orderViewModel.deleteBill(
                        billId
                    )
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),

                shape = RoundedCornerShape(20.dp),

                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFFB3261E)
                ),

                border = BorderStroke(
                    1.dp,
                    Color(0xFFFFDAD6)
                )
            ) {

                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    text = "Delete Bill",

                    fontWeight = FontWeight.Bold
                )
            }

        } else {

            billItems.forEach { item ->

                val name =
                    menuMap[item.menuItemId]?.name
                        ?: "Loading..."

                Row(
                    modifier = Modifier.fillMaxWidth(),

                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = name,

                        modifier = Modifier.weight(1f),

                        style = MaterialTheme.typography.bodyLarge,

                        fontWeight = FontWeight.SemiBold,

                        color = Color(0xFF1E1B16)
                    )

                    Surface(
                        shape = RoundedCornerShape(18.dp),

                        color = Color(0xFFF3E6D8),

                        shadowElevation = 2.dp
                    ) {

                        Text(
                            text = "x${item.quantity}",

                            modifier = Modifier.padding(
                                horizontal = 14.dp,
                                vertical = 6.dp
                            ),

                            color = accentBrown,

                            fontWeight = FontWeight.ExtraBold,

                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(14.dp)
                )
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                Button(
                    onClick = {

                        if (!isLocked) {

                            onShowQr(
                                billTotal,
                                billId
                            )
                        }
                    },

                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),

                    shape = RoundedCornerShape(22.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = accentBrown,
                        contentColor = Color.White
                    )
                ) {

                    Icon(
                        imageVector = Icons.Default.QrCode,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    Text(
                        text = "Show QR",

                        style = MaterialTheme.typography.titleMedium,

                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = {

                        if (!isLocked) {

                            orderViewModel.deleteBill(
                                billId
                            )
                        }
                    },

                    modifier = Modifier
                        .height(56.dp)
                        .width(86.dp),

                    shape = RoundedCornerShape(22.dp),

                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFB3261E)
                    ),

                    border = BorderStroke(
                        1.dp,
                        Color(0xFFFFDAD6)
                    )
                ) {

                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null
                    )
                }
            }

            if (isLocked) {

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                Text(
                    text = "Payment in progress 🔒",

                    color = secondaryText,

                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}