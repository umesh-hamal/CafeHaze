package com.umesh.cafehaze.model.data
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MenuItem(
    val id: Int,
    val name: String,
    val price: Double, // ✅ FIXED (was Int)

    @SerialName("category_id")
    val categoryId: Int,

    val image: String? = null,
    val isFavorite: Boolean = false
)