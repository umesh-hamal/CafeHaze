package com.umesh.cafehaze.utils

import com.umesh.cafehaze.R
import kotlin.collections.get

val categoryIconMap = mapOf(
    1 to R.drawable.ic_tea,
    2 to R.drawable.ic_snacks,
    3 to R.drawable.ic_sandwich,
    4 to R.drawable.ic_rolls,
    5 to R.drawable.ic_chinese,
    6 to R.drawable.ic_coffee,
    7 to R.drawable.ic_milkshake,
    8 to R.drawable.ic_noodles,
    9 to R.drawable.ic_soup,
    10 to R.drawable.ic_drinks,
    11 to R.drawable.ic_desserts,
    12 to R.drawable.ic_waffle
)

fun getIconResId(id: Int?): Int {
    return categoryIconMap[id] ?: R.drawable.ic_default
}