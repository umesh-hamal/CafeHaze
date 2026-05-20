package com.umesh.cafehaze.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

private val Tutu = Color(0xFFFFFFFF)
private val BabyBlue = Color(0xFFF3E8DC)


private val SelectedStart = Color(0xFFF7F6E5)
private val SelectedEnd = Color(0xFFDFFFCD)

private val SoftGlass = Color(0x55FFFFFF)

private val DarkText = Color(0xFF1B2B34)

@Composable
fun BottomBar(navController: NavHostController) {

    val items = listOf(
        "home" to Icons.Default.Home,
        "billing" to Icons.Default.ShoppingCart,
        "fav" to Icons.Default.Bookmark,
        "dashboard" to Icons.Default.Analytics,
    )

    val currentRoute by navController.currentBackStackEntryAsState()

    val route = currentRoute?.destination?.route

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),

        contentAlignment = Alignment.Center
    ) {

        Row(
            modifier = Modifier
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(50),
                    ambientColor = Color(0x22000000),
                    spotColor = Color(0x22000000)
                )
                .clip(RoundedCornerShape(50))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Tutu,
                            BabyBlue
                        )
                    )
                )
                .padding(
                    horizontal = 10.dp,
                    vertical = 8.dp
                ),

            horizontalArrangement = Arrangement.spacedBy(8.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {

            items.forEach { (screen, icon) ->

                val isSelected = route == screen

                if (isSelected) {

                    // SELECTED ITEM

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        SelectedStart,
                                        SelectedEnd
                                    )
                                )
                            )
                            .clickable {
                                navController.navigate(screen) {
                                    popUpTo("home")
                                    launchSingleTop = true
                                }
                            }
                            .padding(
                                horizontal = 18.dp,
                                vertical = 11.dp
                            ),

                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = icon,
                            contentDescription = screen,

                            tint = DarkText
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = screen.replaceFirstChar {
                                it.uppercase()
                            },

                            color = DarkText,

                            fontWeight = FontWeight.SemiBold
                        )
                    }

                } else {

                    // UNSELECTED ITEM

                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(SoftGlass)
                            .clickable {
                                navController.navigate(screen) {
                                    popUpTo("home")
                                    launchSingleTop = true
                                }
                            },

                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = icon,
                            contentDescription = screen,

                            tint = DarkText
                        )
                    }
                }
            }
        }
    }
}