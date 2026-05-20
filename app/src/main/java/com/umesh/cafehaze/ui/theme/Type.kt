package com.umesh.cafehaze.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.umesh.cafehaze.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

val bebasNee = FontFamily(
    Font(R.font.bebas_neue, FontWeight.Normal)
)

val diphylleia = FontFamily(
    Font(R.font.diphylleia, FontWeight.Normal)
)

val pinyonScript = FontFamily(
    Font(R.font.pinyon_script, FontWeight.Normal)
)
val monteCarlo = FontFamily(
    Font(R.font.monte_carlo, FontWeight.Normal)
)

val Arizona = FontFamily(
    Font(R.font.arizonia_regular, FontWeight.Normal)
)

val Felipa = FontFamily(
    Font(R.font.felipa_regular, FontWeight.Normal)
)