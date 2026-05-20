package com.umesh.cafehaze.model.repository

import android.util.Log
import com.umesh.cafehaze.model.data.FavoriteDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import javax.inject.Inject

class FavoriteRepository @Inject constructor(
    private val client: SupabaseClient
) {

    // 🔥 In-memory cache for fast UI
    private var cachedFavorites: MutableSet<Int> = mutableSetOf()

    // ✅ Fetch favorites from DB
    suspend fun getFavorites(): List<FavoriteDto> {
        return try {
            val result = client
                .from("favorites")
                .select()
                .decodeList<FavoriteDto>()

            // update cache
            cachedFavorites = result.map { it.itemId }.toMutableSet()

            result

        } catch (e: Exception) {
            Log.e("FAV_ERROR", "FETCH FAILED", e)
            emptyList()
        }
    }

    // ✅ Check from cache (instant)
    fun isFavoriteLocal(itemId: Int): Boolean {
        return cachedFavorites.contains(itemId)
    }

    // ✅ Add favorite
    suspend fun addFavorite(itemId: Int): Boolean {
        return try {
            client.from("favorites").upsert(
                mapOf("item_id" to itemId)
            ) {
                onConflict = "item_id"
            }

            // update cache instantly
            cachedFavorites.add(itemId)

            true

        } catch (e: Exception) {
            Log.e("FAV_ERROR", "ADD FAILED", e)
            false
        }
    }

    // ✅ Remove favorite
    suspend fun removeFavorite(itemId: Int): Boolean {
        return try {
            client.from("favorites").delete {
                filter {
                    eq("item_id", itemId)
                }
            }

            // update cache instantly
            cachedFavorites.remove(itemId)

            Log.d("FAV_DEBUG", "DELETED = $itemId")
            true

        } catch (e: Exception) {
            Log.e("FAV_ERROR", "DELETE FAILED", e)
            false
        }
    }

    // 🔥 Toggle favorite (BEST API)
    suspend fun toggleFavorite(itemId: Int): Boolean {
        return if (isFavoriteLocal(itemId)) {
            removeFavorite(itemId)
        } else {
            addFavorite(itemId)
        }
    }
}