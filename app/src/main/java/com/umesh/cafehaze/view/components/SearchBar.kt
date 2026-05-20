package com.umesh.cafehaze.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private val SearchBackground = Color(0xFFF3E8DC)

private val GradientStart = Color(0xFFD6B38A)
private val GradientMiddle = Color(0xFFB17B55)
private val GradientEnd = Color(0xFF6B3E26)

private val PrimaryBrown = Color(0xFF4A2C1D)
private val SecondaryBrown = Color(0xFF8A6A58)

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit
) {

    var localQuery by remember(query) {
        mutableStateOf(query)
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    // DEBOUNCE
    LaunchedEffect(localQuery) {
        delay(300)
        onQueryChange(localQuery)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 12.dp),

        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = Modifier
                .matchParentSize()
                .blur(40.dp)
                .background(
                    Color(0xFFF7F6E5),
                    RoundedCornerShape(32.dp)
                )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(32.dp),
                    ambientColor = Color.Transparent,
                    spotColor = Color.Transparent
                )
                .clip(RoundedCornerShape(32.dp))
                .background(SearchBackground)
        ) {

            TextField(
                value = localQuery,

                onValueChange = {
                    localQuery = it
                },

                modifier = Modifier.fillMaxWidth(),

                placeholder = {

                    Text(
                        text = "Search coffee, snacks, desserts...",

                        color = SecondaryBrown,

                        fontSize = 15.sp,

                        fontWeight = FontWeight.Medium
                    )
                },

                // SEARCH ICON
                leadingIcon = {

                    Box(
                        modifier = Modifier
                            .padding(start = 6.dp)
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        GradientStart,
                                        GradientMiddle,
                                        GradientEnd
                                    )
                                )
                            ),

                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",

                            tint = Color.White,

                            modifier = Modifier.size(20.dp)
                        )
                    }
                },

                trailingIcon = {

                    AnimatedVisibility(
                        visible = localQuery.isNotEmpty(),

                        enter = fadeIn(),

                        exit = fadeOut()
                    ) {

                        IconButton(
                            onClick = {
                                localQuery = ""
                                onQueryChange("")
                            }
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE7D6C7)),

                                contentAlignment = Alignment.Center
                            ) {

                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear",

                                    tint = PrimaryBrown,

                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                },

                singleLine = true,

                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),

                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSearch()
                        keyboardController?.hide()
                    }
                ),

                textStyle = LocalTextStyle.current.copy(
                    fontSize = 16.sp,

                    fontWeight = FontWeight.SemiBold,

                    color = PrimaryBrown
                ),

                shape = RoundedCornerShape(32.dp),

                colors = TextFieldDefaults.colors(

                    focusedContainerColor = SearchBackground,

                    unfocusedContainerColor = SearchBackground,

                    disabledContainerColor = SearchBackground,

                    focusedIndicatorColor = Color.Transparent,

                    unfocusedIndicatorColor = Color.Transparent,

                    disabledIndicatorColor = Color.Transparent,

                    cursorColor = GradientMiddle
                )
            )
        }
    }
}