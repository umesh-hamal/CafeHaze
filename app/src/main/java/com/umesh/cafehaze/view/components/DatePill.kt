package com.umesh.cafehaze.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val AccentPurple = Color(0xFF7C3AED)

@Composable
fun DatePill(
    text: String,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(Color.White)
            .border(
                width = 1.dp,
                color = Color(0xFFE5E7EB),
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 16.dp, vertical = 10.dp),

        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(8.dp)
                .background(AccentPurple, CircleShape)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = text,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF020617)
        )
    }
}

@Composable
fun DateHeader(
    date: LocalDate,
    today: LocalDate,
    yesterday: LocalDate
) {

    val title = when (date) {
        today -> "Today"
        yesterday -> "Yesterday"
        else -> date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F3EA))
            .padding(vertical = 6.dp)
    ) {

        DatePill(
            text = title,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}