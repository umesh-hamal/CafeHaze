package com.umesh.cafehaze.view.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.umesh.cafehaze.R
import com.umesh.cafehaze.model.data.Category
import com.umesh.cafehaze.utils.getIconResId

private val AccentStart = Color(0xFFD6B38A)
private val AccentEnd = Color(0xFF9C6B4B)

private val PrimaryText = Color(0xFF1E1B16)
private val SecondaryText = Color(0xFF8A7B6D)

private val SoftCard = Color(0xFFFFFCF8)

@Composable
fun CategoryRow(
    categories: List<Category>,
    selectedCategoryId: Int?,
    onCategoryClick: (Int?) -> Unit
) {

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(start = 24.dp, end = 24.dp)
    ) {

        item {
            CategoryChip(
                name = "All",
                category = null,
                isSelected = selectedCategoryId == null,
                onClick = onCategoryClick
            )
        }

        items(
            categories,
            key = { it.id }
        ) { category ->

            CategoryChip(
                name = category.name,
                category = category,
                isSelected = selectedCategoryId == category.id,
                onClick = onCategoryClick
            )
        }
    }
}

@Composable
fun CategoryChip(
    name: String,
    category: Category?,
    isSelected: Boolean,
    onClick: (Int?) -> Unit
) {

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) Color.Transparent else Color(0xFFE9DED2),
        label = ""
    )

    val textColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else PrimaryText,
        label = ""
    )

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.03f else 1f,
        label = ""
    )

    val interactionSource = remember { MutableInteractionSource() }

    val iconRes = remember(category?.id) {
        category?.let { getIconResId(it.id) } ?: R.drawable.ic_all
    }

    Box(
        modifier = Modifier
            .scale(scale)
            .clip(RoundedCornerShape(28.dp))
            .background(
                brush =
                    if (isSelected)
                        Brush.linearGradient(listOf(AccentStart, AccentEnd))
                    else
                        Brush.linearGradient(listOf(SoftCard, SoftCard))
            )
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(28.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null // ✅ no ripple
            ) {
                onClick(category?.id)
            }
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(58.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected)
                            Color.White.copy(alpha = 0.18f)
                        else
                            Color(0xFFF6EFE8)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = name,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.padding(top = 2.dp, end = 4.dp)
            ) {

                Text(
                    text = name,
                    color = textColor,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (isSelected) "Selected" else "Browse",
                    color = if (isSelected)
                        Color.White.copy(alpha = 0.82f)
                    else
                        SecondaryText,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}