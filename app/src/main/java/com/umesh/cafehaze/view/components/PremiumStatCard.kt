package com.umesh.cafehaze.view.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.abs

@Composable
fun PremiumStatCard(
    title: String,
    value: Float,
    previousValue: Float,
    accent: Color,
    modifier: Modifier = Modifier
) {

    // -------- Currency Formatter --------
    val formatter = remember {
        NumberFormat.getCurrencyInstance(
            Locale("en", "IN")
        )
    }

    val animatedValue by animateFloatAsState(
        targetValue = value,
        animationSpec = tween(
            durationMillis = 800,
            easing = FastOutSlowInEasing
        ),
        label = "value_anim"
    )

    val formattedValue =
        remember(animatedValue) {
            formatter.format(animatedValue)
        }

    // -------- Growth Calculation --------
    val growth = when {

        previousValue <= 0f &&
                value > 0f -> 100f

        previousValue <= 0f ->
            0f

        else -> {

            val rawGrowth =
                ((value - previousValue)
                        / previousValue) * 100f

            rawGrowth.coerceIn(
                -100f,
                100f
            )
        }
    }

    val isPositive = growth >= 0

    val targetGrowthColor =
        if (isPositive) {
            Color(0xFF16A34A)
        } else {
            Color(0xFFDC2626)
        }

    val growthColor by animateColorAsState(
        targetValue = targetGrowthColor,
        animationSpec = tween(500),
        label = "growth_color"
    )

    val arrow =
        if (isPositive) "▲"
        else "▼"

    val growthText = when {

        previousValue <= 0f &&
                value > 0f -> {
            "New"
        }

        abs(growth) >= 1000f -> {

            val multiplier =
                value / previousValue

            "${"%.1f".format(
                multiplier
            )}x"
        }

        else -> {

            "$arrow ${
                "%.1f".format(
                    abs(growth)
                )
            }%"
        }
    }
    // -------- Progress Logic --------
    val progressRatio = when {

        previousValue <= 0f -> 1f

        else -> value / previousValue
    }.coerceIn(0f, 1.5f)

    val targetProgress =
        (progressRatio / 1.5f)
            .coerceIn(0.1f, 1f)

    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(
            durationMillis = 700,
            easing = FastOutSlowInEasing
        ),
        label = "progress_anim"
    )

    // -------- UI --------
    Card(
        modifier = modifier
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(34.dp),
                ambientColor =
                    Color(0x33000000),
                spotColor =
                    Color(0x22000000)
            ),
        shape = RoundedCornerShape(34.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                Color(0xFFFFFBF7)
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 25.dp
                )
        ) {

            // Header
            Surface(
                shape =
                    RoundedCornerShape(50),
                color =
                    accent.copy(alpha = 0.10f)
            ) {

                Row(
                    modifier = Modifier.padding(
                        horizontal = 12.dp,
                        vertical = 6.dp
                    ),
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(
                                accent,
                                CircleShape
                            )
                    )

                    Spacer(
                        modifier =
                            Modifier.width(8.dp)
                    )

                    Text(
                        text = title,
                        color = accent,
                        fontWeight =
                            FontWeight.Bold,
                        style =
                            MaterialTheme
                                .typography
                                .bodyMedium
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )

            // Revenue
            Text(
                text = formattedValue,
                style =
                    MaterialTheme
                        .typography
                        .headlineLarge,
                fontWeight =
                    FontWeight.ExtraBold,
                color =
                    Color(0xFF1A1713)
            )

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            Text(
                text = "Revenue",
                color =
                    Color(0xFF9A938B),
                style =
                    MaterialTheme
                        .typography
                        .bodyMedium
            )

            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )

            // Progress Bar
            Surface(
                modifier =
                    Modifier.fillMaxWidth(),
                shape =
                    RoundedCornerShape(50),
                color =
                    accent.copy(alpha = 0.08f)
            ) {

                Box(
                    modifier = Modifier
                        .height(10.dp)
                        .fillMaxWidth(
                            animatedProgress
                        )
                        .background(
                            accent,
                            RoundedCornerShape(50)
                        )
                )
            }

            Spacer(
                modifier =
                    Modifier.height(18.dp)
            )

            // Growth Row
            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text = growthText,
                    color = growthColor,
                    fontWeight =
                        FontWeight.Bold
                )

                Spacer(
                    modifier =
                        Modifier.width(4.dp)
                )

                Text(
                    text =
                        if (title == "Today") {
                            "vs yesterday"
                        } else {
                            "vs last month"
                        },
                    color =
                        Color(0xFF8A8178),
                    style =
                        MaterialTheme
                            .typography
                            .bodySmall
                )
            }
        }
    }
}