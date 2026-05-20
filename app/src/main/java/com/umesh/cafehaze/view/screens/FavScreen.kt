package com.umesh.cafehaze.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AllInclusive
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.TableBar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.umesh.cafehaze.view.components.MenuItemCard
import com.umesh.cafehaze.viewmodel.MenuViewModel
import com.umesh.cafehaze.viewmodel.OrderViewModel

private val Background = Color(0xFFF7F3EE)

private val PrimaryText = Color(0xFF3E2723)
private val SecondaryText = Color(0xFF8D6E63)

private val DarkBrown = Color(0xFF4E342E)
private val MediumBrown = Color(0xFF795548)
private val Cream = Color(0xFFFFFBF7)

@Composable
fun FavoriteScreen(
    menuViewModel: MenuViewModel,
    orderViewModel: OrderViewModel,
    innerPadding: PaddingValues,
) {

    LaunchedEffect(Unit) {
        if (menuViewModel.menuItems.value.isEmpty()) {
            menuViewModel.loadMenu()
        }
    }

    val favorites by menuViewModel.favoriteItems.collectAsState()

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = innerPadding.calculateStartPadding(
                    LayoutDirection.Ltr
                ),
                end = innerPadding.calculateEndPadding(
                    LayoutDirection.Ltr
                ),
                bottom = innerPadding.calculateBottomPadding()
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {

            // TOP CARD

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 18.dp,
                        vertical = 16.dp
                    )
            ) {

                Surface(
                    modifier = Modifier.shadow(
                        elevation = 20.dp,
                        shape = RoundedCornerShape(34.dp),
                        ambientColor = Color(0x1A000000),
                        spotColor = Color(0x14000000)
                    ),

                    shape = RoundedCornerShape(34.dp),

                    color = Cream
                ) {

                    Column(
                        modifier = Modifier.padding(22.dp)
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            // HEART ICON

                            Box(
                                modifier = Modifier
                                    .size(68.dp)
                                    .clip(CircleShape)
                                    .background(
                                        brush = Brush.linearGradient(
                                            colors = listOf(
                                                Color(0xFFD7B899),
                                                Color(0xFF9C6B4F),
                                                Color(0xFF5D4037)
                                            )
                                        )
                                    ),

                                contentAlignment = Alignment.Center
                            ) {

                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = null,

                                    tint = Color.White,

                                    modifier = Modifier.size(30.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(18.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {

                                Text(
                                    text = "Your Favorites",

                                    style = MaterialTheme.typography.headlineMedium,

                                    fontWeight = FontWeight.Black,

                                    color = PrimaryText
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "Curated items you love most",

                                    style = MaterialTheme.typography.bodyLarge,

                                    color = SecondaryText
                                )
                            }

                            Surface(
                                shape = CircleShape,

                                color = Color(0xFFF1E7DF)
                            ) {

                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,

                                    tint = MediumBrown,

                                    modifier = Modifier
                                        .padding(12.dp)
                                        .size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // STATS CARD

                        Surface(
                            shape = RoundedCornerShape(28.dp),

                            color = Color(0xFFF3ECE7)
                        ) {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = 20.dp,
                                        vertical = 18.dp
                                    ),

                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {

                                    Text(
                                        text = "${favorites.size}",

                                        style = MaterialTheme.typography.displaySmall,

                                        fontWeight = FontWeight.Black,

                                        color = DarkBrown
                                    )

                                    Spacer(modifier = Modifier.height(2.dp))

                                    Text(
                                        text = "Saved Favorites",

                                        style = MaterialTheme.typography.bodyLarge,

                                        color = SecondaryText
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Surface(
                                        shape = CircleShape,

                                        color = Color(0xFFE6D8CF)
                                    ) {

                                        Icon(
                                            imageVector = Icons.Default.AllInclusive,
                                            contentDescription = null,

                                            tint = DarkBrown,

                                            modifier = Modifier
                                                .padding(10.dp)
                                                .size(22.dp)
                                        )
                                    }


                                }
                            }
                        }
                    }
                }
            }

            // EMPTY STATE

            if (favorites.isEmpty()) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 100.dp),

                    contentAlignment = Alignment.Center
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Box(
                            modifier = Modifier
                                .size(82.dp)
                                .clip(CircleShape)
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFFD7B899),
                                            Color(0xFF9C6B4F),
                                            Color(0xFF5D4037)
                                        )
                                    )
                                ),

                            contentAlignment = Alignment.Center
                        ) {

                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,

                                tint = Color.White,

                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "No Favorites Yet",

                            style = MaterialTheme.typography.headlineSmall,

                            fontWeight = FontWeight.Black,

                            color = PrimaryText
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Tap the heart icon on items to save them here.",

                            style = MaterialTheme.typography.bodyMedium,

                            color = SecondaryText
                        )
                    }
                }

            } else {

                // GRID

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),

                    userScrollEnabled = false,

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(
                            (((favorites.size + 1) / 2) * 320).dp
                        ),

                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 120.dp
                    ),

                    verticalArrangement = Arrangement.spacedBy(16.dp),

                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    items(
                        items = favorites,
                        key = { it.id }
                    ) { item ->

                        MenuItemCard(
                            item = item,

                            orderViewModel = orderViewModel,

                            onToggleFavorite = {
                                menuViewModel.toggleFavorite(it)
                            }
                        )
                    }
                }
            }
        }
    }
}