package com.umesh.cafehaze.utils

import java.time.*
import java.time.format.DateTimeFormatter

private val IST: ZoneId = ZoneId.of("Asia/Kolkata")

fun parseToIst(dateTime: String): ZonedDateTime {
    return try {
        val parsed = LocalDateTime.parse(dateTime)
        parsed.atZone(ZoneOffset.UTC).withZoneSameInstant(IST)

    } catch (_: Exception) {

        try {
            OffsetDateTime.parse(dateTime)
                .atZoneSameInstant(IST)

        } catch (_: Exception) {

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

            LocalDateTime.parse(dateTime, formatter)
                .atZone(ZoneOffset.UTC)
                .withZoneSameInstant(IST)
        }
    }
}