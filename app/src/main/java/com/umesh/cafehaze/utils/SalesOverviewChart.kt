package com.umesh.cafehaze.utils

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.max

@Composable
fun SalesOverviewChart(
    onlineData: List<Float>,
    offlineData: List<Float>,
    labels: List<String>
) {

    // Ensure 7 items
    fun ensure7(data: List<Float>): List<Float> =
        if (data.size >= 7) data.takeLast(7)
        else List(7 - data.size) { 0f } + data

    val safeOnline = ensure7(onlineData)
    val safeOffline = ensure7(offlineData)

    val safeLabels = if (labels.size >= 7) labels.takeLast(7)
    else listOf("29 Mon","30 Tue","1 Wed","2 Thu","3 Fri","4 Sat","5 Sun")

    if (safeOnline.all { it == 0f } && safeOffline.all { it == 0f }) {
        Text("No Sales Data")
        return
    }

    val maxValue = max(
        safeOnline.maxOrNull() ?: 0f,
        safeOffline.maxOrNull() ?: 0f
    ).takeIf { it > 0 } ?: 1f

    Column(modifier = Modifier.fillMaxWidth()) {

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        ) {

            val width = size.width
            val height = size.height
            val paddingTop = 24f

            val count = 7
            val stepX = width / (count - 1)

            fun points(data: List<Float>): List<Offset> =
                List(count) { i ->
                    val value = data[i]
                    val x = i * stepX
                    val y = height - (value / maxValue) * (height - paddingTop)
                    Offset(x, y)
                }

            fun smoothPath(pts: List<Offset>): Path = Path().apply {
                moveTo(pts.first().x, pts.first().y)

                for (i in 1 until pts.size) {
                    val prev = pts[i - 1]
                    val curr = pts[i]

                    cubicTo(
                        prev.x + (curr.x - prev.x) / 3,
                        prev.y,
                        prev.x + 2 * (curr.x - prev.x) / 3,
                        curr.y,
                        curr.x,
                        curr.y
                    )
                }
            }

            fun fillPath(pts: List<Offset>, line: Path): Path = Path().apply {
                addPath(line)
                lineTo(pts.last().x, height)
                lineTo(pts.first().x, height)
                close()
            }

            // 🔵 Online
            val oPts = points(safeOnline)
            val oLine = smoothPath(oPts)
            val oFill = fillPath(oPts, oLine)

            drawPath(
                path = oFill,
                brush = Brush.verticalGradient(
                    listOf(Color(0xFF3B82F6).copy(alpha = 0.25f), Color.Transparent)
                )
            )
            drawPath(oLine, color = Color(0xFF3B82F6), style = Stroke(width = 3f))

            // 🟢 Offline
            val fPts = points(safeOffline)
            val fLine = smoothPath(fPts)
            val fFill = fillPath(fPts, fLine)

            drawPath(
                path = fFill,
                brush = Brush.verticalGradient(
                    listOf(Color(0xFF10B981).copy(alpha = 0.25f), Color.Transparent)
                )
            )
            drawPath(fLine, color = Color(0xFF10B981), style = Stroke(width = 3f))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            safeLabels.forEachIndexed { index, label ->

                val parts = label.split(" ")
                val date = parts.getOrNull(0) ?: ""
                val day = parts.getOrNull(1) ?: ""

                val isToday = index == safeLabels.lastIndex

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // Date (Primary)
                    Text(
                        text = date,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isToday) Color(0xFF3B82F6) else Color(0xFF191612)
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    // Day (Secondary)
                    Text(
                        text = day,
                        fontSize = 9.sp,
                        color = if (isToday)
                            Color(0xFF3B82F6).copy(alpha = 0.7f)
                        else
                            Color(0xFF9A938B)
                    )
                }
            }
        }
    }
}