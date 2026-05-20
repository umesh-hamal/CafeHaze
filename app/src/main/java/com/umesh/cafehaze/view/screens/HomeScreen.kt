package com.umesh.cafehaze.view.screens

import com.umesh.cafehaze.view.components.MyHorizontalLine
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.TableBar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.umesh.cafehaze.view.components.CategoryRow
import com.umesh.cafehaze.view.components.MenuItemCard
import com.umesh.cafehaze.view.components.SearchBar
import com.umesh.cafehaze.viewmodel.MenuViewModel
import com.umesh.cafehaze.viewmodel.OrderViewModel

private val BackgroundColor = Color(0xFFF7F6E5)
private val AccentBrown = Color(0xFF8B5E3C)
private val Cream = Color(0xFFFFFBF7)
private val PrimaryText = Color(0xFF3E2723)
private val SecondaryText = Color(0xFF8D6E63)
private val DarkBrown = Color(0xFF4E342E)
private val MediumBrown = Color(0xFF795548)

@Composable
fun HomeScreen(
    menuViewModel: MenuViewModel,
    orderViewModel: OrderViewModel,
) {

    val categories by menuViewModel.categories.collectAsState()
    val selectedCategoryId by menuViewModel.selectedCategoryId.collectAsState()
    val searchQuery by menuViewModel.searchQuery.collectAsState()
    val menu by menuViewModel.filteredItems.collectAsState()

    val listState = rememberLazyListState()

    // Smooth scroll to top without rearranging UI
    LaunchedEffect(selectedCategoryId) {
        if (listState.firstVisibleItemIndex > 2) {
            listState.scrollToItem(0)
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(bottom = 130.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        // ================= HEADER =================
        item {
            HeaderSection(menu.size)
        }

        // ================= STICKY =================
        stickyHeader {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BackgroundColor)
            ) {

                SearchBar(
                    query = searchQuery,
                    onQueryChange = {
                        menuViewModel.updateSearch(it)
                    },
                    onSearch = {
                        menuViewModel.updateSearch(searchQuery)
                    }
                )

                CategoryRow(
                    categories = categories,
                    selectedCategoryId = selectedCategoryId,
                    onCategoryClick = {
                        menuViewModel.selectCategory(it)
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))
                MyHorizontalLine()
            }
        }

        // ================= EMPTY =================
        if (menu.isEmpty()) {

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 60.dp),
                    contentAlignment = Alignment.Center
                ) {

                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.82f),
                        shape = RoundedCornerShape(36.dp),
                        colors = CardDefaults.cardColors(
                            containerColor =
                                Color(0xFFFFFCF7)
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 8.dp
                        )
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 24.dp,
                                    vertical = 36.dp
                                ),
                            horizontalAlignment =
                                Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = "☕",
                                style =
                                    MaterialTheme
                                        .typography
                                        .displayMedium
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(14.dp)
                            )

                            Text(
                                text = "No Items Found",
                                style =
                                    MaterialTheme
                                        .typography
                                        .headlineSmall,
                                fontWeight =
                                    FontWeight.Bold,
                                color =
                                    Color(0xFF2A211A)
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(8.dp)
                            )

                            Text(
                                text =
                                    "Try searching for something else.",
                                style =
                                    MaterialTheme
                                        .typography
                                        .bodyLarge,
                                color =
                                    Color(0xFF8B8178)
                            )
                        }
                    }
                }}
        }

        item {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 700.dp), // keeps layout stable
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                menu.chunked(2).forEach { row ->

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement =
                            Arrangement.spacedBy(16.dp)
                    ) {

                        Box(
                            modifier = Modifier.weight(1f)
                        ) {
                            MenuItemCard(
                                item = row[0],
                                orderViewModel = orderViewModel,
                                onToggleFavorite = {
                                    menuViewModel.toggleFavorite(it)
                                }
                            )
                        }

                        Box(
                            modifier = Modifier.weight(1f)
                        ) {
                            if (row.size > 1) {
                                MenuItemCard(
                                    item = row[1],
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
    }
}

@Composable
fun HeaderSection(itemCount: Int) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 12.dp)
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(34.dp),
                ambientColor = Color(0x1A000000),
                spotColor = Color(0x14000000)
            ),
        shape = RoundedCornerShape(34.dp),
        color = Cream
    ) {

        Column(modifier = Modifier.padding(22.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.linearGradient(
                                listOf(
                                    Color(0xFFD7B899),
                                    Color(0xFF9C6B4F),
                                    Color(0xFF5D4037)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.LocalCafe,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.width(18.dp))

                Column(modifier = Modifier.weight(1f)) {

                    Text(
                        text = "Cafe Haze",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black,
                        color = PrimaryText
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Premium coffee & handcrafted bites",
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

            Surface(
                shape = RoundedCornerShape(28.dp),
                color = Color(0xFFF3ECE7)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(modifier = Modifier.weight(1f)) {

                        Text(
                            text = "$itemCount",
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Black,
                            color = DarkBrown
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = "Available Menu Items",
                            style = MaterialTheme.typography.bodyLarge,
                            color = SecondaryText
                        )
                    }

                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFE6D8CF)
                    ) {

                        Icon(
                            imageVector = Icons.Default.TableBar,
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