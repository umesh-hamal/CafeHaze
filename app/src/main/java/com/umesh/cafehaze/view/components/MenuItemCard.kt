package com.umesh.cafehaze.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.umesh.cafehaze.model.data.MenuItem
import com.umesh.cafehaze.viewmodel.OrderViewModel

private val CardBackground = Color(0xFFF3E8DC)

private val PrimaryText = Color(0xFF2D1B12)

private val FavoriteRed = Color(0xFFD96B5F)

@Composable
fun MenuItemCard(
    item: MenuItem,
    orderViewModel: OrderViewModel,
    onToggleFavorite: (MenuItem) -> Unit
) {

    val orders = orderViewModel.currentOrders

    val quantity = orders.firstOrNull {
        it.menuItemId == item.id
    }?.quantity ?: 0

    Card(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .fillMaxWidth()
            .height(240.dp),

        shape = RoundedCornerShape(24.dp),

        elevation = CardDefaults.cardElevation(0.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(24.dp))
                .background(CardBackground)
                .padding(10.dp)
        ) {

            // IMAGE SECTION
            Box {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0xFFF8EFE6))
                ) {

                    AsyncImage(
                        model = item.image,

                        contentDescription = item.name,

                        modifier = Modifier.fillMaxSize(),

                        contentScale = ContentScale.Crop
                    )
                }

                // FAVORITE BUTTON
                IconButton(
                    onClick = {
                        onToggleFavorite(item)
                    },

                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0x99FFFFFF))
                            .padding(6.dp)
                    ) {

                        Icon(
                            imageVector = if (item.isFavorite)
                                Icons.Filled.Favorite
                            else
                                Icons.Outlined.FavoriteBorder,

                            contentDescription = "Favorite",

                            tint = if (item.isFavorite)
                                FavoriteRed
                            else
                                Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // ITEM NAME
            Text(
                text = item.name,

                fontSize = 15.sp,

                fontWeight = FontWeight.SemiBold,

                color = PrimaryText,

                maxLines = 1,

                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.weight(1f))

            // BOTTOM SECTION
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),

                verticalAlignment = Alignment.CenterVertically,

                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                // PRICE
                Text(
                    text = "₹${"%.0f".format(item.price)}",

                    fontWeight = FontWeight.ExtraBold,

                    fontSize = 24.sp,

                    color = PrimaryText
                )

                // QUANTITY SELECTOR
                Box(
                    modifier = Modifier.width(110.dp),

                    contentAlignment = Alignment.CenterEnd
                ) {

                    QuantitySelector(
                        quantity = quantity,

                        onIncrease = {
                            orderViewModel.increaseItem(
                                menuItemId = item.id,
                                price = item.price
                            )
                        },

                        onDecrease = {
                            orderViewModel.decreaseItem(item.id)
                        }
                    )
                }
            }
        }
    }
}