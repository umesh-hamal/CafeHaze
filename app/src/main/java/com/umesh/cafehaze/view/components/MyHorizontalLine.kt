package com.umesh.cafehaze.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MyHorizontalLine(){ Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {

        // Outer blur (very soft)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color.Transparent,
                            Color(0xFFE6D3C3).copy(alpha = 0.15f),
                            Color(0xFFE6D3C3).copy(alpha = 0.15f),
                            Color.Transparent
                        )
                    )
                )
        )

        // Mid glow
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color.Transparent,
                            Color(0xFFE6D3C3).copy(alpha = 0.3f),
                            Color(0xFFE6D3C3).copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    )
                )
        )

        // Sharp center line
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color.Transparent,
                            Color(0xFFD6B38A),
                            Color(0xFFD6B38A),
                            Color.Transparent
                        )
                    )
                )
        )
    }
}