package com.umesh.cafehaze.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umesh.cafehaze.ui.theme.Green

@Composable
fun QuantitySelector(
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
) {

    if (quantity == 0) {

        Card(
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            modifier = Modifier
                .padding(4.dp)
                .size(width = 40.dp, height = 40.dp),
            onClick = { onIncrease() }
        ) {

            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                modifier = Modifier
                    .padding(6.dp)
                    .fillMaxSize()
            )
        }

    } else {

        Card(
            modifier = Modifier
                .padding(4.dp)
                .height(40.dp),
            colors = CardDefaults.cardColors(containerColor = Green),
            shape = RoundedCornerShape(12.dp)
        ) {

            Row(
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "-",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .clickable { onDecrease() }
                        .padding(horizontal = 8.dp)
                )

                Text(
                    text = quantity.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                )

                Text(
                    text = "+",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .clickable { onIncrease() }
                        .padding(horizontal = 8.dp)
                )
            }
        }
    }
}