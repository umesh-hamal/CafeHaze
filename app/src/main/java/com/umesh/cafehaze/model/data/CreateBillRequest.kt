package com.umesh.cafehaze.model.data

import kotlinx.serialization.Serializable

@Serializable
data class CreateBillRequest(
    val status: String = "pending"
)