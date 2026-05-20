package com.umesh.cafehaze.view.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.umesh.cafehaze.utils.SalesOverviewChart

private val CardBackground = Color(0xFFFFFCF8)
private val SoftText = Color(0xFF8B8178)
private val PrimaryText = Color(0xFF191612)

@Composable
fun SalesOverviewCard(
    onlineData: List<Float>,
    offlineData: List<Float>,
    labels: List<String>
) {

    val totalOnline = onlineData.sum()
    val totalOffline = offlineData.sum()
    val total = totalOnline + totalOffline

    val onlinePercent =
        if (total > 0) ((totalOnline / total) * 100).toInt() else 0

    val offlinePercent =
        if (total > 0) ((totalOffline / total) * 100).toInt() else 0

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(38.dp),

            elevation = CardDefaults.cardElevation(
                defaultElevation = 14.dp
            ),

            colors = CardDefaults.cardColors(
                containerColor = CardBackground
            )
        ) {

            Column(
                modifier = Modifier.padding(horizontal = 22.dp, vertical = 22.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {

                    Column {
                        Text(
                            text = "Sales Overview",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = PrimaryText
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Weekly revenue analytics",
                            style = MaterialTheme.typography.bodyMedium,
                            color = SoftText
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(50),
                        color = Color(0xFFF4ECE4)
                    ) {
                        Text(
                            text = "₹%.0f".format(total),
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            color = Color(0xFF8B5E3C),
                            fontWeight = FontWeight.ExtraBold,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(26.dp))

                Surface(
                    modifier = Modifier.shadow(
                        elevation = 10.dp,
                        shape = RoundedCornerShape(30.dp),
                        ambientColor = Color(0x22000000),
                        spotColor = Color(0x18000000)
                    ),
                    shape = RoundedCornerShape(30.dp),
                    color = Color.White
                ) {

                    Box(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 22.dp)
                    ) {
                        SalesOverviewChart(
                            onlineData = onlineData,
                            offlineData = offlineData,
                            labels = labels
                        )
                    }
                }

                Spacer(modifier = Modifier.height(26.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    ModernAnalyticsCard(
                        modifier = Modifier.weight(1f),
                        title = "Online",
                        amount = totalOnline,
                        percent = onlinePercent,
                        color = Color(0xFF6366F1)
                    )

                    ModernAnalyticsCard(
                        modifier = Modifier.weight(1f),
                        title = "Offline",
                        amount = totalOffline,
                        percent = offlinePercent,
                        color = Color(0xFF10B981)
                    )
                }
            }
        }
    }
}

@Composable
fun ModernAnalyticsCard(
    modifier: Modifier = Modifier,
    title: String,
    amount: Float,
    percent: Int,
    color: Color
) {

    Surface(
        modifier = modifier
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(28.dp),
                ambientColor = Color(0x22000000),
                spotColor = Color(0x18000000)
            ),
        shape = RoundedCornerShape(28.dp),
        color = Color(0xFFFFFEFC),
        border = BorderStroke(
            1.dp,
            Color(0xFFF2ECE5)
        )
    ) {

        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(color, CircleShape)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = title,
                        color = SoftText,
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Surface(
                    shape = RoundedCornerShape(100),
                    color = color.copy(alpha = 0.08f)
                ) {
                    Text(
                        text = "$percent%",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                        color = color,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "₹%.0f".format(amount),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = PrimaryText
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Revenue",
                color = Color(0xFFAAA097),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}