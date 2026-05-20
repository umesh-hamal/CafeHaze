package com.umesh.cafehaze.model.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BillItem(
    val id: String? = null,

    @SerialName("bill_id")
    val billId: Int? = null,

    @SerialName("menu_item_id")
    val menuItemId: Int,

    val quantity: Int,

    @SerialName("price_at_time")
    val priceAtTime: Double
)