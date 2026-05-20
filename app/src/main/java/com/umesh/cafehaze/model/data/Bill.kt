package com.umesh.cafehaze.model.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Bill(

    @SerialName("bill_id")
    val billId: Int,

    @SerialName("total_amount")
    val totalAmount: Double,

    val status: String,

    @SerialName("payment_method")
    val paymentMethod: String? = null,

    @SerialName("created_at")
    val createdAt: String
)