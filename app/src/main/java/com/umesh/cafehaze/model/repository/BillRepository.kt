package com.umesh.cafehaze.model.repository

import com.umesh.cafehaze.model.data.*
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BillRepository @Inject constructor(
    private val supabase: SupabaseClient
) {

    fun getAllBills(): Flow<List<BillWithItems>> = flow {

        while (true) {
            try {
                // 🔹 Fetch bills
                val bills = supabase
                    .from("bills")
                    .select {
                        order("created_at", Order.DESCENDING)
                    }
                    .decodeList<Bill>()

                // 🔹 Fetch bill items
                val items = supabase
                    .from("bill_items")
                    .select()
                    .decodeList<BillItem>()

                // 🔹 Group items by bill_id
                val groupedItems = items.groupBy { it.billId }

                // 🔹 Combine
                val result = bills.map { bill ->
                    BillWithItems(
                        bill = bill,
                        items = groupedItems[bill.billId] ?: emptyList()
                    )
                }

                emit(result)

            } catch (e: Exception) {
                e.printStackTrace()
                emit(emptyList())
            }

            delay(3000)
        }
    }
}