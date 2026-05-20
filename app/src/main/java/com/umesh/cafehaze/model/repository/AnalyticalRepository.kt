package com.umesh.cafehaze.data

import com.umesh.cafehaze.model.data.Bill
import com.umesh.cafehaze.model.data.SalesPoint
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import jakarta.inject.Inject
import kotlinx.datetime.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AnalyticsRepository @Inject constructor(
    private val supabase: SupabaseClient
) {

    suspend fun getWeeklySales(): List<SalesPoint> {

        // ✅ last 7 days filter
        val startDate = LocalDateTime.now()
            .minusDays(7)
            .format(DateTimeFormatter.ISO_DATE_TIME)

        val bills = supabase.from("bills")
            .select {
                filter {
                    gte("created_at", startDate)
                }
            }
            .decodeList<Bill>()

        // ✅ group by date
        val grouped = bills.groupBy {
            it.createdAt.substring(0, 10)
        }

        return grouped.map { (date, list) ->

            val total = list.sumOf { it.totalAmount }

            SalesPoint(
                label = formatToDay(date),
                total = total
            )
        }.sortedBy { it.label }
    }

    private fun formatToDay(date: String): String {
        val localDate = LocalDate.parse(date)
        return localDate.dayOfWeek.name.take(3) // MON, TUE...
    }
}