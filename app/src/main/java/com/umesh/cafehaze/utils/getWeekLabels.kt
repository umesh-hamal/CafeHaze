package com.umesh.cafehaze.utils

import java.text.SimpleDateFormat
import java.util.*

fun getWeeekLabels(): List<String> {
    val cal = Calendar.getInstance()
    val dayFmt = SimpleDateFormat("EEE", Locale.getDefault())
    val dateFmt = SimpleDateFormat("d", Locale.getDefault())

    return List(7) {
        val label = "${dateFmt.format(cal.time)} ${dayFmt.format(cal.time)}"
        cal.add(Calendar.DAY_OF_MONTH, -1)
        label
    }.reversed()
}