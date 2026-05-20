package com.umesh.cafehaze.model.repository

import com.umesh.cafehaze.model.data.Bill
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import javax.inject.Inject

class DashboardRepository @Inject constructor(
    private val client: SupabaseClient
) {

    // ✅ Fetch all bills
    suspend fun getBills(): List<Bill> {
        return client
            .from("bills")
            .select()
            .decodeList<Bill>()
    }
    suspend fun getRecentBills(startDate: String): List<Bill> {
        return client.from("bills").select {
            filter {
                gte("created_at", startDate)
            }
        }.decodeList()
    }
}