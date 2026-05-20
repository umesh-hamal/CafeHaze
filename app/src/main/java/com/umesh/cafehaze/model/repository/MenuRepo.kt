package com.umesh.cafehaze.model.repository

import android.util.Log
import com.umesh.cafehaze.model.data.MenuItem
import com.umesh.cafehaze.model.data.Category
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import javax.inject.Inject

class MenuRepository @Inject constructor(
    private val client: SupabaseClient
) {

    // ✅ Fetch menu items
    suspend fun getMenuItems(): List<MenuItem> {
        return try {
            val result = client
                .from("menu_items")
                .select {
                    order("id", Order.ASCENDING) // ✅ FIXED
                }
                .decodeList<MenuItem>()

            Log.d("SUPABASE_DEBUG", "Menu size = ${result.size}")
            result

        } catch (e: Exception) {
            Log.e("MENU_ERROR", "FETCH MENU FAILED", e)
            emptyList()
        }
    }

    // ✅ Fetch categories (ORDERED by ID)
    suspend fun getCategories(): List<Category> {
        return try {
            val result = client
                .from("categories")
                .select {
                    order("id", Order.ASCENDING) // ✅ FIXED
                }
                .decodeList<Category>()

            Log.d("SUPABASE_DEBUG", "Categories size = ${result.size}")
            result

        } catch (e: Exception) {
            Log.e("MENU_ERROR", "FETCH CATEGORIES FAILED", e)
            emptyList()
        }
    }
}