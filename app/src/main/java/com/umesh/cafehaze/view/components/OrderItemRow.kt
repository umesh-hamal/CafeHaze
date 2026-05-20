package com.umesh.cafehaze.view.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.umesh.cafehaze.model.data.BillItem

@Composable
fun OrderItemRow(
    item: BillItem,
    itemName: String, // ✅ pass from menu
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column {
            Text(itemName)
            Text("₹${"%.0f".format(item.priceAtTime)}")
        }

        Text("₹${"%.0f".format(item.quantity * item.priceAtTime)}")

        Row(verticalAlignment = Alignment.CenterVertically) {

            IconButton(onClick = onDecrease) {
                Icon(Icons.Default.Remove, contentDescription = null)
            }

            Text(item.quantity.toString())

            IconButton(onClick = onIncrease) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    }
}