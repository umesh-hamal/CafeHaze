package com.umesh.cafehaze.model.repository

import android.util.Log
import com.umesh.cafehaze.model.data.Bill
import com.umesh.cafehaze.model.data.BillItem
import com.umesh.cafehaze.model.data.CreateBillRequest
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val client: SupabaseClient
) {

    suspend fun createOrder(items: List<BillItem>): Int {
        try {
            // ✅ Create bill
            val bill = client.from("bills")
                .insert(CreateBillRequest()) {
                    select()
                }
                .decodeSingle<Bill>()

            val billId = bill.billId

            // ✅ Insert items (NO mapOf → avoids serialization crash)
            val billItems = items.map {
                BillItem(
                    billId = billId,
                    menuItemId = it.menuItemId,
                    quantity = it.quantity,
                    priceAtTime = it.priceAtTime
                )
            }

            client.from("bill_items").insert(billItems)

            // ✅ Update total
            val total = items.sumOf { it.priceAtTime * it.quantity }

            client.from("bills").update(
                mapOf("total_amount" to total)
            ) {
                filter { eq("bill_id", billId) }
            }

            return billId

        } catch (e: Exception) {
            Log.e("ORDER_ERROR", "CREATE ORDER FAILED", e)
            throw e
        }
    }

    suspend fun markAsPaid(billId: Int, method: String) {
        client.from("bills").update(
            mapOf(
                "status" to "paid",
                "payment_method" to method
            )
        ) {
            filter { eq("bill_id", billId) }
        }
    }
}